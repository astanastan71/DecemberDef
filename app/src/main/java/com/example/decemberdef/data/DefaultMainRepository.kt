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
    private val user: FirebaseUser?,
) : MainRepository {

    override suspend fun getUserData(): User {
        return if (user != null) {
            User(
                userID = user.uid,
                isEmailVerified = user.isEmailVerified,
                isAnon = user.isAnonymous,
                userName = user.displayName,
                userEmail = user.email,
                userPhoto = user.photoUrl
            )
        } else User(
            userID = "Not Found"
        )
    }

    override suspend fun addCustomTaskAndDirection(textState: RichTextState) {
        if (user != null) {
            val customCollectionPath = db.collection("users")
                .document(user.uid)
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
            if (user != null) {
                val customCollectionPath = db.collection("users")
                    .document(user.uid)
                    .collection("directions")
                val customCollectionId = customCollectionPath
                    .document(direction.uid)
                    .collection("tasks")
                    .document().id
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
                            "count", direction.count+1
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
            if (user != null) {
                val customCollectionPath = db.collection("users")
                    .document(user.uid)
                    .collection("directions")
                val customCollectionId = customCollectionPath.document().id
                customCollectionPath
                    .document(customCollectionId)
                    .set(
                        Direction(
                            uid = customCollectionId,
                            progress = 0
                        )
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
        return if (user != null) {
            val collectionReference = db.collection("users")
                .document(user.uid)
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
        return if (user != null) {
            db.collection("users")
                .document(user.uid)
                .collection("directions")
                .snapshotFlow()
                .map { querySnapshot ->
                    querySnapshot.toObjects(Direction::class.java)
                }
        } else {
            null
        }
    }


    override suspend fun getDirectionTasks(directionId: String): TaskGetState {
        try {
            return if (user != null) {
                Log.d(TAG, "MESSAGE: User is not NULL")
                TaskGetState.Success(
                    db.collection("users")
                        .document(user.uid)
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
            return if (user != null) {
                Log.d(TAG, "MESSAGE: User is not NULL")
                db.collection("users")
                    .document(user.uid)
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

    override suspend fun collectTaskData(directions: List<Direction>): List<Task> {
        val collectedTask: MutableList<Task> = mutableListOf()
        return if (user != null) {
            for (direction in directions) {
                Log.d(TAG, "MESSAGE: collection ${direction.uid}")
                val singleDirectionTasks =
                    db.collection("users")
                        .document(user.uid)
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
        return if (user != null) {
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

    override suspend fun setTaskDateStart(
        taskId: String, directionId: String, time: Timestamp, isStart: Boolean
    ) {
        if (user != null) {

            val customCollectionPath = db.collection("users")
                .document(user.uid)
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
        if (user != null) {
            val customCollectionPath = db.collection("users")
                .document(user.uid)
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
        if (user != null) {
            val customCollectionPath = db.collection("users")
                .document(user.uid)
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
        if (user != null) {
            val customCollectionPath = db.collection("users")
                .document(user.uid)
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

    override suspend fun setTaskCompletionStatus(
        status: Boolean,
        uID: String,
        directionId: String,
        directionProgress: Int
    ) {
        if (user != null) {
            val customCollectionPath = db.collection("users")
                .document(user.uid)
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

    override suspend fun getCurrentDirection(directionId: String): Flow<Direction>? {
        return if (user != null) {
            return db.collection("users")
                .document(user.uid)
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