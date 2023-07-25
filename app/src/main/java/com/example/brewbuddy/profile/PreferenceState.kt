package com.example.brewbuddy.profile

import com.example.brewbuddy.domain.model.Preferences
import com.example.brewbuddy.templates.ResultState

data class PreferenceState(
    override val isLoading: Boolean = false,
    override val error: String = "",
    val data: Preferences = Preferences()
): ResultState()
