package com.example.brewbuddy.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.*
import com.example.brewbuddy.profile.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.brewbuddy.common.Constants


// global state view model to get currently logged in user.
// if this is null, then we are looking at the login screen
class CurrentUserViewModel : ViewModel() {
    val currentUser =  MutableLiveData<User?>()

    fun setUser(user: User?) {
        currentUser.postValue(user);
    }

    fun getUser(): User {
        return currentUser.value!!
    }

    fun loginUser(username: String, email: String) {
        val user = User(username, email, null, null, "")
        setUser(user)
    }

    fun removeUser() {
        setUser(null)
    }

    fun registerUser(username: String, email: String): Boolean {
        val user = User(username, email, null, null, "");
        setUser(user);
        return true;
    }

    suspend fun registerUserWithGoogle(context: Context, username: String, email: String): Boolean {
        return try {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account == null) {
                return false
            }

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            val currentUser = authResult.user

            if (currentUser != null) {
                val user = User(
                    username = username,
                    email = email,
                    bannerUrl = Constants.DEFAULT_BANNER_URL,
                    avatarUrl = currentUser.photoUrl.toString(),
                    userId = currentUser.uid
                )
                setUser(user)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error signing in with Google: ${e.message}")
            false
        }
    }
}