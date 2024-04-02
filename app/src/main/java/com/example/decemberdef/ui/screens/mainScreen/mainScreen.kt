package com.example.decemberdef.ui.screens.mainScreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.navigation.Route
import com.example.decemberdef.ui.navigation.navigationBar
import com.example.decemberdef.ui.screens.calendarScreen.calendarApp
import com.example.decemberdef.ui.screens.homeScreen.HomeApp
import com.example.decemberdef.ui.screens.linkScreen.linkApp
import com.example.decemberdef.ui.screens.listApp.directionListApp
import com.example.decemberdef.ui.screens.mainScreen.components.topAppBarMainScreen

@Composable
fun mainScreen(
    parameter: String? = null,
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val navController = rememberNavController()

    val keyboardController = LocalSoftwareKeyboardController.current



    val startDestinationId = if (parameter != null) {
        Route.LinkScreen.route
    } else {
        BottomNavItem.Home.route
    }


    Scaffold(
        topBar = {
            topAppBarMainScreen(onLogOutClick = {
                mainScreenViewModel.signOut()
            })
        },
        bottomBar = {
            navigationBar(navController)
        }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestinationId,
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                    })
                }
        ) {
            composable(route = Route.LinkScreen.route) {
                if (parameter != null) {
                    mainScreenViewModel.getTasksDataFromLink(parameter)
                }
                when (val tasksListGetState = mainScreenViewModel.tasksListGetState) {
                    is TasksListGetState.Success -> {
                        val tasks =
                            tasksListGetState.tasks
                        linkApp(
                            taskList = tasks,
                            parameter = parameter,
                            addOtherUserDirection = {
                                if (parameter != null) {
                                    mainScreenViewModel.addOtherUserDirection(parameter, tasks)
                                }
                            }
                        )
                    }

                    is TasksListGetState.Loading -> {
                        Text("Loading")
                    }

                    is TasksListGetState.Error -> {
                        Text("ERROR")

                    }
                }

            }
            composable(route = BottomNavItem.Home.route) {
                HomeApp(
                    modifier = Modifier
                )
            }
            composable(route = BottomNavItem.Calendar.route) {
                when (val directionsListGetState = mainScreenViewModel.collectionsListGetState) {
                    is CollectionsListGetState.Success -> {
                        mainScreenViewModel.getTasksData(
                            directionsListGetState.directions.collectAsState(
                                initial = mutableListOf()
                            ).value
                        )
                        when (val tasksListGetState = mainScreenViewModel.tasksListGetState) {
                            is TasksListGetState.Success -> {
                                val tasks =
                                    tasksListGetState.tasks
                                calendarApp(taskList = tasks)
                            }

                            is TasksListGetState.Loading -> {
                                Text("Loading")
                            }

                            is TasksListGetState.Error -> {
                                Text("ERROR")

                            }
                        }
                    }

                    else -> {}
                }
            }
            composable(route = BottomNavItem.DirectionChooser.route) {
                directionListApp(
                    directionListState = mainScreenViewModel.collectionsListGetState,
                    padding = paddingValues
                )
            }

        }

    }
}