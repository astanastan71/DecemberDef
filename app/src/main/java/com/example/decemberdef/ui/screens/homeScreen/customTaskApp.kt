package com.example.decemberdef.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.decemberdef.ui.screens.homeScreen.components.taskEditor
import com.mohamedrejeb.richeditor.model.RichTextState


@Composable
fun customTaskStart(
    taskEditorState: RichTextState,
    taskAdd: ()->Unit,
    modifier: Modifier = Modifier
) {
    taskEditorState
        .toggleSpanStyle(
            SpanStyle(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )
        )
    Box(modifier = modifier) {
        taskEditor(
            taskEditorState = taskEditorState,
            onTaskClick = taskAdd
        )
    }


}