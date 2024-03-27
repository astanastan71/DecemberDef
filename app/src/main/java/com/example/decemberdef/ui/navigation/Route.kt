package com.example.decemberdef.ui.navigation

sealed class Route(val route: String) {
    object HomeScreen : Route("home_screen")
    object DirectionsScreen : Route("direction_screen")
    object TaskScreen : Route("task_screen")
    object LinkScreen: Route("route_screen")

}