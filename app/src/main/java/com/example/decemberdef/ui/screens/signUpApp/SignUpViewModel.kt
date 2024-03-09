package com.example.decemberdef.ui.screens.signUpApp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decemberdef.ui.screens.signInApp.LogInState
import com.example.decemberdef.ui.screens.signInApp.states.LoginPasswordState
import com.example.decemberdef.ui.screens.signInApp.states.SignInUiState
import com.example.decemberdef.ui.screens.signUpApp.states.EmailPasswordState
import com.example.decemberdef.ui.screens.signUpApp.states.SignUpUiState
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

sealed interface SignUpState {
    object Success : SignUpState
    object Error : SignUpState
    object Loading : SignUpState
}

class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()
    private var auth: FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    var signUpState: SignUpState by mutableStateOf(SignUpState.Loading)
        private set

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    signUpState =
                        if (task.isSuccessful) {
                            SignUpState.Success


                        } else {
                            SignUpState.Error
                        }
                    if (task.isCanceled){
                        Log.e(TAG, task.exception.toString())
                    }
                }
        }
    }

    fun reset(){
        signUpState=SignUpState.Loading
    }

    fun updateEmailTextState(
        newValueEmail: String,
        newValuePassword: String,
        newValueVerPassword: String
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                emailPasswordState = EmailPasswordState(
                    newValueEmail,
                    newValuePassword,
                    newValueVerPassword
                )
            )
        }
    }

    fun isPasswordRepeated(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isPasswordValid = password ==
                        _uiState.value.emailPasswordState.password
            )
        }
    }

}