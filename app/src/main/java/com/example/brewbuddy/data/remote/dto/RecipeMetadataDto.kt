package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata

data class RecipeMetadataDto(
    val id: String,
    val title: String,
    val bannerUrl: String,
    val author: AuthorDto,
    val tags: List<String>,
    val keywords: List<String>,
    val likes: Int,
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = RecipeMetadataDto(
                id=map["id"] as? String ?: "",
                title=map["title"] as? String ?: "Untitled",
                bannerUrl=map["bannerUrl"] as? String ?: Constants.DEFAULT_BANNER_URL,
                author=AuthorDto.from(map["author"] as? HashMap<String, Object> ?: hashMapOf()),
                tags=map["tags"] as? List<String> ?: emptyList(),
                keywords=map["keywords"] as? List<String> ?: emptyList(),
                likes=map["likes"] as? Int ?: 0
            )
        }.data
    }
}

fun RecipeMetadataDto.toRecipeMetadata(): RecipeMetadata {
    return RecipeMetadata(
        id = id,
        bannerUrl = bannerUrl,
        title = title,
        author=author.toAuthor(),
        tags=tags,
        keywords=keywords,
        likes=likes
    )
}