package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.data.remote.dto.IngredientList
import com.example.brewbuddy.data.remote.dto.Instructions

data class Recipe(
    var likes: Int = 0,
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false,
    var dairyFree: Boolean = false,
    var sustainable: Boolean = false,
    var preparationMinutes: Int = 0,
    var id: String? = null,
    var title: String = "Unnamed Recipe",
    var servings: Int = 0,
    var summary: String? = null,
    var diets: List<String> = emptyList(),
    var instructions: List<Instructions> = emptyList(),
    var bannerUrl: String = Constants.DEFAULT_BANNER_URL,
    val ingredientLists: List<IngredientList> = emptyList(),
    val author: Author? = null,
    val tags: List<String> = emptyList(),
    val keywords: List<String> = emptyList()
    )
