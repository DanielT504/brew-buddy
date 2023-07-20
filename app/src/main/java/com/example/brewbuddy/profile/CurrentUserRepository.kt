package com.example.brewbuddy.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CurrentUserRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Function to delete the user account
    suspend fun deleteAccount(uid: String): Boolean {
        val user = auth.currentUser
        if (user != null) {
            try {
                user.delete()
                return true
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }
}





