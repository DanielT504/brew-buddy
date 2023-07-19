package com.example.brewbuddy.featured

import com.example.brewbuddy.domain.model.RecipeMetadata

data class FeaturedState(
    val isRecipesLoading: Boolean = false,
    val recipes: List<RecipeMetadata> = emptyList(),
    val recipesError: String = "",
    val isPopularLoading: Boolean = false,
    val popular: List<RecipeMetadata> = emptyList(),
    val popularError: String = "",
)
