package com.example.brewbuddy.profile

import com.example.brewbuddy.domain.model.RecipeMetadata

data class UserScreenState<T> (
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String = "",
)
