package com.example.decemberdef.ui.screens.listApp.states

import com.example.decemberdef.data.Direction
import kotlinx.coroutines.flow.Flow

data class DirectionListUiState(
    val currentDirection: Flow<Direction>?,
    val currentLink: String = ""
)