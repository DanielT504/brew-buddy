package com.example.brewbuddy.featured

import com.example.brewbuddy.domain.model.RecipeMetadata

data class FeaturedState(
    val isLoading: Boolean = false,
    val data: List<RecipeMetadata> = emptyList(),
    val error: String = "",
)
