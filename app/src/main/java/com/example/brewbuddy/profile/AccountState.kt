package com.example.brewbuddy.profile

data class AccountState(
    val isLoading: Boolean = false,
    val success: Boolean? = null,
    val error: String = "",
)
