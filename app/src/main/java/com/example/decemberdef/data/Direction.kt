package com.example.decemberdef.data

import com.google.firebase.Timestamp

data class Direction(
    val title: String = "Без названия",
    val description: String = "",
    val imgURL: String = "",
    var isDone: Boolean = false,
    var uid: String = "no id",
    val progress: Int = 0,
    val count: Int = 0,
    val shared: Boolean = false,
    var userId: String = "",
    var monitored: Boolean = false,
    val timeCreated: Timestamp = Timestamp.now(),
    var monitoredLinkId: String = "",
    val lastEdited: Timestamp = Timestamp.now()
)