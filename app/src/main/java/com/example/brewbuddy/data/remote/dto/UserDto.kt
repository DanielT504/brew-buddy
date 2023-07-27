package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.User

data class UserDto(
    val id: String,
    val username: String,
    val bannerUrl: String,
    val avatarUrl: String,
    val likedRecipeIds: List<String>,
    val email: String,
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = UserDto(
                id=map["id"] as? String ?: "",
                username=map["username"] as? String ?: "unknown",
                bannerUrl=map["bannerUrl"] as? String ?: Constants.DEFAULT_BANNER_URL,
                avatarUrl=map["avatarUrl"] as? String ?: Constants.DEFAULT_AVATAR_URL,
                likedRecipeIds=map["likedRecipeIds"] as? List<String> ?: emptyList(),
                email=map["email"] as? String ?: ""
            )
        }.data
    }
}

fun UserDto.toUser(): User {
    return User(
        id = id,
        bannerUrl = bannerUrl,
        username = username,
        avatarUrl=avatarUrl,
        likedRecipeIds=likedRecipeIds,
        email=email,
    )
}