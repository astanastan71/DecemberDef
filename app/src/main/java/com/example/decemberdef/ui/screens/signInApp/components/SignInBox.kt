package com.example.cmv3.screens.Auth

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decemberdef.R
import com.example.decemberdef.ui.theme.DecemberDefTheme
import com.example.decemberdef.ui.theme.roboto

@Composable
fun LoginBox(
    onTextChangeLog: (String) -> Unit,
    valueLog: String,
    onTextChangePass: (String) -> Unit,
    valuePass: String,
    onClickSignIn: () -> Unit,
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
                        text = stringResource(id = R.string.get_started),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
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
                        label = R.string.password,
                        onTextChange = onTextChangePass,
                        value = valuePass
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = onClickSignIn
                    ) {
                        Text(
                            text = stringResource(id = R.string.log_in),
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                fontFamily = roboto,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            ),
                        )

                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                }

            }

        }
    }

}

@Composable
fun ToSignBox(
    onSignUpButtonClicked: () -> Unit,
    onClickAnonSign: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 30.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.Log_in_text),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                ),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = onClickAnonSign
            ) {
                Text(
                    text = stringResource(id = R.string.anon_sign),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                    ),
                )

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onSignUpButtonClicked
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                    ),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textEdit(
    @StringRes label: Int,
    value: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    icon: @Composable (() -> Unit) = {},
    icon2: @Composable (() -> Unit) = {},
    isValid: Boolean = false
) {
    TextField(
        trailingIcon = if (!isValid) icon
        else icon2,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        value = value,
        visualTransformation = visualTransformation,
        onValueChange = onTextChange,
        modifier = modifier
    )
}

@Preview
@Composable
fun LoginAppPreview() {
    DecemberDefTheme() {
        Surface() {
            LoginBox(
                onClickSignIn = { },
                valueLog = "loginTextState",
                valuePass = "passwordTextState",
                onTextChangeLog = {
                },
                onTextChangePass = {
                }


            )
            ToSignBox(
                {},
                {}
            )

        }

    }
}