package com.example.brewbuddy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.InterFont


@Composable
fun CardWithIconAndTitle(modifier: Modifier) {
    Card(modifier) {
        val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
        val user = currentUserViewModel.getUser()
        val username = user!!.username
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Filled.Face, contentDescription = "Localized description")
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = username,
//                style = InterFont,
            )
        }
    }
}


@Composable
fun ProfileScreen(
) {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val user = currentUserViewModel.getUser()
    val username = user!!.username
//    Surface(modifier = Modifier.fillMaxSize()) {
//        Text("Welcome, $username")
//    }

    CardWithIconAndTitle(modifier = Modifier.fillMaxSize())
}