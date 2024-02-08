package com.example.decemberdef.ui.screens.authScreen

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
import com.example.decemberdef.ui.navigation.AuthRoute
import com.example.decemberdef.ui.screens.signInApp.SignInApp
import com.example.decemberdef.ui.screens.authScreen.components.topAppBar
import com.example.decemberdef.ui.screens.mainScreen.MainApp
import com.example.decemberdef.ui.screens.signInApp.SignInViewModel
import com.example.decemberdef.ui.screens.signUpApp.SignUpApp
import com.example.decemberdef.ui.screens.signUpApp.SignUpViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavHostController = rememberNavController()
) {
    val authScreenViewModel: AuthScreenViewModel = viewModel()
    val uiState = authScreenViewModel.uiState.collectAsState().value
    Scaffold(
        topBar = { topAppBar() }
    ) { paddingValues ->
        var auth: FirebaseAuth = Firebase.auth
        auth.addAuthStateListener {
            authScreenViewModel.changeUserState(it.currentUser == null)

        }
        if (uiState.isUserNull) {
            NavHost(
                navController = navController,
                startDestination = AuthRoute.SignIn.name,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = AuthRoute.SignIn.name) {
                    SignInApp(
                        onSignUpButtonClicked = {
                            navController.navigate(AuthRoute.SignUp.name)
                        }
                    )
                }
                composable(route = AuthRoute.SignUp.name) {
                    SignUpApp()
                }
            }
        } else {
            MainApp(
                modifier = Modifier.padding(paddingValues)
            )

        }


    }
}
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = AuthRoute.valueOf(
//        backStackEntry?.destination?.route ?: AuthRoute.SignIn.name
//    )
