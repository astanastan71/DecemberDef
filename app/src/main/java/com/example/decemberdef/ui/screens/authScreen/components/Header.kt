package com.example.decemberdef.ui.screens.authScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decemberdef.R
import com.example.decemberdef.ui.theme.DecemberDefTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarAuthScreen(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(top = 14.dp, bottom = 14.dp)
        )
    },
    modifier = modifier)
}

@Preview
@Composable
fun HeaderPreview() {
    DecemberDefTheme {
        Surface() {
            topAppBarAuthScreen()
        }


    }
}