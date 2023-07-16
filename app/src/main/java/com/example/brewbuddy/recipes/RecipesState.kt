package com.example.brewbuddy.recipes

import com.example.brewbuddy.domain.model.Recipe

data class RecipesState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String = "",
)
