package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants


data class MarketplaceItem (
    val id: String = "",
    val bannerUrl: String = Constants.DEFAULT_IMAGE_URL,
    val description: String = "",
    val title: String = "Unnamed Listing",
    val contact: String = "",
    val keywords: List<String> = emptyList(),
    val city: String = "",
    val province: String = "",
    val tags: List<String> = emptyList(),
    val price: Number = -1,
    val author: Author = Author()
)