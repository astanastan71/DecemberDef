package com.example.decemberdef.ui.screens.authScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.decemberdef.MainApplication
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.ui.screens.authScreen.states.AuthUiState
import com.example.decemberdef.ui.screens.homeScreen.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    var auth: FirebaseAuth = Firebase.auth


    private fun authorizationCheck() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserNull = mainRepository.getUser() == null
                )
            }
        }
    }

    fun changeUserState(userNull: Boolean) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserNull = userNull
                )
            }
        }

    }

    init {
        authorizationCheck()

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                AuthScreenViewModel(mainRepository = mainRepository)
            }
        }
    }
}