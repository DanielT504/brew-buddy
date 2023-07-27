package com.example.brewbuddy.profile

import com.example.brewbuddy.templates.ResultState

data class UserState<T> (
    override val isLoading: Boolean = false,
    override val error: String = "",
    val data: T? = null,
): ResultState()
