package com.example.decemberdef.ui.screens.calendarScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.listApp.components.taskItem
import com.example.decemberdef.ui.theme.DecemberDefTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun horizontalMonthCalendar(
    taskList: List<Task> = listOf()
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }

    var filteredTaskList by remember { mutableStateOf<MutableList<Task>>(mutableListOf()) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val taskState = rememberLazyListState()

    Column() {
        HorizontalCalendar(
            state = state,
            monthHeader = {
                MonthHeader(
                    month = it.yearMonth.month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale.getDefault()
                    )
                )
                DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title here
            },
            dayContent = { day ->
                Day(
                    tasksList = taskList,
                    day = day,
                    isSelected = selectedDate == day.date
                ) { day ->
                    selectedDate = if (selectedDate == day.date) null else day.date
                    filteredTaskList =
                        taskList.filter { task -> timestampToLocalDate(task.timeStart.seconds) == day.date } as MutableList<Task>
                }
            }
        )
        LazyColumn(contentPadding = PaddingValues(5.dp), state = taskState) {
            itemsIndexed(filteredTaskList) { index, task ->
                taskItem(
                    readOnly = true,
                    item = task,
                    modifier = Modifier.padding(8.dp),
                    onTaskDescriptionClick = {_,_ -> },
                    index = index,
                    taskState = taskState
                )
            }
        }
    }
}

@Composable
fun MonthHeader(month: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = month.toString())
    }
}

@Composable
fun Day(
    tasksList: List<Task> = listOf(),
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    var color = Color.Black
    if (day.position == DayPosition.MonthDate) {
        for (task in tasksList) {
            if (timestampToLocalDate(task.timeStart.seconds) == day.date) {
                color = Color.Green
                break
            }
        }
    } else color = Color.Gray
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = if (isSelected) Color.Green else Color.Transparent
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        // This is important for square sizing!
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = color
        )


    }
}

fun timestampToLocalDate(timestamp: Long): LocalDate {
    val instant: Instant = Instant.ofEpochSecond(timestamp)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek)
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT, Locale.getDefault()
                ),
            )
    }
}

@Composable
@Preview
fun horizontalMonthCalendarPreview() {
    DecemberDefTheme {
        Surface {
            horizontalMonthCalendar()

        }
    }
}