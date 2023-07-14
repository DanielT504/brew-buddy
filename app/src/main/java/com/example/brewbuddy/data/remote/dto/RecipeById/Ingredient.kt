package com.example.brewbuddy.data.remote.dto.RecipeById

data class Ingredient(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String
)