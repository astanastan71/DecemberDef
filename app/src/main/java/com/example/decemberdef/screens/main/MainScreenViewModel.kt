package com.example.decemberdef.screens.main

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decemberdef.screens.main.states.LoginPasswordState
import com.example.decemberdef.screens.main.states.MainScreenUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface LogInState {
    data class Success(val user: FirebaseUser) : LogInState
    object Error : LogInState
    object Loading : LogInState
}

class MainScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()
    private var auth: FirebaseAuth = Firebase.auth

    var logInState: LogInState by mutableStateOf(LogInState.Loading)
        private set

    init {
        val user = auth.currentUser
        if (user != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    isAuthorised = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isAuthorised = false
                )
            }
        }
    }

    fun changeApp() {

    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                logInState = if (task.isSuccessful) {
                    auth.currentUser?.let { LogInState.Success(it) }!!
                } else {
                    LogInState.Error

                }
            }
        }
    }


    fun updateLoginTextState(newValueLogin: String, newValuePassword: String) {
        val loginPasswordState =
            _uiState.update { currentState ->
                currentState.copy(
                    loginPasswordState = LoginPasswordState(
                        newValueLogin,
                        newValuePassword
                    )
                )
            }
    }


}