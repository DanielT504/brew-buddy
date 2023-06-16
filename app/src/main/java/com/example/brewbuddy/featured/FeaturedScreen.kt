package com.example.brewbuddy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeaturedScreen(
    name: String
) {
    Surface(modifier = Modifier.fillMaxSize(), color= MaterialTheme.colorScheme.background) {
        Greeting(name)
    }

}
