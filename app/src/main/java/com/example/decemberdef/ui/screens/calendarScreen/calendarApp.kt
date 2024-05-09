package com.example.decemberdef.ui.screens.calendarScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.calendarScreen.components.horizontalMonthCalendar

@Composable
fun calendarApp(
    calendarAppViewModel: CalendarAppViewModel = viewModel(),
    taskList: List<Task>
) {
    val uiState = calendarAppViewModel.uiState.collectAsState()
    calendarAppViewModel.updateTaskList(taskList)
    horizontalMonthCalendar(
        taskList = uiState.value.taskList,
        filteredTaskList = uiState.value.filteredTaskList,
        selectedDate = uiState.value.selectedDate,
        isWeekMode = uiState.value.isWeekMode,
        updateSelectedDate = {
            calendarAppViewModel.updateSelectedDate(it)
        },
        updateFilteredTaskList = {
            calendarAppViewModel.updateFilteredTaskList(it)
        },
        updateMode = {
            calendarAppViewModel.updateMode(it)
        }
    )
}