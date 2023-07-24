package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.RecipeMetadata

data class AuthorDto(
    var id:  String,
    var username: String,
    var avatarUrl: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = AuthorDto(
                id=map["uid"] as? String ?: "",
                username=map["username"] as? String ?: "unknown",
                avatarUrl=map["avatarUrl"] as? String ?: Constants.DEFAULT_AVATAR_URL,
            )
        }.data
    }

}

fun AuthorDto.toAuthor(): Author {
    return Author(
        id = id,
        username = username,
        avatarUrl = avatarUrl,
    )
}