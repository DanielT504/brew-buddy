package com.example.brewbuddy.domain.model;

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.Step

data class Author(
    var id:  String? = null,
    var username: String? = "Unknown",
    var avatarUrl: String? = Constants.DEFAULT_AVATAR_URL
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Author(
                id=map["uid"] as String,
                username=map["username"] as String,
                avatarUrl=map["avatarUrl"] as String,

                )
        }.data
    }

}
