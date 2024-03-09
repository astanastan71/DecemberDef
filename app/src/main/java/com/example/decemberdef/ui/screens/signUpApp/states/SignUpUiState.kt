package com.example.decemberdef.ui.screens.signUpApp.states

data class SignUpUiState(
    val emailPasswordState: EmailPasswordState = EmailPasswordState(),
    val isPasswordValid: Boolean = false
)

data class EmailPasswordState(
    val email: String = "",
    val password: String = "",
    val verPassword: String = ""
)