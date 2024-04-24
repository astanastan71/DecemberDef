package com.example.decemberdef.data

import android.net.Uri

data class User(
    val userID: String = "Not found",
    val isEmailVerified: Boolean = false,
    val isAnon: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val userPhoto: Uri? = null
)