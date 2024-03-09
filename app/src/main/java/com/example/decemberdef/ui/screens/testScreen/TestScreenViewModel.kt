package com.example.decemberdef.ui.screens.testScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decemberdef.ui.screens.authScreen.states.TestUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    fun changeText(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    text = 23
                )
            }
        }
    }
}