package com.example.brewbuddy.domain.model

data class PostState<T>(
    val isLoading: Boolean = false,
    val post: T? = null,
    val error: String = "",
)