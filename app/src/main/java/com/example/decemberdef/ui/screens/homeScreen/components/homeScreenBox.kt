package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun homeScreenBox(
    onClickStart: () -> Unit,
    userName: String
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Добро пожаловать:")
            Text(text = userName)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            startButton(
                onClickStart = onClickStart
            )
        }


    }


}

@Composable
@Preview
fun mainScreenBoxPreview() {
    DecemberDefTheme() {
        Surface {
            homeScreenBox(
                onClickStart = {},
                userName = "not found"
            )
        }

    }

}