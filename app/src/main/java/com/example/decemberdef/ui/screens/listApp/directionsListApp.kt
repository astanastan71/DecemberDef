package com.example.decemberdef.ui.screens.listApp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
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
import com.example.decemberdef.ui.screens.homeScreen.components.taskEditor
import com.example.decemberdef.ui.screens.listApp.components.directionsList
import com.example.decemberdef.ui.screens.listApp.components.taskList
import com.example.decemberdef.ui.screens.mainScreen.CollectionsListGetState


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun directionListApp(
    directionListState: CollectionsListGetState,
    monitoredDirections: List<Direction> = listOf(),
    pullRefreshState: PullRefreshState,
    refreshing: Boolean,
    viewModel: DirectionListViewModel = viewModel(factory = DirectionListViewModel.Factory)
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val uiState =
        viewModel.uiState.collectAsState().value.currentDirection?.collectAsState(initial = Direction())
    NavHost(
        navController = navController,
        startDestination =
        HomeRoute.DirectionScreen.name
    ) {
        composable(route = HomeRoute.DirectionScreen.name) {
            when (directionListState) {
                is CollectionsListGetState.Success ->
                    directionsList(
                        deleteMonitoredDirection = {
                            viewModel.deleteMonitoredDirection(it)
                        },
                        refreshing = refreshing,
                        pullRefreshState = pullRefreshState,
                        onDirectionClick = { monitored, direction ->
                            viewModel.getDirectionTasks(monitored, direction)
                            navController.navigate(
                                route = HomeRoute.TaskScreen.name + "/${direction.uid}"
                            )
                        },
                        viewModel = viewModel,
                        onDescriptionClick = { text, uID ->
                            viewModel.setDirectionDescription(text, uID)
                        },
                        directions = directionListState.directions.collectAsState(listOf()).value + monitoredDirections,
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
                    if (uiState != null) {
                        Log.d(TAG, "${uiState.value.uid}")
                        taskList(
                            monitored = viewModel.uiState.collectAsState().value.isCurrentDirectionMonitored,
                            onTextExpandClick = {
                                navController.navigate(HomeRoute.TaskAdding.name + "/${it.uid}")
                                viewModel.setTaskEditor(it)
                            },
                            onDateTimeConfirm = { calendar, taskId, isStart ->
                                viewModel.setDateAndTimeTaskStart(
                                    calendar,
                                    uiState.value.uid,
                                    taskId,
                                    isStart
                                )
                            },
                            onCompletionStatusClick = { status, uID ->
                                viewModel.setTaskCompletionStatus(
                                    status,
                                    uID,
                                    uiState.value.uid,
                                    uiState.value.progress
                                )
                            },
                            viewModel = viewModel,
                            tasks = taskListState.tasks.collectAsState(
                                initial =
                                listOf()
                            ).value,
                            onTaskDescriptionClick = { text, taskUid ->
                                viewModel.setTaskDescription(text, uiState.value.uid, taskUid)

                            },
                            onTitleChange = { title, taskId ->
                                viewModel.setTaskTitle(title, taskId, uiState.value.uid)
                            },
                            deleteTask = { task ->
                                viewModel.deleteTask(uiState.value, task)
                            },
                            scheduleNotification = { time, title, description, start, id, active ->
                                if (viewModel.checkNotificationPermissions()) {
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
                            },
                            cancelNotification = { notificationId, title, description, start, id, active ->
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
                        )
                    }

                is TaskGetState.Loading -> {}
                is TaskGetState.Error ->
                    Text(text = "ERROR")
            }
        }
        composable(route = HomeRoute.TaskAdding.name + "/{task_uid}") {
            Box(modifier = Modifier.fillMaxSize()) {
                taskEditor(
                    task = viewModel.uiState.value.taskForTextEditor,
                    isNewTask = false,
                    onOldTaskChange = { task, text ->
                        if (uiState != null) {
                            viewModel.setTaskDescription(text, uiState.value.uid, task.uid)
                        }
                    }
                )
            }
        }
    }
}

