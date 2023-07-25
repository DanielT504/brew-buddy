package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants

data class User(
    val id: String = "",
    val likedRecipeIds: List<String> = emptyList(),
    val savedRecipeIds: List<String> = emptyList(),
    val username: String = "unknown",
    val bannerUrl: String = Constants.DEFAULT_BANNER_URL,
    val avatarUrl: String = Constants.DEFAULT_AVATAR_URL,
    val email: String = "",
)
