package com.example.decemberdef.ui

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings

class CreateDataFire {
    private val db = Firebase.firestore

    fun createCollection() {
        val user = hashMapOf(
            "first" to "aoba",
            "last" to "Lovelace",
            "born" to 1815,
        )
        val userTwo = hashMapOf(
            "first" to "allanad",
            "last" to "Turing",
            "born" to 1912,
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        db.collection("users")
            .add(userTwo)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun readCollection() {
        db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

}