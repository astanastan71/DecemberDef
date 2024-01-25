package com.example.decemberdef.screens.authScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.navigation.AuthRoute
import com.example.decemberdef.screens.signInApp.SignInApp
import com.example.decemberdef.screens.authScreen.components.topAppBar
import com.example.decemberdef.screens.signUpApp.SignUpApp
import com.example.decemberdef.screens.signUpApp.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavHostController = rememberNavController(),
    authScreenViewModel: AuthScreenViewModel = viewModel()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AuthRoute.valueOf(
        backStackEntry?.destination?.route ?: AuthRoute.SignIn.name
    )
    val uiState = authScreenViewModel.uiState.collectAsState().value



    Scaffold(topBar = { topAppBar() }) { it ->
        if (uiState.user == null) {
            NavHost(
                navController = navController,
                startDestination = AuthRoute.SignIn.name,
                modifier = Modifier.padding(it)
            ) {
                composable(route = AuthRoute.SignIn.name) {
                    SignInApp(
                        onSignUpButtonClicked = { navController.navigate(AuthRoute.SignUp.name) }
                    )
                }
                composable(route = AuthRoute.SignUp.name) {
                    SignUpApp()


                }
            }
        }
        else {

        }


    }


}

