package com.example.decemberdef.data

import com.google.firebase.Timestamp
import java.util.Date

data class Task(
    val title: String = "Без названия",
    var uid: String = "no id",
    val description: String = "",
    val completed: Boolean = false,
    val imgUrl: String = "",
    val timeStart: Timestamp = Timestamp.now(),
    val timeEnd: Timestamp = Timestamp(Date(4076312924000)),
    val notificationStartId: Int = 0,
    val notificationEndId: Int = 0,
    val startNotificationActive: Boolean = false,
    val endNotificationActive: Boolean = false,
)