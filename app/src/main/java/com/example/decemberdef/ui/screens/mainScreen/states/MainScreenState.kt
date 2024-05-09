package com.example.decemberdef.ui.screens.mainScreen.states

import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.User

data class MainScreenState(
    val user:User = User(),
    val currentDirectionLink:Direction = Direction()
)