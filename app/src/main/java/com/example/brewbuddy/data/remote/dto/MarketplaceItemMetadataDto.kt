package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.MarketplaceItemMetadata

data class MarketplaceItemMetadataDto(
    val id: String = "",
    val author: AuthorDto,
    val city: String,
    val title: String,
    val price: Number,
    val province: String,
    val tags: List<String>,
    val bannerUrl: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = MarketplaceItemMetadataDto(
                id=map["id"] as? String ?: "",
                author=AuthorDto.from(map["author"] as? HashMap<String, Object> ?: hashMapOf()),
                city=map["city"] as? String ?: "",
                title=map["title"] as? String ?: "Untitled Listing",
                price=map["price"] as? Number ?: 0,
                province=map["province"] as? String ?: "",
                tags=map["tags"] as? List<String> ?: emptyList(),
                bannerUrl=map["imageUrl"] as? String ?: Constants.DEFAULT_IMAGE_URL
            )
        }.data
    }
}

fun MarketplaceItemMetadataDto.toMarketplaceItemMetadata(): MarketplaceItemMetadata {
    return MarketplaceItemMetadata(
        id=id,
        author=author.toAuthor(),
        city=city,
        title=title,
        price=price,
        province=province,
        tags=tags,
        bannerUrl=bannerUrl
    )
}