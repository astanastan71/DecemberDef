package com.example.decemberdef.ui.screens.homeScreen.states

import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.User

data class HomeScreenState(
    val user: User,
    val directions: List<Direction>? = null,
    val directionId: String = ""
)