package com.example.brewbuddy.domain.model;

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.Step

data class Author(
    var id:  String = "",
    var username: String = "unknown",
    var avatarUrl: String = Constants.DEFAULT_AVATAR_URL
)