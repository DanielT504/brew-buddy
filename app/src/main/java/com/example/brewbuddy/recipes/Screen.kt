package com.example.brewbuddy.recipes

sealed class Screen(val route: String) {
    object RecipesScreen: Screen("recipes_screen")
    object IndividualRecipeScreen: Screen("Recipes")
}
