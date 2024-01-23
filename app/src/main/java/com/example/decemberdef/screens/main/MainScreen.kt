package com.example.decemberdef.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decemberdef.screens.main.components.AuthorisationApp
import com.example.decemberdef.screens.main.components.topAppBar
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel()
) {
    val mainScreenState = viewModel.uiState.collectAsState().value

    AuthorisationApp(
        onTextChangeLog = {
            viewModel.updateLoginTextState(
                it,
                mainScreenState.loginPasswordState.password
            )
        },
        valueLog = mainScreenState.loginPasswordState.login,
        onTextChangePass = {
            viewModel.updateLoginTextState(
                mainScreenState.loginPasswordState.login,
                it
            )
        },
        valuePass = mainScreenState.loginPasswordState.password,
        onClickSignIn = {
            viewModel.signIn(
                mainScreenState.loginPasswordState.login,
                mainScreenState.loginPasswordState.password
            )
        },
        isAuthorised = mainScreenState.isAuthorised
    )


}

