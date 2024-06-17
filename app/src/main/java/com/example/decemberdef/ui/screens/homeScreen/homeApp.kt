package com.example.decemberdef.ui.screens.homeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute.Start.name
        )
        {
            composable(route = HomeRoute.Start.name) {
                homeScreenBox(
                    userName = uiState.user.userName,
                    onClickStart = { navController.navigate(HomeRoute.TaskAdding.name) }
                )
            }
            composable(route = HomeRoute.TaskAdding.name) {
                customTaskStart(
                    taskAdd = {
                        viewModel.taskAdd(taskEditorState)
                        Toast.makeText(
                            context,
                            "Задача добавлена",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        navController.navigate(HomeRoute.Start.name)
                    }
                )
            }


        }

    }


}