package com.example.brewbuddy.recipes

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class TagType (
    val iconTint: Color,
    val tagText: String,
    val tagColor: Color,
    @DrawableRes val  img: Int,
)

data class Tag (
    val tag: Color,
    val label: String,
)

val GlutenFreeTag = Tag(

)