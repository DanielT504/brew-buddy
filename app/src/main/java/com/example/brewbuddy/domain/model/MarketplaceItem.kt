package com.example.brewbuddy.domain.model


data class MarketplaceItem (
    val id: String,
    val imageUrl: String,
    val description: String,
    val title: String,
    val contact: String,
    val keywords: List<String>,
    val city: String,
    val province: String,
    val tags: List<String>,
    val price: Number,
    val author: Author
)