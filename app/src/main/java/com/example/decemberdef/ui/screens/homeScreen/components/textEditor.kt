package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.AlignHorizontalLeft
import androidx.compose.material.icons.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.decemberdef.R
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.theme.DecemberDefTheme
import com.example.decemberdef.ui.theme.roboto
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun taskEditor(
    onTaskClick: (RichTextState) -> Unit = {},
    onOldTaskChange: (Task, RichTextState) -> Unit = { _, _ -> },
    task: Task = Task(),
    isNewTask: Boolean = true,
    taskEditorState: RichTextState = rememberRichTextState(),
    modifier: Modifier = Modifier.fillMaxSize()
) {
    LaunchedEffect(Unit) {
        taskEditorState.setHtml(task.description)
    }
    val controller = rememberColorPickerController()
    var paragraphStyle by remember { mutableStateOf(ParagraphStyle(textAlign = TextAlign.Start)) }
    var boldSelected by rememberSaveable { mutableStateOf(false) }
    var underlinedSelected by rememberSaveable { mutableStateOf(false) }
    var colorPickerState by remember { mutableStateOf(false) }
    var linkAddState by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var yourColor by remember { mutableStateOf(Color.Black) }
    taskEditorState.toggleParagraphStyle(
        paragraphStyle
    )
    if (linkAddState) {
        linkAddDialog(
            text = text,
            link = link,
            onValueChangeText = {
                text = it
            },
            onValueChangeLink = {
                link = it
            },
            onDismissRequest = { linkAddState = false },
            onConfirmation = {
                taskEditorState.addLink(text, link)
                linkAddState = false
            }
        )
    }

    if (colorPickerState) {
        colorPickerDialog(
            onColorChanged = {
                yourColor = it.color
            },
            controller = controller,
            applyYourColor = {
                taskEditorState.toggleSpanStyle(
                    SpanStyle(
                        color = yourColor
                    )
                )
                colorPickerState = false
            },
            resetColor = {
                taskEditorState.toggleSpanStyle(
                    SpanStyle(
                        color = Color.Black
                    )
                )
                colorPickerState = false
            },
            dismissRequest = {
                colorPickerState = false
            }
        )
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            IconButton(onClick = {
                when (paragraphStyle) {
                    ParagraphStyle(textAlign = TextAlign.Start) -> {
                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center)
                    }

                    ParagraphStyle(textAlign = TextAlign.Center) -> {
                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.End)
                    }

                    ParagraphStyle(textAlign = TextAlign.End) -> {
                        paragraphStyle = ParagraphStyle(textAlign = TextAlign.Start)
                    }
                }
            }
            ) {
                Icon(
                    imageVector =
                    when (paragraphStyle) {
                        ParagraphStyle(textAlign = TextAlign.Start) -> Icons.Default.AlignHorizontalLeft
                        ParagraphStyle(textAlign = TextAlign.Center) ->
                            Icons.Default.AlignHorizontalCenter

                        ParagraphStyle(textAlign = TextAlign.End) ->
                            Icons.Default.AlignHorizontalRight

                        else -> {
                            Icons.Default.Face
                        }
                    },
                    contentDescription = stringResource(R.string.Bold),
                    modifier = Modifier.padding(5.dp)
                )
            }
            IconButton(onClick = {
                taskEditorState.toggleSpanStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                boldSelected = !boldSelected
            }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatBold,
                    contentDescription = stringResource(R.string.Bold),
                    tint = if (!boldSelected) {
                        Color.Gray
                    } else {
                        Color.Black
                    },
                    modifier = Modifier.padding(5.dp)
                )
            }
            IconButton(onClick = {
                taskEditorState.toggleSpanStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.Underline
                    )
                )
                underlinedSelected = !underlinedSelected
            }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatUnderlined,
                    contentDescription = stringResource(R.string.underlined),
                    tint = if (!underlinedSelected) {
                        Color.Gray
                    } else {
                        Color.Black
                    },
                    modifier = Modifier.padding(5.dp)
                )
            }
            IconButton(onClick = {
                colorPickerState = true
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Colorize,
                    contentDescription = stringResource(R.string.apply_your_color),
                    modifier = Modifier.padding(5.dp)
                )
            }
            IconButton(onClick = {
                linkAddState = true
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = stringResource(R.string.link_add),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = {
                    if (isNewTask) {
                        onTaskClick(taskEditorState)
                    }
                    else {
                        onOldTaskChange(task, taskEditorState)
                    }

                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.add),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextEditor(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                    },
                state = taskEditorState,
                readOnly = false
            )
        }
    }
}

@Composable
fun colorPickerDialog(
    onColorChanged: (ColorEnvelope) -> Unit,
    controller: ColorPickerController,
    applyYourColor: () -> Unit,
    resetColor: () -> Unit,
    dismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { dismissRequest() }) {
        Card() {
            Column() {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = {
                        onColorChanged(it)
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { resetColor() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Сбросить")
                    }
                    TextButton(
                        onClick = { applyYourColor() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Подтвердить")
                    }
                }

            }
        }


    }
}

@Composable
fun linkAddDialog(
    text: String,
    link: String,
    onValueChangeText: (String) -> Unit,
    onValueChangeLink: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card() {
            Column() {
                Row() {
                    Text(
                        text = "Текст:",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )
                    TextField(
                        value = text,
                        onValueChange = {
                            onValueChangeText(it)
                        }
                    )
                }
                Row() {
                    Text(
                        text = "Ссылка:",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )
                    TextField(
                        value = link,
                        onValueChange = {
                            onValueChangeLink(it)
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Подтвердить")
                    }
                }
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