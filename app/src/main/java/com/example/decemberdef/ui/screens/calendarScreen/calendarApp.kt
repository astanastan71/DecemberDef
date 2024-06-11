package com.example.decemberdef.ui.screens.calendarScreen

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.calendarScreen.components.horizontalMonthCalendar

@Composable
fun calendarApp(
    calendarAppViewModel: CalendarAppViewModel = viewModel(),
    taskList: List<Task>
) {
    val uiState = calendarAppViewModel.uiState.collectAsState().value
    var filteredTaskList = calendarAppViewModel.filteredTaskGetState
    LaunchedEffect(taskList) {
        calendarAppViewModel.updateTaskList(taskList)
    }
    horizontalMonthCalendar(
        taskList = uiState.taskList,
        filteredTaskList = filteredTaskList,
        selectedDate = uiState.selectedDate,
        isWeekMode = uiState.isWeekMode,
        updateSelectedDate = {
            calendarAppViewModel.updateSelectedDate(it)
        },
        updateFilteredTaskList = {
            calendarAppViewModel.updateFilteredTaskList(it)
            Log.d(ContentValues.TAG, "taskList: ${uiState.filteredTaskList}")
        },
        updateMode = {
            calendarAppViewModel.updateMode(it)
        }
    )
}