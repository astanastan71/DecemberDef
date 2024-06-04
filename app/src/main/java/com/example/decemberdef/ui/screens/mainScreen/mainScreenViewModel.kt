package com.example.decemberdef.ui.screens.mainScreen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import com.example.decemberdef.Notification
import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.MainRepository
import com.example.decemberdef.data.Task
import com.example.decemberdef.messageExtra
import com.example.decemberdef.notificationID
import com.example.decemberdef.titleExtra
import com.example.decemberdef.ui.screens.mainScreen.states.MainScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Random

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
    private val mainRepository: MainRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    var collectionsListGetState: CollectionsListGetState by mutableStateOf(CollectionsListGetState.Loading)
        private set

    var tasksListGetState: TasksListGetState by mutableStateOf(TasksListGetState.Loading)
        private set

    var logOutState: LogOutState = LogOutState.Loading
        private set

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getCollectionsData()
        getUserData()
        getMonitoredDirectionList()
    }

    fun getCollectionsData() {
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
            _uiState.update { currentState ->
                currentState.copy(
                    monitoredDirectionList = mainRepository.getMonitoredDirectionsList()
                )
            }
            _isRefreshing.emit(false)
        }

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

    fun updateCurrentDirectionFromLink(parameter: String) {
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

    fun monitorDirection(parameter: String) {
        val parts = parameter.split("AndAlso")
        viewModelScope.launch {
            mainRepository.monitorOtherUserDirection(parts[1], parts[0])
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

    private fun getMonitoredDirectionList() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    monitoredDirectionList = mainRepository.getMonitoredDirectionsList()
                )
            }
        }
    }

    fun deleteAndScheduleAllNotifications(tasks: List<Task>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        tasks.forEach {
            if (it.startNotificationActive) {
                val intent = Intent(context.applicationContext, Notification::class.java)
                intent.action = "ALARM_ACTION"

                intent.putExtra(titleExtra, it.title)
                intent.putExtra(messageExtra, it.description)
                intent.putExtra(notificationID, it.notificationStartId)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    it.notificationStartId,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.cancel(pendingIntent)

                viewModelScope.launch {
                    mainRepository.cancelNotification(it.uid, it.directionId, true)
                    mainRepository.isStartNotificationActiveChange(it.uid, it.directionId, false)
                    scheduleNotification(
                        it.timeStart.seconds * 1000 + it.timeStart.nanoseconds / 1000000,
                        title = it.title,
                        description = it.description,
                        taskId = it.uid,
                        collectionId = it.directionId,
                        start = true,
                        active = true
                    )
                }
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(
        time: Long,
        title: String,
        description: String,
        taskId: String,
        collectionId: String,
        start: Boolean,
        active: Boolean
    ) {
        //Инициализация намерения уведомления
        val intent = Intent(context.applicationContext, Notification::class.java)
        intent.action = "ALARM_ACTION"

        //Генерация идентификатора
        val genId = generateUniqueIntId()

        //Заполнения данными
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, description)
        intent.putExtra(notificationID, genId)

        //Обновления данных задачи
        viewModelScope.launch {
            mainRepository.setNotificationId(taskId, collectionId, start, genId)
            mainRepository.isStartNotificationActiveChange(taskId, collectionId, active)
        }

        //Отложенное намерение
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            genId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun generateUniqueIntId(): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val randomNumber = Random().nextInt(Int.MAX_VALUE) // Generate random int within int range

        // Ensure both operands have the same type (long) before XOR
        val combinedLong = currentTimeMillis.toLong() xor randomNumber.toLong()

        // Take the least significant bits to get an int within the desired range
        return (combinedLong and Int.MAX_VALUE.toLong()).toInt()
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
                val appContext by lazy { application.applicationContext }
                MainScreenViewModel(
                    mainRepository = mainRepository,
                    context = appContext
                )
            }
        }
    }
}