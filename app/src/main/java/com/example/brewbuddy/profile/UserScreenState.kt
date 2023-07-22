package com.example.brewbuddy.profile

import com.example.brewbuddy.domain.model.RecipeMetadata

data class UserScreenState (
    val isLoading: Boolean = false,
    val data: List<RecipeMetadata> = emptyList(),
    val error: String = "",
)
