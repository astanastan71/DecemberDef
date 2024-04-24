package com.example.decemberdef.ui.screens.signUpApp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.decemberdef.MainApplication
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.ui.screens.signUpApp.states.EmailPasswordState
import com.example.decemberdef.ui.screens.signUpApp.states.SignUpUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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

sealed interface AnonSignUpState {
    object Success : AnonSignUpState
    object Error : AnonSignUpState
    object Loading : AnonSignUpState
}



class SignUpViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()
    private var auth: FirebaseAuth = Firebase.auth

    var signUpState: SignUpState by mutableStateOf(SignUpState.Loading)
        private set

    var anonSignUpState: AnonSignUpState by mutableStateOf(AnonSignUpState.Loading)
        private set

    fun anonSignUp(
        email: String,
        password: String,
        goBackandUpdate: () -> Unit
    ) {
        val credential = EmailAuthProvider.getCredential(email, password)
        try {
            auth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener { task ->
                    anonSignUpState = if (task.isSuccessful) {
                        Log.d(TAG, "linkWithCredential:success")
                        val user = task.result?.user
                        mainRepository.updateUser(user)
                        goBackandUpdate()
                        AnonSignUpState.Success
                    } else {
                        Log.w(TAG, "linkWithCredential:failure", task.exception)
                        AnonSignUpState.Error
                    }
                }
        }
        catch (e:Exception){
            Log.w(TAG, "linkWithCredential:failure", e)
        }

    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        signUpState =
                            if (task.isSuccessful) {
                                SignUpState.Success
                            } else {
                                SignUpState.Error
                            }
                        if (task.isCanceled) {
                            Log.e(TAG, task.exception.toString())
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Error", e)
            }

        }
    }

    fun reset() {
        signUpState = SignUpState.Loading
        anonSignUpState = AnonSignUpState.Loading
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                SignUpViewModel(mainRepository = mainRepository)
            }
        }
    }

}