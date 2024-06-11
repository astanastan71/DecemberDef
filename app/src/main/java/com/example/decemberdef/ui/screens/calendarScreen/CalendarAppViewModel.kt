package com.example.decemberdef.ui.screens.calendarScreen

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.calendarScreen.components.isDateInRange
import com.example.decemberdef.ui.screens.calendarScreen.components.timestampToLocalDate
import com.example.decemberdef.ui.screens.calendarScreen.state.CalendarAppMainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

sealed interface FilteredTaskGetState {
    data class Success(val tasks: List<Task>) : FilteredTaskGetState
    object Loading : FilteredTaskGetState
    object Error : FilteredTaskGetState
}

class CalendarAppViewModel(
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarAppMainState())
    val uiState: StateFlow<CalendarAppMainState> = _uiState.asStateFlow()

    var filteredTaskGetState: FilteredTaskGetState by mutableStateOf(FilteredTaskGetState.Loading)
        private set

    init {
        updateFilteredTaskList(LocalDate.now())
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
        updateFilteredTaskList(LocalDate.now())
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
        try {
            filteredTaskGetState = FilteredTaskGetState.Success(
                _uiState.value.taskList.filter { task ->
                    if (task.continued) {
                        isDateInRange(
                            date,
                            timestampToLocalDate(task.timeStart.seconds),
                            timestampToLocalDate(task.timeEnd.seconds)
                        )
                    } else {
                        timestampToLocalDate(task.timeStart.seconds) == date
                    }
                }.sortedBy { it.timeStart }.reversed()
            )
        } catch (e: Exception) {
            filteredTaskGetState = FilteredTaskGetState.Error
        }
        Log.d(ContentValues.TAG, "taskList updated $filteredTaskGetState")
    }
}