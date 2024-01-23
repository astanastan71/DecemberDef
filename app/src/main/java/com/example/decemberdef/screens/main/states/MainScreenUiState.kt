package com.example.decemberdef.screens.main.states

import com.google.firebase.auth.FirebaseUser

data class MainScreenUiState(
    val isAuthorised: Boolean = true,
    val loginPasswordState: LoginPasswordState = LoginPasswordState()
)

data class LoginPasswordState
    (
    val login: String = "",
    val password: String = ""
)


