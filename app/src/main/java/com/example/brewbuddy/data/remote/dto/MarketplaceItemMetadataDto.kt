package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.domain.model.MarketplaceItemMetadata

data class MarketplaceItemMetadataDto(
    val authorId: String,
    val city: String,
    val postTitle: String,
    val price: Number,
    val province: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = MarketplaceItemMetadataDto(
                authorId=map["authorId"] as? String ?: "",
                city=map["city"] as? String ?: "",
                postTitle=map["postTitle"] as? String ?: "Untitled Listing",
                price=map["price"] as? Number ?: 0,
                province=map["province"] as? String ?: ""
            )
        }.data
    }
}

fun MarketplaceItemMetadataDto.toMarketplaceItemMetadata(): MarketplaceItemMetadata {
    return MarketplaceItemMetadata(
        authorId=authorId,
        city=city,
        postTitle=postTitle,
        price=price,
        province=province
    )
}