package com.example.brewbuddy.recipes;

data class IngredientComposition (
    val quantities: List<String>,
    val subIngredientDetails: List<String>,
)
data class Ingredient (
    val ingredientName: String,
    val ingredientComposite: IngredientComposition
)

//fun createIngredientsFromJSON(list: List<>) {
//    return listOf(
//        Ingredient(
//            ingredientName = "Espresso",
//            IngredientComposition(quantities = listOf("4.5 tbsp", "4oz"), subIngredientDetails = listOf("Finely-ground dark roast coffee", "Water"))
//        ),
//        Ingredient(
//            ingredientName = "Foam Milk",
//            IngredientComposition(quantities = listOf("4oz"), subIngredientDetails = listOf("Milk"))
//        )
//    )
//}