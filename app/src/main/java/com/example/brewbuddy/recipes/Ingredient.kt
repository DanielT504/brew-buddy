package com.example.brewbuddy.recipes;

import android.util.Log
import java.util.Objects

data class IngredientsList (
    val quantities: List<Number>,
    val units: List<String>,
    val labels: List<String>,
)
data class IngredientSection (
    val sectionName: String,
    val ingredientsList: IngredientsList
)

data class IndividualIngredient (
    val quantity: Number,
    val unit: String,
    val label: String,
)

fun createIngredientsFromJSON(ingredients: List<HashMap<String, Object>>): List<IngredientSection> {
    var recipeIngredients = ArrayList<IngredientSection>()

    ingredients.forEach { ingredient ->
        val sectionName = ingredient["sectionName"] as String
        val ingredientList = IngredientsList(
            quantities=ingredient["quantities"] as List<Number>,
            units=ingredient["units"] as List<String>,
            labels=ingredient["labels"] as List<String>
        )
        recipeIngredients.add(IngredientSection(sectionName=sectionName, ingredientsList=ingredientList))
    }

    Log.d("createIngredientsFromJSON", recipeIngredients.toString())
    return recipeIngredients.toList()
}
