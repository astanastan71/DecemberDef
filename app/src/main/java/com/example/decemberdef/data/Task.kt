package com.example.decemberdef.data

import com.google.firebase.Timestamp
import java.util.Date

data class Task(
    val title: String = "",
    val uid: String = "no id",
    val description: String = "",
    val completed: Boolean = false,
    val imgUrl: String = "",
    val timeStart: Timestamp = Timestamp.now(),
    val timeEnd: Timestamp = Timestamp(Date(4076312924000))
)