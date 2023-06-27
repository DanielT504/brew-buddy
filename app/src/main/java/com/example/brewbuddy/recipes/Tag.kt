package com.example.brewbuddy.recipes

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class TagType (
    val iconTint: Color,
    val tagText: String,
    val tagColor: Color,
    @DrawableRes val  img: Int,
)