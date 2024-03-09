package com.example.decemberdef.data

import android.net.Uri
import com.google.firebase.firestore.CollectionReference

data class User(
    val userID: String = "Not found",
    val isEmailVerified: Boolean = false,
    val isAnon: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhoto: Uri? = null
)