package com.example.brewbuddy.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.*
import com.example.brewbuddy.profile.User
import kotlinx.coroutines.launch


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
        val user = User(username, email)
        setUser(user)
    }

    fun removeUser() {
        setUser(null)
    }

    fun registerUser(username: String, email: String): Boolean {
        val user = User(username, email);
        setUser(user);
        return true;
    }

    fun registerUserWithGoogle(username: String, email: String): Boolean {
        val user = User(username, email);
        setUser(user);
        return true;
    }
}