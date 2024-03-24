package com.example.decemberdef.ui.screens.homeScreen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.decemberdef.MainApplication
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.data.User
import com.example.decemberdef.ui.screens.homeScreen.states.HomeScreenState
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenState(user = User(userID = "Не найдено")))
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()


    init {
        getUserData()
    }

    private fun getUserData(
    ) {
        viewModelScope.launch {
            val userData = mainRepository.getUserData()
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        user = userData
                    )
                }
                Log.d(TAG, "Good went")
            } catch (e: Exception) {
                Log.e(TAG, "Bad Error: $e")
            }
        }


    }

    fun taskAdd(textState: RichTextState) {
        viewModelScope.launch {
            mainRepository.addCustomTaskAndDirection(textState)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                MainViewModel(mainRepository = mainRepository)
            }
        }
    }
}