package com.example.decemberdef.ui.screens.testScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun changeScreen(){
    val viewModel: TestScreenViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Row() {
            Button(onClick = { viewModel.changeText() }) {
                Text(text = "change")
            }


        }
    }

}