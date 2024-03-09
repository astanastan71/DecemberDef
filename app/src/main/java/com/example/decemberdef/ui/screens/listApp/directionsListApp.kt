package com.example.decemberdef.ui.screens.listApp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.HomeRoute
import com.example.decemberdef.ui.screens.listApp.components.directionsList
import com.example.decemberdef.ui.screens.listApp.components.taskList

@Composable
fun directionListApp(
    padding: PaddingValues,
    viewModel: DirectionListViewModel = viewModel(factory = DirectionListViewModel.Factory)
) {
    val navController = rememberNavController()
    val uiState = viewModel.uiState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = HomeRoute.DirectionScreen.name,
        modifier = Modifier.padding(padding)
    ) {
        composable(route = HomeRoute.DirectionScreen.name) {
            when (val directionListState = viewModel.collectionsListGetState) {
                is CollectionsListGetState.Success ->
                    directionsList(
                        onDirectionClick = {
                            viewModel.getDirectionTasks(it)
                            navController.navigate(
                                route = HomeRoute.TaskScreen.name + "/${it}"
                            )
                        },
                        isDoneClick = {isDone, uID ->
                            viewModel.setDirectionStatus(isDone, uID)
                        },
                        viewModel = viewModel,
                        directions = directionListState.directions.collectAsState(listOf()).value
                    )

                is CollectionsListGetState.Loading -> {}
                is CollectionsListGetState.Error ->
                    Text(text = "ERROR")
            }
        }
        composable(route = HomeRoute.TaskScreen.name + "/{direction_uid}") {
            when (val taskListState = viewModel.taskGetState) {
                is TaskGetState.Success ->
                    taskList(
                        onDateTimeConfirm = { calendar, taskId, isStart ->
                            viewModel.setDateAndTimeTaskStart(
                                calendar,
                                uiState.value.currentDirection,
                                taskId,
                                isStart
                            )
                        },
                        tasks = taskListState.tasks.collectAsState(initial = listOf()).value
                    )

                is TaskGetState.Loading -> {}
                is TaskGetState.Error ->
                    Text(text = "ERROR")
            }
        }
    }
}

