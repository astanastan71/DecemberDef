package com.example.decemberdef.ui.screens.signInApp

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cmv3.screens.Auth.LoginBox
import com.example.cmv3.screens.Auth.ToSignBox
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun SignInApp(
    viewModel: SignInViewModel = viewModel(factory = SignInViewModel.Factory),
    onSignUpButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val signInAppState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current as Activity
    when (viewModel.logInState) {
        is LogInState.Success -> {
            context.recreate()
            viewModel.reset()
        }

        else -> {
            Box(modifier = modifier) {
                LoginBox(
                    onClickSignIn = {
                        viewModel.signIn(
                            signInAppState.loginPasswordState.login,
                            signInAppState.loginPasswordState.password
                        )


                    },
                    valueLog = signInAppState.loginPasswordState.login,
                    valuePass = signInAppState.loginPasswordState.password,
                    onTextChangeLog = {
                        viewModel.updateLoginTextState(
                            it,
                            signInAppState.loginPasswordState.password
                        )
                    },
                    onTextChangePass = {
                        viewModel.updateLoginTextState(
                            signInAppState.loginPasswordState.login,
                            it
                        )
                    },
                    onClickAnonSign = {
                        viewModel.anonSign()
                    },

                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxWidth()
                )
                ToSignBox(onSignUpButtonClicked)


            }
        }
    }


}

@Preview
@Composable
fun MainScreenPreview() {
    DecemberDefTheme {
        Surface() {
            SignInApp(
                onSignUpButtonClicked = {}
            )
        }
    }
}