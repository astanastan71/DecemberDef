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
    suspend fun addCustomTask(text: RichTextState)
    suspend fun addCustomDirection()
    fun getDirectionsList(): Flow<List<Direction>>?
    fun getUser(): FirebaseUser?
    suspend fun anonSignInCheck(): LogInState
    suspend fun getDirectionTasks(directionId: String): TaskGetState
    suspend fun setTaskDateStart(
        taskId: String,
        directionId: String,
        time: Timestamp,
        isStart: Boolean
    )
    suspend fun setDirectionStatus(isDone: Boolean, directionId: String)
}