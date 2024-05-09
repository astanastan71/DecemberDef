package com.example.decemberdef.ui.screens.calendarScreen

import androidx.lifecycle.ViewModel
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.calendarScreen.components.timestampToLocalDate
import com.example.decemberdef.ui.screens.calendarScreen.state.CalendarAppMainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CalendarAppViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarAppMainState())
    val uiState: StateFlow<CalendarAppMainState> = _uiState.asStateFlow()

    init {

    }

    fun updateTaskList(taskList: List<Task>) {
        val filteredTaskList =
            taskList.filter { task -> timestampToLocalDate(task.timeStart.seconds) == _uiState.value.selectedDate } as MutableList<Task>
        filteredTaskList.sortBy { it.timeStart }
        _uiState.update { currentState ->
            currentState.copy(
                taskList = taskList,
                filteredTaskList = filteredTaskList
            )
        }
    }

    fun updateMode(mode: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isWeekMode = mode
            )
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
    }

    fun updateFilteredTaskList(
        date: LocalDate
    ) {
        val filteredTaskList =
            _uiState.value.taskList.filter { task -> timestampToLocalDate(task.timeStart.seconds) == date } as MutableList<Task>
        filteredTaskList.sortBy { it.timeStart }
        _uiState.update { currentState ->
            currentState.copy(
                filteredTaskList = filteredTaskList
            )
        }
    }
}