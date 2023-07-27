package com.example.brewbuddy.access

import com.example.brewbuddy.templates.ResultState

data class AccessState(
    override val isLoading: Boolean = false,
    val success: Boolean? = null,
    override val error: String = "",
): ResultState()
