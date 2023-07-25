package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author

data class MarketplaceItemMetadata (
    override val author: Author = Author(),
    override val id: String = "",
    val city: String = "",
    override val title: String = "",
    val price: Number = 0,
    val province: String = "",
    val tags: List<String> = emptyList(),
    override val bannerUrl: String = Constants.DEFAULT_IMAGE_URL
): PostMetadata()