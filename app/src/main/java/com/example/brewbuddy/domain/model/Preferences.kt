package com.example.brewbuddy.domain.model

data class Preferences(
    val keto: Boolean = false,
    val dairyFree: Boolean = false,
    val glutenFree: Boolean = false,
    val kosher: Boolean = false,
    val halal: Boolean = false,
    val vegetarian: Boolean = false,
    val nutFree: Boolean = false,
    val vegan: Boolean = false,
    val radius: Float = 0f,
    val ingredients: List<String> = emptyList()
)
