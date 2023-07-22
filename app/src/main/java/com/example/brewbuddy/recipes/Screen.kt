package com.example.brewbuddy.recipes

sealed class Screen(val route: String) {
    object IndividualRecipeScreen: Screen("Recipes")

    object MarketplaceItemScreen: Screen("items")
}
