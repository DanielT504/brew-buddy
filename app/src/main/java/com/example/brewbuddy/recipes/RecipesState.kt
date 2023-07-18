package com.example.brewbuddy.recipes

import com.example.brewbuddy.domain.model.RecipeMetadata

data class RecipesState(
    val isLoading: Boolean = false,
    val recipes: List<RecipeMetadata> = emptyList(),
    val error: String = "",
)
