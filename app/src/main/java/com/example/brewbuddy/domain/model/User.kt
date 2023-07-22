package com.example.brewbuddy.domain.model

data class User(
    val id: String,
    val likedRecipeIds: List<String>,
    val savedRecipeIds: List<String>,
)
