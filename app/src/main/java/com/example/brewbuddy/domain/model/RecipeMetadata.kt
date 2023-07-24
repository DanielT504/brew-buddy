package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants

data class RecipeMetadata(
    val id: String,
    val title: String = "Untitled",
    val bannerUrl: String = Constants.DEFAULT_BANNER_URL,
    val author: Author = Author(),
    val tags: List<String> = emptyList()
)