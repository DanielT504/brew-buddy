package com.example.brewbuddy.recipes

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.brewbuddy.R
import com.example.brewbuddy.common.TagType
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium

data class TagConfig (
    val iconTint: Color,
    val tagColor: Color
)

val TagConfigs = hashMapOf(
    TagType.ALLERGEN to TagConfig(tagColor = GreenMedium, iconTint = Color.White),
    TagType.DIET to TagConfig(tagColor = Brown, iconTint = Color.White),
)

data class TagInfo (
    val type: TagType,
    val name: String,
    val label: String,
    @DrawableRes val icon: Int
)

data class TagDto(
    val iconTint: Color,
    val tagText: String,
    val tagName: String,
    val tagColor: Color,
    @DrawableRes val  img: Int,
)

val TagList = listOf(
    TagInfo(TagType.ALLERGEN, "glutenFree", "Gluten Free", R.drawable.icon_custom_gluten_free),
    TagInfo(TagType.ALLERGEN, "dairyFree", "Dairy Free", R.drawable.icon_info),
    TagInfo(TagType.ALLERGEN, "nutFree", "Nut Free", R.drawable.icon_info),
    TagInfo(TagType.DIET, "keto", "Keto", R.drawable.icon_info),
    TagInfo(TagType.DIET, "vegan", "Vegan", R.drawable.icon_custom_vegan),
    TagInfo(TagType.DIET, "vegetarian", "Vegetarian", R.drawable.icon_custom_vegetarian),
    TagInfo(TagType.DIET, "halal", "Halal", R.drawable.icon_info),
    TagInfo(TagType.DIET, "kosher", "Kosher", R.drawable.icon_info)
)

fun createTag(tagInfo: TagInfo): TagDto {
    val tagConfig = TagConfigs[tagInfo.type]
    return TagDto(tagConfig!!.iconTint, tagInfo.label, tagInfo.name, tagConfig!!.tagColor, tagInfo.icon)
}