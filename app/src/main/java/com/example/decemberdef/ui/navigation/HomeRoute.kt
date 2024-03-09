package com.example.decemberdef.ui.navigation

import androidx.annotation.StringRes
import com.example.decemberdef.R

enum class HomeRoute(@StringRes val title: Int) {
    Start(title = R.string.start),
    TaskAdding(title = R.string.task_adding),
    DirectionScreen(title = R.string.direction),
    TaskScreen(title = R.string.task_show)
}