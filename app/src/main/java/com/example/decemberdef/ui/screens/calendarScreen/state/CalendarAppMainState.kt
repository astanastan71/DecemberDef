package com.example.decemberdef.ui.screens.calendarScreen.state

import com.example.decemberdef.data.Task
import java.time.LocalDate

data class CalendarAppMainState(
    val filteredTaskList: MutableList<Task> = mutableListOf(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isWeekMode: Boolean = false,
    val taskList: List<Task> = listOf()
)