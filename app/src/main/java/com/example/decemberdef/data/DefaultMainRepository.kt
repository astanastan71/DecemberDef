package com.example.decemberdef.data

import android.content.ContentValues.TAG
import android.util.Log
import com.example.decemberdef.ui.screens.listApp.TaskGetState
import com.example.decemberdef.ui.screens.signInApp.LogInState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class DefaultMainRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private var user: FirebaseUser?,
) : MainRepository {

    override suspend fun getUserData(): User {
        val localUser = user
        return if (localUser != null) {
            User(
                userID = localUser.uid,
                isEmailVerified = localUser.isEmailVerified,
                isAnon = localUser.isAnonymous,
                userName = localUser.displayName,
                userEmail = localUser.email,
                userPhoto = localUser.photoUrl
            )
        } else User(
            userID = "Not Found"
        )
    }

    override fun updateUser(firebaseUser: FirebaseUser?) {
        user = firebaseUser
    }

    override suspend fun addCustomTaskAndDirection(textState: RichTextState) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")

            val customCollectionId = customCollectionPath.document().id

            customCollectionPath
                .document(customCollectionId)
                .set(
                    Direction(
                        uid = customCollectionId,
                        progress = 0,
                        count = 1
                    )
                )

            val customTaskId = customCollectionPath
                .document(customCollectionId)
                .collection("tasks").document().id

            val task = Task(
                title = "Без названия",
                uid = customTaskId,
                description = textState.toHtml()
            )

            customCollectionPath
                .document(customCollectionId)
                .collection("tasks")
                .document(customTaskId).set(task)
        } else {
            Log.e(TAG, "USER IS NULL")
        }
    }

    override suspend fun addCustomTask(direction: Direction) {
        try {
            val localUser = user
            if (localUser != null) {
                //Получаем путь к коллекции направлений
                val customCollectionPath = db.collection("users")
                    .document(localUser.uid)
                    .collection("directions")
                //Инициализируем идентификатор задачи
                val customCollectionId = customCollectionPath
                    .document(direction.uid)
                    .collection("tasks")
                    .document().id
                //Определяем экземпляр класса задачи в коллекции
                customCollectionPath
                    .document(direction.uid)
                    .collection("tasks")
                    .document(customCollectionId)
                    .set(
                        Task(
                            uid = customCollectionId
                        )
                    )
                    .addOnCompleteListener {
                        customCollectionPath.document(direction.uid).update(
                            "count", direction.count + 1
                        ).addOnSuccessListener {
                            Log.d(
                                TAG,
                                "DocumentSnapshot successfully updated!"
                            )
                        }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully added!"
                        )
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "Error", it)
                    }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error:", e)

        }
    }

    override suspend fun addCustomDirection() {
        try {
            val localUser = user
            if (localUser != null) {
                //Путь к коллекции
                val customCollectionPath = db.collection("users")
                    .document(localUser.uid)
                    .collection("directions")
                //Определяем идентификатор направления
                val customCollectionId = customCollectionPath.document().id
                customCollectionPath
                    .document(customCollectionId)
                    .set(
                        Direction(
                            uid = customCollectionId,
                            progress = 0
                        ) // назначаем идентификатор в качестве значения атрибута
                    )
                    .addOnCompleteListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully added!"
                        )
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "Error", it)
                    }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error:", e)

        }

    }

    private suspend fun getDirectionsFromFirestore(): QuerySnapshot? {
        val localUser = user
        return if (localUser != null) {
            val collectionReference = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
            collectionReference.get().await()
        } else {
            null
        }
    }

    private fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener() { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    private fun DocumentReference.snapshotFlow(): Flow<Direction> = callbackFlow {
        val listenerRegistration = addSnapshotListener() { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null) {
                val doc = value?.toObject(Direction::class.java)
                if (doc != null) {
                    trySend(doc)
                }
            }

        }
        awaitClose {
            listenerRegistration.remove()
        }
    }


    override fun getDirectionsList(): Flow<List<Direction>>? {
        val localUser = user
        return if (localUser != null) {
            db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .snapshotFlow()
                .map { querySnapshot ->
                    querySnapshot.toObjects(Direction::class.java)
                }
        } else {
            null
        }
    }

    override suspend fun getTasksListFromLink(userID: String, directionId: String): List<Task> {
        return db.collection("users")
            .document(userID)
            .collection("directions")
            .document(directionId)
            .collection("tasks")
            .get().await().toObjects(Task::class.java)
    }


    override suspend fun getDirectionTasks(directionId: String): TaskGetState {
        try {
            val localUser = user
            return if (localUser != null) {
                Log.d(TAG, "MESSAGE: User is not NULL")
                TaskGetState.Success(
                    db.collection("users")
                        .document(localUser.uid)
                        .collection("directions")
                        .document(directionId)
                        .collection("tasks")
                        .snapshotFlow()
                        .map { querySnapshot ->
                            querySnapshot.toObjects(Task::class.java)
                        }
                )
            } else {
                Log.d(TAG, "MESSAGE: user is NULL")
                TaskGetState.Loading
            }
        } catch (e: Exception) {
            Log.w(TAG, "MESSAGE: Error getting documents: ", e)
            return TaskGetState.Error
        }
    }

    override suspend fun getDirectionTasksForAll(directionId: String): Flow<MutableList<Task>>? {
        try {
            val localUser = user
            return if (localUser != null) {
                Log.d(TAG, "MESSAGE: User is not NULL")
                db.collection("users")
                    .document(localUser.uid)
                    .collection("directions")
                    .document(directionId)
                    .collection("tasks")
                    .snapshotFlow()
                    .map { querySnapshot ->
                        querySnapshot.toObjects(Task::class.java)
                    }
            } else {
                Log.d(TAG, "MESSAGE: user is NULL")
                return null
            }
        } catch (e: Exception) {
            Log.w(TAG, "MESSAGE: Error getting documents: ", e)
            return null
        }
    }

    override suspend fun setNotificationId(
        taskId: String, directionId: String, start: Boolean, id: Int
    ) {
        val localUser = user
        if (localUser != null) {

            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .collection("tasks")
                .document(taskId)

            if (start) {
                customCollectionPath
                    .update(
                        "notificationStartId", id
                    )
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "notificationStartId successfully updated! $id"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            } else {
                customCollectionPath
                    .update(
                        "notificationEndId", id
                    )
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "notificationEndId successfully updated! $id"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }


        }
    }

    override suspend fun cancelNotification(
        taskId: String,
        directionId: String,
        start: Boolean
    ) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .collection("tasks")
                .document(taskId)

            if (start){
                customCollectionPath
                    .update(
                        "notificationStartId", 0
                    )
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "notificationStartId successfully updated!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }
            else {customCollectionPath
                .update(
                    "notificationEndId", 0
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "notificationEndId successfully updated!"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

            }


        }
    }

    override suspend fun isStartNotificationActiveChange(
        taskId: String,
        directionId: String,
        active: Boolean
    ) {
        val localUser = user
        if (localUser != null) {

            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .collection("tasks")
                .document(taskId)

            customCollectionPath
                .update(
                    "startNotificationActive", active
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "StartNotificationActive successfully updated!"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }

    override suspend fun collectTaskData(directions: List<Direction>): List<Task> {
        val collectedTask: MutableList<Task> = mutableListOf()
        val localUser = user
        return if (localUser != null) {
            for (direction in directions) {
                Log.d(TAG, "MESSAGE: collection ${direction.uid}")
                val singleDirectionTasks =
                    db.collection("users")
                        .document(localUser.uid)
                        .collection("directions")
                        .document(direction.uid)
                        .collection("tasks")
                        .get().await().toObjects(Task::class.java)
                collectedTask += singleDirectionTasks
            }
            Log.d(TAG, "MESSAGE: Returning collected Tasks")
            collectedTask
        } else {
            mutableListOf()
        }
    }

    override suspend fun getOtherUserDirection(userID: String): Flow<List<Direction>>? {
        val localUser = user
        return if (localUser != null) {
            db.collection("users")
                .document(userID)
                .collection("directions")
                .snapshotFlow()
                .map { querySnapshot ->
                    querySnapshot.toObjects(Direction::class.java)
                }
        } else {
            null
        }

    }

    override suspend fun setDirectionShareMode(share: Boolean, directionId: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)

            customCollectionPath
                .update(
                    "share", share
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "Direction share status successfully updated!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Error updating direction share status",
                        e
                    )
                }
        }
    }

    override suspend fun setTaskDateStart(
        taskId: String, directionId: String, time: Timestamp, isStart: Boolean
    ) {
        val localUser = user
        if (localUser != null) {

            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .collection("tasks")
                .document(taskId)

            if (isStart) {
                customCollectionPath
                    .update(
                        "timeStart", time
                    )
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully updated! $time"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            } else {
                customCollectionPath
                    .update(
                        "timeEnd", time
                    )
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully updated! $time"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }


        }
    }

    override suspend fun setDirectionStatus(isDone: Boolean, directionId: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)

            customCollectionPath
                .update(
                    "done", isDone
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully updated! $isDone"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }

    }

    override suspend fun setDirectionDescription(text: RichTextState, directionId: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)

            customCollectionPath
                .update(
                    "description", text.toHtml()
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully updated! $text"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }

    override suspend fun setTaskDescription(
        text: RichTextState,
        directionId: String,
        taskId: String
    ) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .collection("tasks")
                .document(taskId)

            customCollectionPath
                .update(
                    "description", text.toHtml()
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully updated! $text"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }

    }

    override suspend fun setTaskTitle(directionId: String, taskId: String, text: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
            customCollectionPath
                .collection("tasks")
                .document(taskId)
                .update(
                    "title", text
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully updated!"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }

    override suspend fun setDirectionTitle(directionId: String, text: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
            customCollectionPath
                .update(
                    "title", text
                )
                .addOnSuccessListener {
                    Log.d(
                        TAG,
                        "Direction title successfully updated!"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating direction title", e) }
        }
    }

    override suspend fun setTaskCompletionStatus(
        status: Boolean,
        uID: String,
        directionId: String,
        directionProgress: Int
    ) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)

            customCollectionPath
                .collection("tasks")
                .document(uID)
                .update(
                    "completed", status
                )
                .addOnSuccessListener {
                    if (status) {
                        customCollectionPath.update(
                            "progress", directionProgress + 1
                        )
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully updated! $directionProgress"
                        )
                    } else {
                        customCollectionPath.update(
                            "progress", directionProgress - 1
                        )
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully updated! $directionProgress"
                        )
                    }
                    Log.d(
                        TAG,
                        "DocumentSnapshot successfully updated! $status"
                    )
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }

    }

    override suspend fun addOtherUserDirection(
        userID: String,
        directionId: String,
        tasks: List<Task>
    ) {
        val localUser = user
        if (localUser != null) {
            val sourceCollectionPath = db.collection("users")
                .document(userID)
                .collection("directions")

            val collectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")

            val newDirectionId = collectionPath.document().id

            var direction = sourceCollectionPath.document(directionId)
                .get().await().toObject(Direction::class.java)

            if (direction != null) {
                direction.uid = newDirectionId

                collectionPath.document(newDirectionId).set(direction).addOnSuccessListener {
                    Log.d(TAG, "OtherUser collection added!")
                    tasks.forEachIndexed { index, task ->
                        val newTaskId = collectionPath.document(newDirectionId)
                            .collection("tasks")
                            .document().id

                        task.uid = newTaskId

                        val document = collectionPath
                            .document(newDirectionId)
                            .collection("tasks")
                            .document(newTaskId)
                        try {
                            document.set(task)
                        } catch (e: Exception) {
                            Log.w(TAG, "error adding tasks $index", e)
                        }
                    }
                }
                    .addOnFailureListener {
                        Log.w(TAG, "OtherUser collection add failed", it)
                    }
                    .await()
            }
        } else {
            null
        }


    }

//        val userCollection: MutableList<Task> = mutableListOf()
//        try {
//            val collectionSnapshot = getTasksFromFirestore(directionId)
//            if (collectionSnapshot != null) {
//                for (document in collectionSnapshot) {
//                    userCollection.add(document.toObject(Task::class.java))
//                    Log.d(TAG, "MESSAGE: ATTENTION $userCollection")
//                }
//            } else {
//                Log.d(TAG, "MESSAGE:  collection is empty")
//            }
//            return TaskGetState.Success(userCollection)
//
//        } catch (e: Exception) {
//            Log.w(TAG, "MESSAGE: Error getting documents: ", e)
//            return TaskGetState.Error
//        }


//    override suspend fun getDirectionsList(): List<Direction> {
//        val userCollection: MutableList<Direction> = mutableListOf()
//        try {
//            val collectionSnapshot = getDirectionsFromFirestore()
//            if (collectionSnapshot != null) {
//                for (document in collectionSnapshot) {
//                    userCollection.add(document.toObject(Direction::class.java))
//                    Log.d(TAG, "MESSAGE: ATTENTION $userCollection")
//                }
//            } else {
//                Log.d(TAG, "MESSAGE:  collection is empty")
//            }
//
//        } catch (e: Exception) {
//            Log.w(TAG, "MESSAGE: Error getting documents: ", e)
//        }
//        return userCollection.toList()
//
//    }

    override fun getUser(): FirebaseUser? {
        return user
    }

    override suspend fun deleteTask(direction: Direction, task: Task) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(direction.uid)
            customCollectionPath
                .collection("tasks")
                .document(task.uid)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Document successfully deleted")
                    customCollectionPath.update(
                        "count", direction.count - 1
                    )
                        .addOnSuccessListener {
                            Log.d(
                                TAG,
                                "Direction count successfully updated!"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                TAG,
                                "Error updating direction count",
                                e
                            )
                        }
                    if (task.completed) {
                        customCollectionPath.update(
                            "progress", direction.progress - 1
                        ).addOnSuccessListener {
                            Log.d(
                                TAG,
                                "Direction progress successfully updated!"
                            )
                        }
                            .addOnFailureListener { e ->
                                Log.w(
                                    TAG,
                                    "Error updating direction progress",
                                    e
                                )
                            }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error deleting document", it)
                }
        }
    }

    override suspend fun deleteDirection(directionId: String) {
        val localUser = user
        if (localUser != null) {
            val customCollectionPath = db.collection("users")
                .document(localUser.uid)
                .collection("directions")

            val tasksSnapshot = customCollectionPath.document(directionId)
                .collection("tasks")
                .get().await()

            for (task in tasksSnapshot) {
                val taskId = task.id
                val taskReference = customCollectionPath.document(directionId)
                    .collection("tasks")
                    .document(taskId)
                taskReference.delete().addOnSuccessListener {
                    Log.d(TAG, "Document successfully deleted")
                }
                    .addOnFailureListener {
                        Log.e(TAG, "Error deleting document", it)
                    }
                    .await()
            }

            customCollectionPath.document(directionId)
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Document successfully deleted")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error deleting document", it)
                }
                .await()
        }
    }


    override suspend fun getCurrentDirection(directionId: String): Flow<Direction>? {
        val localUser = user
        return if (localUser != null) {
            return db.collection("users")
                .document(localUser.uid)
                .collection("directions")
                .document(directionId)
                .snapshotFlow()
        } else {
            return null
        }

    }

    override suspend fun anonSignInCheck(): LogInState {
        return try {
            auth.signInAnonymously().await()
            if (user == null) {
                LogInState.Error
            } else {
                LogInState.Success
            }
        } catch (e: Exception) {
            LogInState.Error
        }

    }

//    private suspend fun getTasksFromFirestore(directionId: String): QuerySnapshot? {
//        return if (user != null) {
//            val collectionReference = db.collection("users")
//                .document(user.uid)
//                .collection("directions")
//                .document(directionId)
//                .collection("tasks")
//            collectionReference.get().await()
//        } else {
//            null
//        }
//    }

    override fun signOut() {
        auth.signOut()
    }


}