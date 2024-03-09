package com.example.decemberdef.data

import com.example.decemberdef.ui.screens.listApp.TaskGetState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

interface AppContainer {
    val mainRepository: MainRepository
}

class DefaultAppContainer : AppContainer {

    val auth = Firebase.auth

    private val db = Firebase.firestore

    private val user = Firebase.auth.currentUser



    override val mainRepository: MainRepository by lazy {
        DefaultMainRepository(auth, db, user)
    }


}