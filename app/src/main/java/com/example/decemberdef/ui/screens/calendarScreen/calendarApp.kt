package com.example.decemberdef.ui.screens.calendarScreen

import androidx.compose.runtime.Composable
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.calendarScreen.components.horizontalMonthCalendar

@Composable
fun calendarApp(taskList: List<Task>){
    horizontalMonthCalendar(taskList = taskList)
}