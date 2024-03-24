package com.example.decemberdef.ui.screens.mainScreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.navigation.HomeRoute
import com.example.decemberdef.ui.navigation.navigationBar
import com.example.decemberdef.ui.screens.calendarScreen.calendarApp
import com.example.decemberdef.ui.screens.homeScreen.HomeApp
import com.example.decemberdef.ui.screens.listApp.directionListApp
import com.example.decemberdef.ui.screens.mainScreen.components.topAppBarMainScreen

@Composable
fun mainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val navController = rememberNavController()

    val keyboardController = LocalSoftwareKeyboardController.current

    val lastOpenedDestination = remember { mutableStateOf(HomeRoute.DirectionScreen.name) }

    var currentDestinationId: Int? = null
    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentDestinationId = destination.id
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
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues).pointerInput(Unit){
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }
        ) {
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
                    mainNavControllerDestinationId = currentDestinationId,
                    directionListState = mainScreenViewModel.collectionsListGetState,
                    lastOpenedDestination = lastOpenedDestination.value,
                    onDestinationChange = { destination, _ ->
//                        if (destination.route.toString()!= HomeRoute.DirectionScreen.name)
//                            lastOpenedDestination.value = HomeRoute.TaskScreen.name + "/${directionUid}"
//                        else
                        lastOpenedDestination.value = destination.route.toString()

                    },
                    padding = paddingValues
                )
            }

        }

    }
}