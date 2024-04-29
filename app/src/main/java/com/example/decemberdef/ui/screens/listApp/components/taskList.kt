package com.example.decemberdef.ui.screens.listApp.components

import android.content.ContentValues.TAG
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.AlignHorizontalLeft
import androidx.compose.material.icons.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.example.decemberdef.R
import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.listApp.DirectionListViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import kotlinx.coroutines.launch

@Composable
fun taskList(
    onDateTimeConfirm: (Long, String, Boolean) -> Unit,
    onCompletionStatusClick: (Boolean, String) -> Unit,
    onTaskDescriptionClick: (RichTextState, String) -> Unit,
    viewModel: DirectionListViewModel,
    tasks: List<Task>,
    onTextExpandClick: (Task) -> Unit = {},
    deleteTask: (Task) -> Unit,
    onTitleChange: (String, String) -> Unit,
    scheduleNotification: (Long, String, String, Boolean, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    cancelNotification: (Int, String, String, Boolean, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
) {
    val taskState = rememberLazyListState()
    var addList: MutableList<Task> = mutableListOf()
    addList.add(
        Task(
            title = stringResource(id = R.string.add),
            imgUrl = "https://cdn-icons-png.flaticon.com/512/7666/7666164.png"
        )
    )

    LazyColumn(contentPadding = PaddingValues(5.dp), state = taskState) {
        items(addList) {
            addTaskItem(
                task = it,
                viewModel = viewModel,
                modifier = Modifier.padding(8.dp)
            )
        }
        itemsIndexed(tasks.reversed()) { index, task ->
            taskItem(
                item = task,
                modifier = Modifier.padding(8.dp),
                onDateTimeConfirm = onDateTimeConfirm,
                onCompletionStatusClick = onCompletionStatusClick,
                onTaskDescriptionClick = onTaskDescriptionClick,
                onTitleChange = onTitleChange,
                deleteTask = deleteTask,
                scheduleNotification = scheduleNotification,
                cancelNotification = cancelNotification,
                taskState = taskState,
                onTextExpandClick = onTextExpandClick,
                index = index
            )
        }
    }
}

@Composable
fun addTaskItem(
    task: Task,
    viewModel: DirectionListViewModel,
    modifier: Modifier = Modifier
) {
    val uiState =
        viewModel.uiState.collectAsState().value.currentDirection?.collectAsState(initial = Direction())
    Card(modifier = modifier.clickable {
        if (uiState != null) {
            viewModel.addCustomTask(uiState.value)
        }
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.add),
                contentDescription = task.title,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun taskItem(
    item: Task,
    readOnly: Boolean = false,
    onDateTimeConfirm: (Long, String, Boolean) -> Unit = { _, _, _ -> },
    onCompletionStatusClick: (Boolean, String) -> Unit = { _, _ -> },
    onTaskDescriptionClick: (RichTextState, String) -> Unit,
    linkItem: Boolean = false,
    onTitleChange: (String, String) -> Unit = { _, _ -> },
    deleteTask: (Task) -> Unit = { _ -> },
    taskState: LazyListState,
    scheduleNotification: (Long, String, String, Boolean, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    cancelNotification: (Int, String, String, Boolean, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    index: Int = 0,
    onTextExpandClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var text by remember { mutableStateOf(item.title) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    val taskEditorState = rememberRichTextState()
    var dateStateStart = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val dateStateEnd = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var isDone by remember { mutableStateOf(item.completed) }
    val openDialog = remember { mutableStateOf(false) }
    var isStartNotificationActive by remember { mutableStateOf(item.startNotificationActive) }
    val milliseconds = item.timeStart.seconds * 1000 + item.timeStart.nanoseconds / 1000000
    var paragraphStyle by remember { mutableStateOf(ParagraphStyle(textAlign = TextAlign.Start)) }
    var boldSelected by rememberSaveable { mutableStateOf(false) }
    var underlinedSelected by rememberSaveable { mutableStateOf(false) }
    if (isStartNotificationActive) {
        if (milliseconds < System.currentTimeMillis()) {
            Log.d(TAG, "Milliseconds lower that current time")
            cancelNotification(
                item.notificationStartId,
                item.title,
                HtmlCompat.fromHtml(
                    item.description,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString(),
                true,
                item.uid,
                false
            )
            isStartNotificationActive = !isStartNotificationActive
        }
    }
    taskEditorState.toggleParagraphStyle(
        paragraphStyle
    )
    if (openDialog.value) {
        titleChangeDialog(
            onConfirmation = { text ->
                onTitleChange(text, item.uid)
                openDialog.value = false
            },
            onDismissRequest = {
                openDialog.value = false
            },
            text = text,
            onValueChange = {
                text = it
            }
        )
    }
    Card(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                    }
            )
            {
                AsyncImage(
                    modifier = Modifier
                        .size(64.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                    model = item.imgUrl,
                    contentDescription = item.title
                )
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        if(!readOnly)
                        openDialog.value = true
                    },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    if (!expanded) {
                        taskEditorState.setHtml(item.description)
                    }
                    expanded = !expanded
                    keyboardController?.hide()
                    coroutineScope.launch {
                        taskState.scrollToItem(index)
                    }
                    if (taskEditorState.toHtml() != item.description) {
                        onTaskDescriptionClick(taskEditorState, item.uid)
                    }
                }) {
                    Icon(
                        imageVector = if (expanded)
                            ImageVector.vectorResource(id = R.drawable.expand_less)
                        else
                            ImageVector.vectorResource(id = R.drawable.expand_more),
                        contentDescription = stringResource(R.string.expand_button_content_description),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        if (expanded) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(4f)) {
                    if (!readOnly){
                        Row(modifier = Modifier.fillMaxSize()) {
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
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RichTextEditor(
                            state = taskEditorState,
                            readOnly = readOnly,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (taskEditorState.toHtml() != item.description) {
                                        onTaskDescriptionClick(taskEditorState, item.uid)
                                    }
                                    keyboardController?.hide()
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusEvent {
                                    if (it.isFocused) {
                                        coroutineScope.launch {
                                            bringIntoViewRequester.bringIntoView()
                                        }
                                    }
                                }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            dateTimeItem(
                                dateState = dateStateStart,
                                dateItem = item.timeStart.toDate().toLocaleString(),
                                item = item,
                                onDateTimeConfirm = onDateTimeConfirm,
                                isStart = true,
                                readOnly = readOnly
                            )
                            if (!readOnly) {
                                IconButton(onClick = {
                                    if (isStartNotificationActive) {
                                        cancelNotification(
                                            item.notificationStartId,
                                            item.title,
                                            HtmlCompat.fromHtml(
                                                item.description,
                                                HtmlCompat.FROM_HTML_MODE_COMPACT
                                            ).toString(),
                                            true,
                                            item.uid,
                                            false
                                        )
                                    } else {
                                        if (milliseconds != null) {
                                            scheduleNotification(
                                                milliseconds,
                                                item.title,
                                                HtmlCompat.fromHtml(
                                                    item.description,
                                                    HtmlCompat.FROM_HTML_MODE_COMPACT
                                                ).toString(),
                                                true,
                                                item.uid,
                                                true
                                            )
                                        }
                                    }
                                    isStartNotificationActive = !isStartNotificationActive
                                }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationAdd,
                                        contentDescription = stringResource(R.string.create_notification),
                                        tint = if (isStartNotificationActive) Color.Black
                                        else Color.Gray,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        dateTimeItem(
                            dateState = dateStateEnd,
                            dateItem = item.timeEnd.toDate().toLocaleString(),
                            item = item,
                            onDateTimeConfirm = onDateTimeConfirm,
                            isStart = false,
                            readOnly = readOnly
                        )

                    }

                }
                if (!readOnly) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                isDone = !isDone
                                onCompletionStatusClick(isDone, item.uid)
                            }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.is_done),
                                    contentDescription = stringResource(R.string.is_done),
                                    tint = if (isDone)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.Gray,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                onTextExpandClick(item)
                            }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.expand),
                                    contentDescription = stringResource(R.string.expand_text),
                                    modifier = Modifier.padding(5.dp)
                                )
                            }

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = {
                                deleteTask(item)
                            }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = stringResource(R.string.delete),
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                        if (linkItem) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(onClick = {
                                }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.add),
                                        contentDescription = stringResource(R.string.add),
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }


                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateTimeItem(
    dateState: DatePickerState,
    dateItem: String,
    item: Task,
    isStart: Boolean,
    readOnly: Boolean,
    onDateTimeConfirm: (Long, String, Boolean) -> Unit
) {
    var expandedDatePicker by remember { mutableStateOf(false) }
    var expandedTimePicker by remember { mutableStateOf(false) }
    Row(modifier = Modifier.clickable {
        if (!readOnly)
        expandedDatePicker = true
    })
    {
        if (expandedDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    expandedDatePicker = false
                    expandedTimePicker = false
                },
                confirmButton = {
                    Button(onClick = {
                        expandedDatePicker = false
                        expandedTimePicker = true
                    }
                    ) {
                        Text(text = "Ok")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        expandedDatePicker = false
                        expandedTimePicker = false
                    }) {
                        Text(text = "Отмена")
                    }
                }
            ) {
                DatePicker(
                    state = dateState
                )
            }

        }
        if (expandedTimePicker) {
            TimePickerDialog(
                onCancel = { expandedTimePicker = false },
                onConfirm = {
                    val calendar = Calendar.getInstance()
                    val dateInMillis = dateState.selectedDateMillis
                    if (dateInMillis != null) {
                        calendar.timeInMillis = dateInMillis
                        calendar.set(Calendar.HOUR, it.time.hours)
                        calendar.set(Calendar.MINUTE, it.time.minutes)
                    } else {
                        calendar.timeInMillis = Calendar.getInstance().timeInMillis
                        calendar.set(Calendar.HOUR, it.time.hours)
                        calendar.set(Calendar.MINUTE, it.time.minutes)
                    }
                    onDateTimeConfirm(calendar.timeInMillis, item.uid, isStart)
                    expandedTimePicker = false
                },
                modifier = Modifier
            )
        } else {
            Text(
                text = dateItem,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun titleChangeDialog(
    onConfirmation: (String) -> Unit,
    onDismissRequest: () -> Unit,
    text: String,
    onValueChange: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = text,
                    onValueChange = {
                        onValueChange(it)
                    }
                )
                Text(
                    text = "Введите название",
                    modifier = Modifier.padding(16.dp),
                )
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
                        onClick = { onConfirmation(text) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Подтвердить")
                    }
                }
            }

        }
    }
}

