package com.example.decemberdef.data

import com.example.decemberdef.ui.screens.listApp.TaskGetState
import com.example.decemberdef.ui.screens.signInApp.LogInState
import com.example.decemberdef.ui.screens.signUpApp.AnonSignUpState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getMonitoredDirectionsList(): MutableList<Direction>
    suspend fun getSingleDirectionForLink(userID: String, directionId: String): Direction
    suspend fun getPermanentAccount(email: String, password: String): AnonSignUpState
    suspend fun userInfoUpdate(userName: String)
    suspend fun deleteMonitoredDirection(linkId:String)
    suspend fun getUserData(): User
    fun signOut()
    fun updateUser(firebaseUser: FirebaseUser?)
    suspend fun addCustomTaskAndDirection(text: RichTextState): String
    suspend fun addCustomTask(direction: Direction)
    suspend fun addCustomDirection()
    fun getDirectionsList(): Flow<List<Direction>>?
    suspend fun getTasksListFromLink(userID: String, directionId: String): List<Task>
    fun getUser(): FirebaseUser?
    suspend fun deleteTask(direction: Direction, task: Task)
    suspend fun deleteDirection(directionId: String)
    suspend fun getCurrentDirection(userID: String, directionId: String): Flow<Direction>?
    suspend fun anonSignInCheck(): LogInState
    suspend fun getDirectionTasks(directionId: String): TaskGetState
    suspend fun getMonitoredDirectionTasks(directionId: String, userID: String): TaskGetState
    suspend fun getDirectionTasksForAll(directionId: String): Flow<MutableList<Task>>?
    suspend fun setNotificationId(taskId: String, directionId: String, start: Boolean, id: Int)
    suspend fun cancelNotification(taskId: String, directionId: String, start: Boolean)
    suspend fun isStartNotificationActiveChange(
        taskId: String,
        directionId: String,
        active: Boolean
    )

    suspend fun setTaskDateStart(
        taskId: String,
        directionId: String,
        time: Timestamp,
        isStart: Boolean
    )

    suspend fun setTaskCompletionStatus(
        status: Boolean,
        uID: String,
        directionId: String,
        directionProgress: Int
    )

    suspend fun monitorOtherUserDirection(userID: String, directionId: String)
    suspend fun addOtherUserDirection(userID: String, directionId: String, tasks: List<Task>)
    suspend fun setDirectionStatus(isDone: Boolean, directionId: String)
    suspend fun setDirectionDescription(text: RichTextState, directionId: String)
    suspend fun setTaskDescription(text: RichTextState, directionId: String, taskId: String)
    suspend fun setTaskTitle(directionId: String, taskId: String, text: String)
    suspend fun setDirectionTitle(directionId: String, text: String)
    suspend fun collectTaskData(directions: List<Direction>): List<Task>
    suspend fun getOtherUserDirection(userID: String): Flow<List<Direction>>?
    suspend fun setDirectionShareMode(share: Boolean, directionId: String)
}