package com.example.decemberdef.data

data class Direction(
    val title: String = "Без названия",
    val description: String = "",
    val imgURL: String = "",
    var isDone: Boolean = false,
    var uid: String = "no id",
    val progress: Int = 0,
    val count: Int = 0,
    val shared: Boolean = false
)