package com.example.brewbuddy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.shoplocator.storeNotif
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // initializes the viewmodel for the currently logged in user and attaches it to the
    // scope of the main activity
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseMessaging = FirebaseMessaging.getInstance()
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
        setContent {
            BrewBuddyTheme {
                AccessScreen(this)
            }
        }
        currentUserViewModel.setUser(null)

        // observe changes in the current user. if the current user is null (logged out)
        // change screen to login
        currentUserViewModel.currentUser.observe(this, Observer {
            Log.d("ACTIVITY", it.toString())
            if(it != null) {
                setContent {
                    BrewBuddyTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    BrewBuddyTheme {
                        AccessScreen(this)
                    }
                }
            }
        })
    }
}

