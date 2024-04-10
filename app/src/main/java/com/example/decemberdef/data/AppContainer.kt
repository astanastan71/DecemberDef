package com.example.decemberdef.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

interface AppContainer {
    val mainRepository: MainRepository
}

class DefaultAppContainer : AppContainer {

    val auth = Firebase.auth

    private val db = Firebase.firestore

    private val user = Firebase.auth.currentUser

    init {
        auth.addAuthStateListener {
            mainRepository.updateUser(it.currentUser)
        }
    }



    override val mainRepository: MainRepository by lazy {
        DefaultMainRepository(auth, db, user)
    }


}