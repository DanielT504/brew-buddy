package com.example.brewbuddy.recipes

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.brewbuddy.R
import com.example.brewbuddy.common.TagType
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.GreenMedium

data class RecipeTagConfig (
    val iconTint: Color,
    val tagColor: Color
)

val RecipeTagConfigs = hashMapOf(
    TagType.ALLERGEN to RecipeTagConfig(tagColor = GreenMedium, iconTint = Color.White),
    TagType.DIET to RecipeTagConfig(tagColor = Brown, iconTint = Color.White),
)

data class RecipeTagInfo (
    val type: TagType,
    val name: String,
    val label: String,
    @DrawableRes val icon: Int
)

data class RecipeTagDto(
    val iconTint: Color,
    val tagText: String,
    val tagName: String,
    val tagColor: Color,
    @DrawableRes val  img: Int,
)

val RecipeTagList = listOf(
    RecipeTagInfo(TagType.ALLERGEN, "glutenFree", "Gluten Free", R.drawable.icon_custom_gluten_free),
    RecipeTagInfo(TagType.ALLERGEN, "dairyFree", "Dairy Free", R.drawable.icon_info),
    RecipeTagInfo(TagType.ALLERGEN, "nutFree", "Nut Free", R.drawable.icon_info),
    RecipeTagInfo(TagType.DIET, "keto", "Keto", R.drawable.icon_info),
    RecipeTagInfo(TagType.DIET, "vegan", "Vegan", R.drawable.icon_custom_vegan),
    RecipeTagInfo(TagType.DIET, "vegetarian", "Vegetarian", R.drawable.icon_custom_vegetarian),
    RecipeTagInfo(TagType.DIET, "halal", "Halal", R.drawable.icon_info),
    RecipeTagInfo(TagType.DIET, "kosher", "Kosher", R.drawable.icon_info)
)

fun createTag(tagInfo: RecipeTagInfo): RecipeTagDto {
    val tagConfig = RecipeTagConfigs[tagInfo.type]
    return RecipeTagDto(tagConfig!!.iconTint, tagInfo.label, tagInfo.name, tagConfig!!.tagColor, tagInfo.icon)
}