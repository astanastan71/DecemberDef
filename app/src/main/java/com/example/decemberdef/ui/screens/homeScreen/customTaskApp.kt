package com.example.decemberdef.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.decemberdef.ui.screens.homeScreen.components.taskEditor
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState


@Composable
fun customTaskStart(
    taskEditorState: RichTextState,
    taskAdd: ()->Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        taskEditor(
            taskEditorState = taskEditorState,
            onTaskClick = taskAdd
        )
    }
}

@Preview
@Composable
fun customTaskStartPreview(){
    val taskEditorState = rememberRichTextState()
    customTaskStart(taskEditorState = taskEditorState, taskAdd = { /*TODO*/ })
}