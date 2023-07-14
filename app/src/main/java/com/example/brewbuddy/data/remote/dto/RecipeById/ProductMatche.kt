package com.example.brewbuddy.data.remote.dto.RecipeById

data class ProductMatche(
    val averageRating: Int,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val link: String,
    val price: String,
    val ratingCount: Int,
    val score: Double,
    val title: String
)