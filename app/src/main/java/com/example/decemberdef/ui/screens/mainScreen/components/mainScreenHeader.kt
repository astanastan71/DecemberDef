package com.example.decemberdef.ui.screens.mainScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decemberdef.R
import com.example.decemberdef.ui.theme.DecemberDefTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarMainScreen(
    isAnon: Boolean,
    onAnonRegisterClick: () -> Unit,
    onLogOutClick: () -> Unit,
    changeDialogStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Row(){
                Image(
                    modifier = Modifier
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.main_icon),
                    contentDescription = "Student App"
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .padding(top = 14.dp, bottom = 14.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }

        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = {
                    expanded = !expanded
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.options),
                    contentDescription = "Опции",
                    modifier = Modifier.size(25.dp)
                )
            }
            DropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false }) {
                Text(
                    text = stringResource(id = R.string.changeUserInfo),
                    modifier = Modifier.clickable(onClick = {
                        changeDialogStatus()
                        expanded = false
                    }),
                    style = MaterialTheme.typography.displaySmall
                )
                if (isAnon) {
                    Text(
                        text = stringResource(id = R.string.register),
                        modifier = Modifier.clickable(onClick = {
                            onAnonRegisterClick()
                            expanded = false
                        }),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                Text(
                    text = stringResource(id = R.string.log_out),
                    modifier = Modifier.clickable(
                        onClick = onLogOutClick
                    ),
                    style = MaterialTheme.typography.displaySmall
                )

            }
        }
    )
}

@Preview
@Composable
fun HeaderPreview() {
    DecemberDefTheme {
        Surface() {
            topAppBarMainScreen(
                true,
                onLogOutClick = {},
                changeDialogStatus = {},
                onAnonRegisterClick = {}
            )
        }
    }
}