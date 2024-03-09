package com.example.decemberdef.ui.screens.listApp

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
import com.example.decemberdef.ui.screens.listApp.states.DirectionListUiState
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

sealed interface TaskGetState {
    data class Success(val tasks: Flow<List<Task>>) : TaskGetState
    object Loading : TaskGetState
    object Error : TaskGetState
}

sealed interface CollectionsListGetState {
    data class Success(val directions: Flow<List<Direction>>) : CollectionsListGetState
    object Error : CollectionsListGetState
    object Loading : CollectionsListGetState
}

class DirectionListViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    var taskGetState: TaskGetState by mutableStateOf(TaskGetState.Loading)
        private set

    private val _uiState = MutableStateFlow(DirectionListUiState())
    val uiState: StateFlow<DirectionListUiState> = _uiState.asStateFlow()

    var collectionsListGetState: CollectionsListGetState by mutableStateOf(CollectionsListGetState.Loading)
        private set

    init {
        getCollectionsData()
    }

    private fun changeCurrentDirection(directionId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentDirection = directionId
            )
        }
    }

    fun setDateAndTimeTaskStart(
        selectedDate: Long,
        directionId: String, taskId: String, isStart: Boolean
    ) {
        viewModelScope.launch {
            mainRepository.setTaskDateStart(
                taskId,
                directionId,
                Timestamp(Date(selectedDate)),
                isStart
            )
        }
    }

    fun getDirectionTasks(directionId: String) {
        viewModelScope.launch {
            taskGetState = mainRepository.getDirectionTasks(directionId)
        }
        changeCurrentDirection(directionId)
    }

    fun addCustomDirection(){
        viewModelScope.launch {
            mainRepository.addCustomDirection()
        }
    }

    fun setDirectionStatus(isDone: Boolean, uID: String){
        viewModelScope.launch {
            mainRepository.setDirectionStatus(isDone, uID)
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

    fun reset() {
        taskGetState = TaskGetState.Loading
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                DirectionListViewModel(mainRepository = mainRepository)
            }
        }
    }
}
