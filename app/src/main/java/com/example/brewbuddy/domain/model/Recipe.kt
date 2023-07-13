package com.example.brewbuddy.domain.model

import com.example.brewbuddy.data.remote.dto.AnalyzedInstruction
import com.example.brewbuddy.data.remote.dto.ExtendedIngredient

data class Recipe(
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var glutenFree: Boolean = false,
    var dairyFree: Boolean = false,
    var veryHealthy: Boolean = false,
    var cheap: Boolean = false,
    var veryPopular: Boolean = false,
    var sustainable: Boolean = false,
    var lowFodmap: Boolean = false,
    var weightWatcherSmartPoints: Int = 0,
    var gaps: String? = null,
    var preparationMinutes: Int = 0,
    var cookingMinutes: Int = 0,
    var aggregateLikes: Int = 0,
    var healthScore: Int = 0,
    var sourceName: String? = null,
    var pricePerServing: Double = 0.0,
    var extendedIngredients: List<ExtendedIngredient>? = null,
    var id: Int = 0,
    var title: String? = null,
    var readyInMinutes: Int = 0,
    var servings: Int = 0,
    var sourceUrl: String? = null,
    var image: String? = null,
    var imageType: String? = null,
    var summary: String? = null,
    var cuisines: List<Any>? = null,
    var dishTypes: List<String>? = null,
    var diets: List<String>? = null,
    var instructions: String? = null,
    var analyzedInstructions: List<AnalyzedInstruction>? = null,
    var spoonacularSourceUrl: String? = null
)
