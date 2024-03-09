package com.example.decemberdef.ui.screens.authScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.AuthRoute
import com.example.decemberdef.ui.screens.signInApp.SignInApp
import com.example.decemberdef.ui.screens.authScreen.components.topAppBarAuthScreen
import com.example.decemberdef.ui.screens.homeScreen.MainViewModel
import com.example.decemberdef.ui.screens.mainScreen.mainScreen
import com.example.decemberdef.ui.screens.signUpApp.SignUpApp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavHostController = rememberNavController()
) {
    val authScreenViewModel: AuthScreenViewModel = viewModel(factory = AuthScreenViewModel.Factory)
    val uiState = authScreenViewModel.uiState.collectAsState().value
    var auth: FirebaseAuth = Firebase.auth
    auth.addAuthStateListener {
        authScreenViewModel.changeUserState(it.currentUser == null)
    }
    if (uiState.isUserNull) {
        Scaffold(
            topBar = { topAppBarAuthScreen() }
        ) { paddingValues ->
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
        }
    } else {
        mainScreen()


    }
}
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = AuthRoute.valueOf(
//        backStackEntry?.destination?.route ?: AuthRoute.SignIn.name
//    )
