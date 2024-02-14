package com.example.decemberdef.ui.screens.signInApp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decemberdef.ui.screens.authScreen.states.AuthUiState
import com.example.decemberdef.ui.screens.signInApp.states.LoginPasswordState
import com.example.decemberdef.ui.screens.signInApp.states.SignInUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception


sealed interface LogInState {
    object Success : LogInState
    object Error : LogInState
    object Loading : LogInState
}

class SignInViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    var logInState: LogInState by mutableStateOf(LogInState.Loading)
        private set

    init {
    }

    fun changeApp() {
        val docRef = db.collection("cities").document("SF")

    }

    fun createData(){

    }



    fun anonSign(){
        viewModelScope.launch {
            auth.signInAnonymously().addOnCompleteListener { task ->
                logInState = if (task.isSuccessful) {
                    LogInState.Success

                } else {
                    LogInState.Error

                }
                if (task.isCanceled) {
                    Log.e(TAG, task.exception.toString())
                }
            }
        }
    }

    fun reset(){
        logInState=LogInState.Loading
    }

    fun signIn(login: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                logInState = if (task.isSuccessful) {
                    LogInState.Success
                } else {
                    LogInState.Error

                }
                if (task.isCanceled) {
                    Log.e(TAG, task.exception.toString())
                }
            }

        }
    }


    fun updateLoginTextState(newValueLogin: String, newValuePassword: String) {
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