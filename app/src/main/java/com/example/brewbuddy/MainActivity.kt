package com.example.brewbuddy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.brewbuddy.profile.CurrentUserRepository
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // initializes the viewmodel for the currently logged in user and attaches it to the
    // scope of the main activity
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            BrewBuddyTheme {
                AccessScreen(this, ::handleLogout) // Pass the handleLogout function to AccessScreen
            }
        }
        currentUserViewModel.setUser(null)

        // observe changes in the current user. if the current user is null (logged out)
        // change screen to login
        currentUserViewModel.currentUser.observe(this, Observer {
            Log.d("ACTIVITY", it.toString())
            if (it != null) {
                setContent {
                    BrewBuddyTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    BrewBuddyTheme {
                        AccessScreen(this, ::handleLogout) // Pass the handleLogout function to AccessScreen
                    }
                }
            }
        })
    }

    // Function to handle logout and app restart
    private fun handleLogout() {
        // Clear the back stack and start the MainActivity again to simulate a restart
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}