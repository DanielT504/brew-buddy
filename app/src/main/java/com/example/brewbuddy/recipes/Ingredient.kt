package com.example.brewbuddy.recipes;

data class IngredientComposition (
    val quantities: List<String>,
    val subIngredientDetails: List<String>,
)
data class Ingredient (
    val ingredientName: String,
    val ingredientComposite: IngredientComposition
)
