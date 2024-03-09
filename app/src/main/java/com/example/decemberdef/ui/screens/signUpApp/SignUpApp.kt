package com.example.decemberdef.ui.screens.signUpApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decemberdef.ui.screens.signUpApp.components.SignUpBox

@Composable
fun SignUpApp(
    viewModel: SignUpViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val signUpAppState = viewModel.uiState.collectAsState().value
    when (viewModel.signUpState) {
        is SignUpState.Success -> {
            viewModel.reset()
        }

        else -> {
            SignUpBox(
                onTextChangeLog = {
                    viewModel.updateEmailTextState(
                        it,
                        signUpAppState.emailPasswordState.password,
                        signUpAppState.emailPasswordState.verPassword
                    )
                },
                valueVerificationPass = signUpAppState.emailPasswordState.verPassword,
                valueLog = signUpAppState.emailPasswordState.email,
                onTextChangePass = {
                    viewModel.updateEmailTextState(
                        signUpAppState.emailPasswordState.email,
                        it,
                        signUpAppState.emailPasswordState.verPassword
                    )
                },
                valuePass = signUpAppState.emailPasswordState.password,
                onClickSignUp = {
                    viewModel.signUp(
                        signUpAppState.emailPasswordState.email,
                        signUpAppState.emailPasswordState.password
                    )


                },
                onPasswordVerification = {
                    viewModel.updateEmailTextState(
                        signUpAppState.emailPasswordState.email,
                        signUpAppState.emailPasswordState.password,
                        it
                    )
                    viewModel.isPasswordRepeated(it)
                },
                isValidPassword =
                signUpAppState.isPasswordValid,
                modifier = modifier
            )
        }
    }


}