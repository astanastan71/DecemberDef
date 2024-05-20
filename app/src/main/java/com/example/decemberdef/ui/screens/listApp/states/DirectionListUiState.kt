package com.example.decemberdef.ui.screens.listApp.states

import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.Task
import kotlinx.coroutines.flow.Flow

data class DirectionListUiState(
    val currentDirection: Flow<Direction>?,
    val currentLink: String = "",
    val taskForTextEditor: Task = Task(),
    val isCurrentDirectionMonitored: Boolean = false
)