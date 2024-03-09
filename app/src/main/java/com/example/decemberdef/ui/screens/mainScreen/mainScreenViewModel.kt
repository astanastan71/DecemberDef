package com.example.decemberdef.ui.screens.mainScreen

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.decemberdef.MainApplication
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.ui.screens.homeScreen.MainViewModel

sealed interface LogOutState {
    object Success : LogOutState
    object Loading : LogOutState
}

class MainScreenViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    var logOutState: LogOutState = LogOutState.Loading
        private set

    fun signOut() {
        mainRepository.signOut()
        logOutState = LogOutState.Success
        Log.w(ContentValues.TAG, "signOut")
    }

    fun reset(){
        logOutState = LogOutState.Loading
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                MainScreenViewModel(mainRepository = mainRepository)
            }
        }
    }
}