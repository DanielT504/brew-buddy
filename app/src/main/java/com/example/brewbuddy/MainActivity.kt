package com.example.brewbuddy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.brewbuddy.access.AccessScreen
import com.example.brewbuddy.profile.CurrentUserRepository
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var isLoggedIn = MutableLiveData(false)
    private var currentUserRepository=CurrentUserRepository()
    fun setLogin(bool: Boolean) {
        isLoggedIn.value = bool;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseMessaging = FirebaseMessaging.getInstance()
        auth = Firebase.auth
        firebaseMessaging.subscribeToTopic("Stores")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
            return
        }

        isLoggedIn.observe(this, Observer {
            Log.d("MainActivity", "Log In: ${it}")
            if (it) {
                setContent {
                    BrewBuddyTheme {
                        MainScreen(this)
                    }
                }
            } else {
                setContent {
                    BrewBuddyTheme {
                        AccessScreen(currentUserRepository, this)
                    }
                }
            }
        })
    }
}