package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.decemberdef.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarMainScreen(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(top = 14.dp, bottom = 14.dp)
        )
    },
        modifier = modifier)
}