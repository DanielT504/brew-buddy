package com.example.brewbuddy

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    // initializes the viewmodel for the currently logged in user and attaches it to the
    // scope of the main activity
    private val currentUserViewModel: CurrentUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            BrewBuddyApp(currentUserViewModel, this)
        }

        currentUserViewModel.setUser(null)

        // observe changes in the current user. if the current user is null (logged out)
        // change screen to login
        currentUserViewModel.currentUser.observe(this, Observer {
            if (it != null) {
                // User is logged in
                currentUserViewModel.setUser(it)
            } else {
                // User is logged out
                currentUserViewModel.setUser(null)
            }
        })
    }
}

@Composable
fun BrewBuddyApp(currentUserViewModel: CurrentUserViewModel, activity: Activity) {
    val navController = rememberNavController()

    val startDestination = if (currentUserViewModel.getUser() != null) {
        "main"
    } else {
        "access"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("access") {
            AccessScreen(activity)
        }
        composable("main") {
            MainScreen(currentUserViewModel, navController)
        }
    }
}

