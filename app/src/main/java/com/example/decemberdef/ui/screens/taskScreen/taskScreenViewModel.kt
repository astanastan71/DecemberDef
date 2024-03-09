package com.example.decemberdef.ui.screens.taskScreen

import androidx.lifecycle.ViewModel
import com.example.decemberdef.data.DefaultMainRepository
import com.example.decemberdef.ui.screens.taskScreen.states.TaskAppState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class TaskAppViewModel (private val defaultMainRepository: DefaultMainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow((TaskAppState()))
    val uiState: StateFlow<TaskAppState> = _uiState.asStateFlow()


    init {

    }


}