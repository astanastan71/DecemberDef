package com.example.decemberdef.ui.screens.listApp.components

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.decemberdef.R
import com.example.decemberdef.data.Task
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun taskList(
    onDateTimeConfirm: (Long, String, Boolean) -> Unit,
    tasks: List<Task>
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(tasks) {
            taskItem(
                item = it,
                modifier = Modifier.padding(8.dp),
                onDateTimeConfirm = onDateTimeConfirm
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun taskItem(
    item: Task,
    onDateTimeConfirm: (Long, String, Boolean) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val taskEditorState = rememberRichTextState()
    val dateStateStart = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val dateStateEnd = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    taskEditorState
        .toggleSpanStyle(
            SpanStyle(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )
        )
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Row() {
                    RichTextEditor(
                        state = taskEditorState
                    )
                }
                dateTimeItem(
                    dateState = dateStateStart,
                    dateItem = item.timeStart.toDate().toLocaleString(),
                    item = item,
                    onDateTimeConfirm = onDateTimeConfirm,
                    isStart = true
                )
                dateTimeItem(
                    dateState = dateStateEnd,
                    dateItem = item.timeEnd.toDate().toLocaleString(),
                    item = item,
                    onDateTimeConfirm = onDateTimeConfirm,
                    isStart = false
                )
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
    onDateTimeConfirm: (Long, String, Boolean) -> Unit
) {
    var expandedDatePicker by remember { mutableStateOf(false) }
    var expandedTimePicker by remember { mutableStateOf(false) }
    Row(modifier = Modifier.clickable {
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