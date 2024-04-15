package com.example.decemberdef.ui.screens.listApp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.data.Direction
import com.example.decemberdef.ui.navigation.HomeRoute
import com.example.decemberdef.ui.screens.listApp.components.directionsList
import com.example.decemberdef.ui.screens.listApp.components.taskList
import com.example.decemberdef.ui.screens.mainScreen.CollectionsListGetState


@Composable
fun directionListApp(
    padding: PaddingValues,
    directionListState: CollectionsListGetState,
    viewModel: DirectionListViewModel = viewModel(factory = DirectionListViewModel.Factory)
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val uiState =
        viewModel.uiState.collectAsState().value.currentDirection?.collectAsState(initial = Direction())
    NavHost(
        navController = navController,
        startDestination =
        HomeRoute.DirectionScreen.name,
        modifier = Modifier.padding(padding)
    ) {
        composable(route = HomeRoute.DirectionScreen.name) { it ->
            when (directionListState) {
                is CollectionsListGetState.Success ->
                    directionsList(
                        onDirectionClick = {
                            viewModel.getDirectionTasks(it)
                            navController.navigate(
                                route = HomeRoute.TaskScreen.name + "/${it.uid}"
                            )
                        },
                        viewModel = viewModel,
                        onDescriptionClick = { text, uID ->
                            viewModel.setDirectionDescription(text, uID)
                        },
                        directions = directionListState.directions.collectAsState(listOf()).value,
                        link = viewModel.uiState.collectAsState().value.currentLink,
                        onTitleChange = { title, directionId ->
                            viewModel.setDirectionTitle(title, directionId)
                        },
                        setSharedStatus = { shared, directionId ->
                            viewModel.setDirectionSharedStatus(shared, directionId)
                        },
                        onDirectionDelete = {
                            viewModel.deleteDirection(it)
                        }
                    )

                is CollectionsListGetState.Loading -> {}
                is CollectionsListGetState.Error -> {
                    Text(text = "ERROR")
                }

            }
        }
        composable(route = HomeRoute.TaskScreen.name + "/{direction_uid}") {
            when (val taskListState = viewModel.taskGetState) {
                is TaskGetState.Success ->
                    taskList(
                        onDateTimeConfirm = { calendar, taskId, isStart ->
                            if (uiState != null) {
                                viewModel.setDateAndTimeTaskStart(
                                    calendar,
                                    uiState.value.uid,
                                    taskId,
                                    isStart
                                )
                            }
                        },
                        onCompletionStatusClick = { status, uID ->
                            if (uiState != null) {
                                viewModel.setTaskCompletionStatus(
                                    status,
                                    uID,
                                    uiState.value.uid,
                                    uiState.value.progress
                                )
                            }
                        },
                        viewModel = viewModel,
                        tasks = taskListState.tasks.collectAsState(
                            initial =
                            listOf()
                        ).value,
                        onTaskDescriptionClick = { text, taskUid ->
                            if (uiState != null) {
                                viewModel.setTaskDescription(text, uiState.value.uid, taskUid)
                            }

                        },
                        onTitleChange = { title, taskId ->
                            if (uiState != null) {
                                viewModel.setTaskTitle(title, taskId, uiState.value.uid)
                            }
                        },
                        deleteTask = { task ->
                            if (uiState != null) {
                                viewModel.deleteTask(uiState.value, task)
                            }
                        },
                        scheduleNotification = { time, title, description, start, id, active ->
                            if (viewModel.checkNotificationPermissions()) {
                                if (uiState != null) {
                                    viewModel.scheduleNotification(
                                        time,
                                        title,
                                        description,
                                        context,
                                        id,
                                        uiState.value.uid,
                                        start,
                                        active
                                    )
                                }
                            }
                        },
                        cancelNotification = { notificationId, title, description, start, id, active ->
                            if (uiState != null) {
                                viewModel.deleteNotification(
                                    notificationId,
                                    title,
                                    description,
                                    id,
                                    uiState.value.uid,
                                    active,
                                    context,
                                    start
                                )
                            }

                        }
                    )

                is TaskGetState.Loading -> {}
                is TaskGetState.Error ->
                    Text(text = "ERROR")
            }
        }
    }
}

