package com.example.decemberdef.ui.screens.mainScreen

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.AuthRoute
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.navigation.Route
import com.example.decemberdef.ui.navigation.navigationBar
import com.example.decemberdef.ui.screens.calendarScreen.calendarApp
import com.example.decemberdef.ui.screens.homeScreen.HomeApp
import com.example.decemberdef.ui.screens.linkScreen.linkApp
import com.example.decemberdef.ui.screens.listApp.directionListApp
import com.example.decemberdef.ui.screens.mainScreen.components.topAppBarMainScreen
import com.example.decemberdef.ui.screens.signUpApp.SignUpApp
import com.example.decemberdef.ui.theme.roboto

@Composable
fun mainScreen(
    parameter: String? = null,
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val navController = rememberNavController()

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val startDestinationId = if (parameter != null) {
        Route.LinkScreen.route
    } else {
        BottomNavItem.Home.route
    }

    var showUserInfoDialog by remember { mutableStateOf(false) }
    val uiState = mainScreenViewModel.uiState.collectAsState()
    var userName by remember { mutableStateOf(uiState.value.user.userName) }

    if (showUserInfoDialog) {
        Dialog(
            onDismissRequest = { showUserInfoDialog = false },
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "ID пользователя: ${uiState.value.user.userID}",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    Text(
                        text = "Email пользователя: ${uiState.value.user.userEmail}",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    Text(
                        text = "Имя пользователя:",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        ),
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    TextField(
                        value = userName,
                        onValueChange = {
                            userName = it
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { showUserInfoDialog = false },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Отмена")
                        }
                        TextButton(
                            onClick = {
                                showUserInfoDialog = false
                                mainScreenViewModel.updateUserInfo(userName)
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Подтвердить")
                        }
                    }
                }
            }
        }
    }


    Scaffold(
        topBar = {
            topAppBarMainScreen(
                isAnon = uiState.value.user.isAnon,
                onLogOutClick = {
                    mainScreenViewModel.signOut()
                },
                changeDialogStatus = {
                    showUserInfoDialog = true
                },
                onAnonRegisterClick = {
                    navController.navigate(AuthRoute.SignUp.name)
                }
            )
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
            composable(route = AuthRoute.SignUp.name) {
                SignUpApp(
                    isAnonRegister = true,
                    goBackandUpdate = {
                        navController.navigate(Route.HomeScreen.route)
                        mainScreenViewModel.getUserData()
                    }
                )
            }
            composable(route = Route.LinkScreen.route) {
                if (parameter != null) {
                    mainScreenViewModel.getTasksDataFromLink(parameter)
                    mainScreenViewModel.updateCurrentDirectionFromLink(parameter)
                }
                when (val tasksListGetState = mainScreenViewModel.tasksListGetState) {
                    is TasksListGetState.Success -> {
                        val tasks =
                            tasksListGetState.tasks
                        val direction =
                            mainScreenViewModel.uiState.collectAsState().value.currentDirectionLink
                        linkApp(
                            direction = direction,
                            taskList = tasks,
                            parameter = parameter,
                            addOtherUserDirection = {
                                if (parameter != null) {
                                    mainScreenViewModel.addOtherUserDirection(parameter, tasks)
                                    Toast.makeText(
                                        context,
                                        "Направление добавлено",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            },
                            monitorDirection = {
                                if (parameter != null) {
                                    mainScreenViewModel.monitorDirection(parameter)
                                    Toast.makeText(
                                        context,
                                        "Направление транслируется",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
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
                            ).value + uiState.value.monitoredDirectionList
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
                    monitoredDirections = uiState.value.monitoredDirectionList
                )
            }

        }

    }
}

