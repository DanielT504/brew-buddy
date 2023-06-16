package com.example.brewbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.brewbuddy.ui.theme.BrewBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewBuddyTheme {
                LoginScreen(loginUser = ::login)
            }
        }
    }

    fun login(username: String, password: String) {
        setContent{
            BrewBuddyTheme {
                MainScreen()
            }
        }
    }
}

