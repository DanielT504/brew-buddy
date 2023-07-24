package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata

data class RecipeMetadataDto(
    val id: String,
    val title: String,
    val bannerUrl: String,
    val author: Author,
    val tags: List<String>
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = RecipeMetadataDto(
                id=map["id"] as String,
                title=map["title"] as String,
                bannerUrl=map["bannerUrl"] as String,
                author=Author.from(map["author"] as HashMap<String, Object>),
                tags=map["tags"] as? List<String> ?: emptyList()
            )
        }.data
    }
}

fun RecipeMetadataDto.toRecipeMetadata(): RecipeMetadata {
    return RecipeMetadata(
        id = id,
        bannerUrl = bannerUrl,
        title = title,
        author=author,
        tags=tags
    )
}