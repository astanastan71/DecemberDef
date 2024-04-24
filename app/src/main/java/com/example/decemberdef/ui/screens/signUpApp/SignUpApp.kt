package com.example.decemberdef.ui.screens.signUpApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decemberdef.ui.screens.signUpApp.components.SignUpBox
import com.example.decemberdef.ui.theme.roboto

@Composable
fun SignUpApp(
    viewModel: SignUpViewModel = viewModel(factory = SignUpViewModel.Factory),
    isAnonRegister: Boolean = false,
    goBackandUpdate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val signUpAppState = viewModel.uiState.collectAsState().value
    when (viewModel.signUpState) {
        is SignUpState.Success -> {
            viewModel.reset()
        }

        else -> {
            Column() {
                Row() {
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
                            if (!isAnonRegister) {
                                viewModel.signUp(
                                    signUpAppState.emailPasswordState.email,
                                    signUpAppState.emailPasswordState.password
                                )
                            } else {
                                viewModel.anonSignUp(
                                    signUpAppState.emailPasswordState.email,
                                    signUpAppState.emailPasswordState.password
                                ) {
                                    goBackandUpdate()
                                }
                            }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (viewModel.signUpState == SignUpState.Error ||
                        viewModel.anonSignUpState == AnonSignUpState.Error
                    ) {
                        Text(
                            text = "Неверный пароль или адрес почты!",
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontFamily = roboto,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.Red
                            ),
                        )
                    }
                }

            }


        }
    }


}