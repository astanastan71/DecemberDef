package com.example.decemberdef.data

import com.example.decemberdef.ui.screens.listApp.TaskGetState
import com.example.decemberdef.ui.screens.signInApp.LogInState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getUserData(): User
    fun signOut()
    suspend fun addCustomTaskAndDirection(text: RichTextState)
    suspend fun addCustomTask(direction: Direction)
    suspend fun addCustomDirection()
    fun getDirectionsList(): Flow<List<Direction>>?
    suspend fun getTasksListFromLink(userID: String, directionId: String): List<Task>
    fun getUser(): FirebaseUser?
    suspend fun deleteTask(direction: Direction, task: Task)
    suspend fun deleteDirection(directionId: String)
    suspend fun getCurrentDirection(directionId: String): Flow<Direction>?
    suspend fun anonSignInCheck(): LogInState
    suspend fun getDirectionTasks(directionId: String): TaskGetState
    suspend fun getDirectionTasksForAll(directionId: String): Flow<MutableList<Task>>?
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
    suspend fun addOtherUserDirection(userID: String, directionId: String, tasks: List<Task>)
    suspend fun setDirectionStatus(isDone: Boolean, directionId: String)
    suspend fun setDirectionDescription(text: RichTextState, directionId: String)
    suspend fun setTaskDescription(text: RichTextState, directionId: String, taskId: String)
    suspend fun setTaskTitle(directionId: String, taskId: String, text: String)
    suspend fun collectTaskData(directions: List<Direction>): List<Task>
    suspend fun getOtherUserDirection(userID: String): Flow<List<Direction>>?
}