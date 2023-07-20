package com.example.brewbuddy.data.remote.dto

data class Ingredient(
    val name: String,
    val quantity: Measures
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Ingredient(
                name=map["name"] as String,
                quantity=Measures.from(map["quantity"] as HashMap<String, Object>),
            )
        }.data
    }
}