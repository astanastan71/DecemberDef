package com.example.decemberdef.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.HomeRoute
import com.example.decemberdef.ui.screens.homeScreen.components.homeScreenBox
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun HomeApp(
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory),
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val taskEditorState = rememberRichTextState()
    val navController: NavHostController = rememberNavController()
    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute.Start.name
        )
        {
            composable(route = HomeRoute.Start.name) {
                homeScreenBox(
                    userName = uiState.user.userID,
                    onClickStart = { navController.navigate(HomeRoute.TaskAdding.name) }
                )
            }
            composable(route = HomeRoute.TaskAdding.name) {
                customTaskStart(
                    taskEditorState = taskEditorState,
                    taskAdd = { viewModel.taskAdd(taskEditorState) }
                )

            }

        }

    }


}