package com.example.decemberdef.screens.signUpApp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cmv3.screens.Auth.textEdit
import com.example.decemberdef.R
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun SignUpBox(
    onTextChangeLog: (String) -> Unit,
    valueLog: String,
    onTextChangePass: (String) -> Unit,
    valuePass: String,
    valueVerificationPass: String,
    onClick: () -> Unit,
    isValidPassword: Boolean,
    onPasswordVerification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column() {
        Box(
            modifier = modifier

        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, start = 15.dp, end = 15.dp)
                ) {
                    textEdit(
                        label = R.string.email,
                        onTextChange = onTextChangeLog,
                        value = valueLog
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                ) {
                    textEdit(
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = R.string.passwordGen,
                        onTextChange = onTextChangePass,
                        value = valuePass
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                ) {
                    textEdit(
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = R.string.passwordRepeat,
                        onTextChange = onPasswordVerification,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Предупреждение, пароли не совпадают"
                            )
                        },
                        icon2 = {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Пароли совпадают"
                            )
                        },
                        isValid = isValidPassword,
                        value = valueVerificationPass
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = onClick,
                        enabled = isValidPassword
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_up_confirm)
                        )

                    }
                }

            }

        }
    }


}

@Preview
@Composable
fun SignUpBoxPreview() {
    DecemberDefTheme() {
        Surface() {
            SignUpBox(
                onClick = { },
                valueLog = "loginTextState",
                valuePass = "passwordTextState",
                onTextChangeLog = {
                },
                onTextChangePass = {
                },
                onPasswordVerification = {},
                isValidPassword = true,
                valueVerificationPass = ""
            )
        }

    }

}
