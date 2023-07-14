package com.example.brewbuddy.data.remote.dto.RecipeById

import com.example.brewbuddy.domain.model.Recipe

data class RecipeByIdDto(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val cheap: Boolean,
    val cookingMinutes: Int,
    val creditsText: String,
    val cuisines: List<String>,
    val dairyFree: Boolean,
    val diets: List<String>,
    val dishTypes: List<String>,
    val extendedIngredients: List<ExtendedIngredient>,
    val gaps: String,
    val glutenFree: Boolean,
    val healthScore: Int,
    val id: Int,
    val image: String,
    val imageType: String,
    val instructions: String,
    val lowFodmap: Boolean,
    val occasions: List<Any>,
    val originalId: Any,
    val preparationMinutes: Int,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceName: String,
    val sourceUrl: String,
    val spoonacularSourceUrl: String,
    val summary: String,
    val sustainable: Boolean,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val veryPopular: Boolean,
    val weightWatcherSmartPoints: Int,
    val winePairing: WinePairing
)

fun RecipeByIdDto.toRecipe(): Recipe? {
    return Recipe(
        vegetarian = vegetarian,
        vegan = vegan,
        glutenFree = glutenFree,
        dairyFree = dairyFree,
        veryHealthy = veryHealthy,
        cheap = cheap,
        veryPopular = veryPopular,
        sustainable = sustainable,
        lowFodmap = lowFodmap,
        weightWatcherSmartPoints = weightWatcherSmartPoints,
        gaps = gaps,
        preparationMinutes = preparationMinutes,
        cookingMinutes = cookingMinutes,
        aggregateLikes = aggregateLikes,
        healthScore = healthScore,
        sourceName = sourceName,
        pricePerServing = pricePerServing,
     /*   extendedIngredients = extendedIngredients,*/
        id = id,
        title = title,
        readyInMinutes = readyInMinutes,
        servings = servings,
        sourceUrl = sourceUrl,
        image = image,
        imageType = imageType,
        summary = summary,
        cuisines = cuisines,
        dishTypes = dishTypes,
        diets = diets,
        instructions = instructions,
/*        analyzedInstructions = analyzedInstructions,*/
        spoonacularSourceUrl = spoonacularSourceUrl
    )
}