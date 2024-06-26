package com.example.decemberdef.ui.navigation.BottomNav

import com.example.decemberdef.R
import com.example.decemberdef.ui.navigation.Route


sealed class BottomNavItem(
    var title: String,
    var icon: Int,
    var route: String,
) {
    object Home : BottomNavItem(
        "Главный экран",
        R.drawable.home_button,
        Route.HomeScreen.route
    )
    object DirectionChooser : BottomNavItem(
        "Направления",
        R.drawable.direction_button,
        Route.DirectionsScreen.route
    )
    object Calendar : BottomNavItem(
        "Календарь",
        R.drawable.schedule_ic,
        Route.TaskScreen.route
    )
}
