package com.example.brewbuddy.recipes;

import java.util.Objects

data class IngredientsList (
    val quantities: List<Number>,
    val units: List<String>,
    val ingredients: List<String>,
)
data class IngredientSection (
    val sectionName: String,
    val ingredientsList: IngredientsList
)

fun createIngredientsFromJSON(ingredients: List<Map<String, Object>>): List<IngredientSection> {
    var recipeIngredients = ArrayList<IngredientSection>()

    ingredients.forEach { ingredient ->
        val sectionName = ingredient["sectionName"] as String
        val ingredientList = IngredientsList(
            quantities=ingredient["quantities"] as List<Number>,
            units=ingredient["units"] as List<String>,
            ingredients=ingredient["ingredients"] as List<String>
        )
        recipeIngredients.add(IngredientSection(sectionName=sectionName, ingredientsList=ingredientList))
    }

    return recipeIngredients.toList()
}
//    return listOf(
//        Ingredient(
//            ingredientName = "Espresso",
//            IngredientComposition(quantities = listOf("4.5 tbsp", "4oz"), subIngredientDetails = listOf("Finely-ground dark roast coffee", "Water"))
//
//        ),
//        Ingredient(
//            ingredientName = "Foam Milk",
//            IngredientComposition(quantities = listOf("4oz"), subIngredientDetails = listOf("Milk"))
//        )
//    )
