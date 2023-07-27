package com.example.brewbuddy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.brewbuddy.access.AccessViewModel

val LocalViewModel = staticCompositionLocalOf<AccessViewModel?> {
    null // Provide the default value here, or handle null as needed.
}

@Composable
fun ProvideViewModelContent(viewModel: AccessViewModel?, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalViewModel provides viewModel) {
        content()
    }
}