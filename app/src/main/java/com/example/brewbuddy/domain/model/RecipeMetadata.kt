package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants

data class RecipeMetadata(
    override val id: String,
    override val title: String = "Untitled",
    override val bannerUrl: String = Constants.DEFAULT_BANNER_URL,
    override val author: Author = Author(),
    val tags: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
    val likes: Int = 0
): PostMetadata()