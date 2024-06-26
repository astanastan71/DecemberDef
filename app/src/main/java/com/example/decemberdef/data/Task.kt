package com.example.decemberdef.data

import com.google.firebase.Timestamp

data class Task(
    val title: String = "Без названия",
    var uid: String = "no id",
    val description: String = "",
    val completed: Boolean = false,
    val imgUrl: String = "",
    val timeStart: Timestamp = Timestamp.now(),
    val timeEnd: Timestamp = Timestamp.now(),
    val notificationStartId: Int = 0,
    val notificationEndId: Int = 0,
    val startNotificationActive: Boolean = false,
    val endNotificationActive: Boolean = false,
    val directionId: String = "",
    var directionName: String = "",
    val timeCreated: Timestamp = Timestamp.now(),
    val continued: Boolean = false
)