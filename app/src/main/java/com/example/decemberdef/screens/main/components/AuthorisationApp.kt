package com.example.decemberdef.screens.main.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cmv3.screens.Auth.LoginBox
import com.example.cmv3.screens.Auth.ToSignApp
import com.example.decemberdef.ui.theme.DecemberDefTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorisationApp(
    onTextChangeLog: (String) -> Unit,
    valueLog: String,
    onTextChangePass: (String) -> Unit,
    valuePass: String,
    onClickSignIn: () -> Unit,
    isAuthorised: Boolean
) {
    Scaffold(topBar = { topAppBar() }) { it ->
        if (!isAuthorised) {
            LoginBox(
                onClick = onClickSignIn,
                valueLog = valueLog,
                valuePass = valuePass,
                onTextChangeLog = onTextChangeLog,
                onTextChangePass = onTextChangePass,
                modifier = Modifier
                    .padding(it)
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
            )
            ToSignApp()
        }
        else {

        }



    }
}

@Preview
@Composable
fun MainScreenPreview() {
    DecemberDefTheme {
        Surface() {
            AuthorisationApp(
                onClickSignIn = { },
                valueLog = "Логин",
                valuePass = "Пароль",
                onTextChangeLog = {
                },
                onTextChangePass = {
                },
                isAuthorised = false
            )
        }
    }
}