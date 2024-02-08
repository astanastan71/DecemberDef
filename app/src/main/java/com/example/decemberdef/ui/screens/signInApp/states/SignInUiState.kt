package com.example.decemberdef.ui.screens.signInApp.states

data class SignInUiState(
    val loginPasswordState: LoginPasswordState = LoginPasswordState()
)

data class LoginPasswordState
    (
    val login: String = "",
    val password: String = ""
)


