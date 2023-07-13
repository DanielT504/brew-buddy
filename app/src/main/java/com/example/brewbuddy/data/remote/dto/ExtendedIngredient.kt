package com.example.brewbuddy.data.remote.dto

data class ExtendedIngredient(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: Measures,
    val meta: List<Any>,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val unit: String
)