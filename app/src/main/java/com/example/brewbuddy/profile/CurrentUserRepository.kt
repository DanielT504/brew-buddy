package com.example.brewbuddy.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CurrentUserRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun deleteAccount(uid: String): Boolean {
        val user = auth.currentUser
        if (user != null) {
            try {
                user.delete()

                val userDocRef = firestore.collection("users").document(uid)
                userDocRef.delete()

                return true
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }
}





