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

class CurrentUserViewModel : ViewModel() {
    val currentUser =  MutableLiveData<User?>()

    fun setUser(user: User?) {
        currentUser.postValue(user);
    }

    fun getUser(): User? {
        return currentUser.value
    }
}