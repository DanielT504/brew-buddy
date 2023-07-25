package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author

data class MarketplaceItemMetadata (
    val author: Author = Author(),
    val id: String = "",
    val city: String = "",
    val title: String = "",
    val price: Number = 0,
    val province: String = "",
    val tags: List<String> = emptyList(),
    val imageUrl: String = Constants.DEFAULT_IMAGE_URL
)