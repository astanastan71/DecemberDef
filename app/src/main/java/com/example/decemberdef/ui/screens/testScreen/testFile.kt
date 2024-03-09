package com.example.decemberdef.ui.screens.testScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun testNumber() {
    val viewModel: TestScreenViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value

    if (uiState.text == null) {
        changeScreen()

    } else {
//        mainScreenBox(userName = uiState.text.toString())
    }


}