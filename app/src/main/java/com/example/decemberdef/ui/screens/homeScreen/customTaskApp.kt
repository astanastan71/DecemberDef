package com.example.decemberdef.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.decemberdef.ui.screens.homeScreen.components.taskEditor
import com.mohamedrejeb.richeditor.model.RichTextState


@Composable
fun customTaskStart(
    taskAdd: (RichTextState)->Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        taskEditor(
            onTaskClick = taskAdd
        )
    }
}

@Preview
@Composable
fun customTaskStartPreview(){
    customTaskStart( taskAdd = { /*TODO*/ })
}