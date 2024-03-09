package com.example.decemberdef.ui.screens.mainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.navigation.navigationBar
import com.example.decemberdef.ui.screens.homeScreen.HomeApp
import com.example.decemberdef.ui.screens.listApp.directionListApp
import com.example.decemberdef.ui.screens.mainScreen.components.topAppBarMainScreen

@Composable
fun mainScreen(
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            topAppBarMainScreen(onLogOutClick = {

            })
        },
        bottomBar = {
            navigationBar(navController)
        }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = BottomNavItem.Home.route) {
                HomeApp(
                    modifier = Modifier
                )
            }
            composable(route = BottomNavItem.TaskBuilder.route) {
            }
            composable(route = BottomNavItem.DirectionChooser.route) {
                directionListApp(
                    padding = paddingValues
                )
            }

        }

    }
}