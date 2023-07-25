package com.example.brewbuddy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.CurrentUserRepository
import com.example.brewbuddy.profile.LoginUserViewModel
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginUserViewModel: LoginUserViewModel by viewModels()
    private val currentUserRepository = CurrentUserRepository()
    private lateinit var auth: FirebaseAuth
    private var isLoggedIn = MutableLiveData(false)

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
//        setContent {
//            BrewBuddyTheme {
//                val navController = rememberNavController()
//                AccessScreen(navController, currentUserRepository, currentUserViewModel, this, ::handleLogout)
//            }
//        }
//        currentUserViewModel.setUser(null)
//
        // observe changes in the current user. if the current user is null (logged out)
        // change screen to login
        isLoggedIn.observe(this, Observer {
            Log.d("MainActivity", "Log In: ${it}")
            if (it) {
                setContent {
                    BrewBuddyTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    BrewBuddyTheme {
                        AccessScreen(
                            currentUserRepository,
                            this
                        )
                    }
                }
            }
        })
    }
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            isLoggedIn = true;
//            setContent {
//                BrewBuddyTheme {
//                    MainScreen()
//                }
//            }
//        } else {
//            setContent {
//                BrewBuddyTheme {
//                    val navController = rememberNavController()
//                    AccessScreen(navController, currentUserRepository, loginUserViewModel, this, ::handleLogout)                    }
//            }
//        }
//    }
    // Function to handle logout and app restart
    private fun handleLogout() {
        // Clear the back stack and start the MainActivity again to simulate a restart
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}