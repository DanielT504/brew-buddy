package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author

data class MarketplaceItemMetadata (
    val authorId: String,
    val city: String,
    val postTitle: String,
    val price: Number,
    val province: String
)