package com.example.decemberdef.data

data class Direction(
    val title: String = "Без названия",
    val description: String = "",
    val imgURL: String = "",
    val isDone: Boolean = false,
    val uid: String = "no id",
    val progress: Int = 0
)
//) {
//    val getProgress: Int
//        get() = 100 / (listOfTasks.count() / listOfTasks.count(){ it.isCompleted })
//
//}