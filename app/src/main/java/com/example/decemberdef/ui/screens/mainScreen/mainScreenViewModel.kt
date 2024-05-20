package com.example.decemberdef.ui.screens.mainScreen

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.decemberdef.MainApplication
import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.mainScreen.states.MainScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LogOutState {
    object Success : LogOutState
    object Loading : LogOutState
}

sealed interface CollectionsListGetState {
    data class Success(val directions: Flow<List<Direction>>) : CollectionsListGetState
    object Error : CollectionsListGetState
    object Loading : CollectionsListGetState
}


sealed interface TasksListGetState {
    data class Success(val tasks: List<Task>) : TasksListGetState
    object Error : TasksListGetState
    object Loading : TasksListGetState
}

class MainScreenViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    var collectionsListGetState: CollectionsListGetState by mutableStateOf(CollectionsListGetState.Loading)
        private set

    var tasksListGetState: TasksListGetState by mutableStateOf(TasksListGetState.Loading)
        private set

    var logOutState: LogOutState = LogOutState.Loading
        private set

    init {
        getCollectionsData()
        getUserData()
        getMonitoredDirectionList()
    }

    fun getUserData() {
        viewModelScope.launch {
            val userData = mainRepository.getUserData()
            try {
                _uiState.update { currentState ->
                    currentState.copy(
                        user = userData
                    )
                }
                Log.d(ContentValues.TAG, "Good went")
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Bad Error: $e")
            }
        }
    }

    fun updateCurrentDirectionFromLink(parameter: String){
        viewModelScope.launch {
            val parts = parameter.split("AndAlso")
            val direction = mainRepository.getSingleDirectionForLink(parts[1], parts[0])
            _uiState.update { currentState ->
                currentState.copy(
                    currentDirectionLink = direction
                )
            }
        }
    }

    fun addOtherUserDirection(parameter: String, tasks: List<Task>) {
        val parts = parameter.split("AndAlso")
        viewModelScope.launch {
            mainRepository.addOtherUserDirection(parts[1], parts[0], tasks)
        }
    }

    fun monitorDirection(parameter: String){
        val parts = parameter.split("AndAlso")
        viewModelScope.launch {
            mainRepository.monitorOtherUserDirection(parts[1], parts[0])
        }

    }

    private fun getCollectionsData() {
        viewModelScope.launch {
            collectionsListGetState = try {
                if (mainRepository.getDirectionsList() != null) {
                    CollectionsListGetState.Success(mainRepository.getDirectionsList()!!)
                } else {
                    CollectionsListGetState.Loading
                }

            } catch (e: Exception) {
                CollectionsListGetState.Error
            }
        }
    }

    fun getTasksDataFromLink(parameter: String) {
        val parts = parameter.split("AndAlso")
        viewModelScope.launch {
            tasksListGetState = try {
                TasksListGetState.Success(mainRepository.getTasksListFromLink(parts[1], parts[0]))
            } catch (e: Exception) {
                TasksListGetState.Error
            }
        }


    }

    fun getTasksData(directions: List<Direction>) {
        viewModelScope.launch {
            tasksListGetState = try {
                TasksListGetState.Success(mainRepository.collectTaskData(directions))
            } catch (e: Exception) {
                TasksListGetState.Error
            }
        }
    }

    private fun getMonitoredDirectionList(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    monitoredDirectionList = mainRepository.getMonitoredDirectionsList()
                )
            }
        }
    }

    fun signOut() {
        mainRepository.signOut()
        logOutState = LogOutState.Success
        Log.w(ContentValues.TAG, "signOut")
    }

    fun reset() {
        logOutState = LogOutState.Loading
    }

    fun updateUserInfo(userName: String) {
        viewModelScope.launch {
            mainRepository.userInfoUpdate(userName)
            getUserData()
        }

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