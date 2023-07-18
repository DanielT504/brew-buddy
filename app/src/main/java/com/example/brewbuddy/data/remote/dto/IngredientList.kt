package com.example.brewbuddy.data.remote.dto

data class IngredientList(
    val name: String,
    val ingredients: List<Ingredient>,
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val ingredientList = map["ingredients"] as List<HashMap<String, Object>>
            val data = IngredientList(
                name=map["name"] as String,
                ingredients=ingredientList.map{Ingredient.from(it)},
            )
        }.data
    }
}
