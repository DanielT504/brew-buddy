package com.example.brewbuddy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.brewbuddy.profile.CurrentUserViewModel

val LocalViewModel = staticCompositionLocalOf<CurrentUserViewModel?> {
    null // Provide the default value here, or handle null as needed.
}

@Composable
fun ProvideViewModelContent(viewModel: CurrentUserViewModel?, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalViewModel provides viewModel) {
        content()
    }
}