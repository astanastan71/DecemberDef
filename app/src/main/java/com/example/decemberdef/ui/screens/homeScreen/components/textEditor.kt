package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.decemberdef.ui.theme.DecemberDefTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun taskEditor(
    onTaskClick: () -> Unit,
    taskEditorState: RichTextState,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextEditor(
                state = taskEditorState
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onTaskClick) {
                Text(text = "Внести")

            }

        }
    }


}

@Preview
@Composable
fun previewTaskEditor() {
    DecemberDefTheme() {
        Surface() {
            taskEditor(
                taskEditorState = rememberRichTextState(),
                onTaskClick = {}
            )
        }

    }

}