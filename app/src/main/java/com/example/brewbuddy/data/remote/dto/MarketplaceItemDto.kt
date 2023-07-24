package com.example.brewbuddy.data.remote.dto


import android.util.Log
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.MarketplaceItem
import com.example.brewbuddy.domain.model.Recipe

data class MarketplaceItemDto(
    val id: String,
    val imageUrl: String,
    val description: String,
    val title: String,
    val contact: String,
    val keywords: List<String>,
    val city: String,
    val province: String,
    val tags: List<String>,
    val author: AuthorDto,
    val price: Number,
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = MarketplaceItemDto(
                keywords=map["keywords"] as? List<String> ?: emptyList(),
                contact=map["contact"] as? String ?: "",
                city=map["city"] as? String ?: "",
                province=map["province"] as? String ?: "",
                author=AuthorDto.from(map["author"] as? HashMap<String, Object> ?: hashMapOf()),
                title=map["title"] as? String ?: "",
                id=map["id"] as? String ?: "",
                imageUrl=map["imageUrl"] as? String ?: Constants.DEFAULT_BANNER_URL,
                description=map["description"] as? String ?: "",
                tags=map["tags"] as? List<String> ?: emptyList(),
                price=map["price"] as? Number ?: 0
            )
        }.data
    }

}

fun MarketplaceItemDto.toMarketplaceItem(): MarketplaceItem {
    return MarketplaceItem(
        city = city,
        province = province,
        price = price,
        id = id,
        imageUrl = imageUrl,
        description = description,
        title = title,
        author=author.toAuthor(),
        tags=tags,
        contact=contact,
        keywords=keywords
    )
}