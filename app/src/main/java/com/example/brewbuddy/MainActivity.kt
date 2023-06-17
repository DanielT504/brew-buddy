package com.example.brewbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.BrewBuddyTheme


class MainActivity : ComponentActivity() {
    private val currentUserViewModel: CurrentUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewBuddyTheme {
                LoginScreen()
            }
        }
        currentUserViewModel.setUser(null)

        currentUserViewModel.currentUser.observe(this, Observer {
            if(it != null) {
                setContent {
                    BrewBuddyTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    BrewBuddyTheme {
                        LoginScreen()
                    }
                }
            }
        })
    }

}

