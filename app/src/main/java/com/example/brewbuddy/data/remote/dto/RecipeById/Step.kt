package com.example.brewbuddy.data.remote.dto.RecipeById

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)