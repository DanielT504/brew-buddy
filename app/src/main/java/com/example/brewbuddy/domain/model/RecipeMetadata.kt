package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants

data class RecipeMetadata(
    val id: String? = null,
    val title: String? = "Unnamed Recipe",
    val bannerUrl: String? = Constants.DEFAULT_BANNER_URL,
)