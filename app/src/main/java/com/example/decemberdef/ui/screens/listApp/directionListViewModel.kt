package com.example.decemberdef.ui.screens.listApp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.example.decemberdef.ui.screens.listApp.states.DirectionListUiState
import com.google.firebase.Timestamp
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random

sealed interface TaskGetState {
    data class Success(val tasks: Flow<List<Task>>) : TaskGetState
    object Loading : TaskGetState
    object Error : TaskGetState
}

//sealed interface CollectionsListGetState {
//    data class Success(val directions: Flow<List<Direction>>) : CollectionsListGetState
//    object Error : CollectionsListGetState
//    object Loading : CollectionsListGetState
//}

class DirectionListViewModel(
    private val mainRepository: MainRepository,
    private val context: Context
) : ViewModel() {

    var taskGetState: TaskGetState by mutableStateOf(TaskGetState.Loading)
        private set

    private val _uiState = MutableStateFlow(DirectionListUiState(null))
    val uiState: StateFlow<DirectionListUiState> = _uiState.asStateFlow()

    init {
    }

    fun getUser(): String? {
        val user = mainRepository.getUser()
        return user?.uid
    }

    fun setTaskEditor(task: Task) {
        _uiState.update { currentState ->
            currentState.copy(
                taskForTextEditor = task
            )
        }

    }

    fun setTaskCompletionStatus(
        status: Boolean,
        uID: String,
        directionId: String,
        directionProgress: Int
    ) {
        viewModelScope.launch {
            mainRepository.setTaskCompletionStatus(
                status, uID, directionId, directionProgress
            )

        }

    }

    fun deleteMonitoredDirection(linkId: String){
        viewModelScope.launch {
            mainRepository.deleteMonitoredDirection(linkId)
        }
    }

    fun updateCurrentLink(link: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentLink = link
            )
        }
    }

    fun deleteTask(direction: Direction, task: Task) {
        viewModelScope.launch {
            mainRepository.deleteTask(direction, task)
        }
    }

    fun setTaskTitle(
        title: String,
        taskId: String,
        directionId: String
    ) {
        viewModelScope.launch {
            mainRepository.setTaskTitle(
                directionId,
                taskId,
                title
            )
        }
    }

    fun setDirectionTitle(
        title: String,
        directionId: String
    ) {
        viewModelScope.launch {
            mainRepository.setDirectionTitle(
                directionId,
                title
            )
        }
    }

    fun setDirectionSharedStatus(
        shared: Boolean,
        directionId: String
    ) {
        viewModelScope.launch {
            mainRepository.setDirectionShareMode(shared, directionId)

        }
    }

    fun deleteDirection(directionId: String) {
        viewModelScope.launch {
            mainRepository.deleteDirection(directionId)
        }
    }

    fun setTaskDescription(
        text: RichTextState,
        directionId: String,
        taskId: String
    ) {
        viewModelScope.launch {
            mainRepository.setTaskDescription(text, directionId, taskId)
        }
    }

    private fun changeCurrentDirection(direction: Direction) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    currentDirection = mainRepository.getCurrentDirection(
                        direction.userId,
                        direction.uid
                    )
                )
            }
        }

    }

    fun setDirectionDescription(text: RichTextState, uID: String) {
        viewModelScope.launch {
            mainRepository.setDirectionDescription(text, uID)
        }

    }

    fun setDateAndTimeTaskStart(
        selectedDate: Long,
        directionId: String,
        taskId: String,
        isStart: Boolean
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

    fun getDirectionTasks(monitored: Boolean, direction: Direction) {
        viewModelScope.launch {
            taskGetState = if (monitored) {
                changeCurrentDirection(direction = direction)
                _uiState.update { currentState ->
                    currentState.copy(
                        isCurrentDirectionMonitored = true
                        )
                }
                mainRepository.getDirectionTasks(direction.uid)
            } else {
                changeCurrentDirection(direction)
                _uiState.update { currentState ->
                    currentState.copy(
                        isCurrentDirectionMonitored = false
                    )
                }
                mainRepository.getMonitoredDirectionTasks(direction.uid, direction.userId)
            }

        }

    }

    fun addCustomDirection() {
        viewModelScope.launch {
            mainRepository.addCustomDirection()
        }
    }

    fun addCustomTask(direction: Direction) {
        viewModelScope.launch {
            mainRepository.addCustomTask(direction = direction)
        }
    }

    private fun generateUniqueIntId(): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val randomNumber = Random().nextInt(Int.MAX_VALUE) // Generate random int within int range

        // Ensure both operands have the same type (long) before XOR
        val combinedLong = currentTimeMillis.toLong() xor randomNumber.toLong()

        // Take the least significant bits to get an int within the desired range
        return (combinedLong and Int.MAX_VALUE.toLong()).toInt()
    }

    @SuppressLint("ScheduleExactAlarm")
    fun deleteNotification(
        id: Int,
        title: String,
        description: String,
        taskId: String,
        collectionId: String,
        active: Boolean,
        localContext: Context,
        start: Boolean
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context.applicationContext, Notification::class.java)
        intent.action = "ALARM_ACTION"

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, description)
        intent.putExtra(notificationID, id)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)

        viewModelScope.launch {
            mainRepository.cancelNotification(taskId, collectionId, start)
            mainRepository.isStartNotificationActiveChange(taskId, collectionId, active)
        }

        AlertDialog.Builder(localContext)
            .setTitle("Уведомление удалено")
            .setMessage(
                "Название: $title\nОписание: $description"
            )
            .setPositiveButton("Ok") { _, _ -> }
            .show()

    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(
        time: Long,
        title: String,
        description: String,
        localContext: Context,
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
        //Передача намерения alarmManager в назначенное время
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        showAlert("Уведомление назначено", time, title, description, localContext)
    }

    private fun showAlert(
        alert: String,
        time: Long,
        title: String,
        message: String,
        localContext: Context
    ) {
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)
        AlertDialog.Builder(localContext)
            .setTitle(alert)
            .setMessage(
                "Название: $title\nОписание: $message\nВремя: ${dateFormat.format(time)} ${
                    timeFormat.format(
                        time
                    )
                }"
            )
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    fun checkNotificationPermissions(): Boolean {
        // Check if notification permissions are granted
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isEnabled = notificationManager.areNotificationsEnabled()
        if (!isEnabled) {
            // Open the app notification settings if notifications are not enabled
//            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
//            context.startActivity(intent)
            return false
        }
        return true
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)
                val mainRepository = application.container.mainRepository
                val appContext by lazy { application.applicationContext }
                DirectionListViewModel(
                    mainRepository = mainRepository,
                    context = appContext
                )
            }
        }
    }
}
