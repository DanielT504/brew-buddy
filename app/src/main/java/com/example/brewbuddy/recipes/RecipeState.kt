package com.example.brewbuddy.recipes

import com.example.brewbuddy.domain.model.Recipe

data class RecipeState (
    val isLoading: Boolean = false,
    val recipe: Recipe? = null,
    val error: String = "",
)