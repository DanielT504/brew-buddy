package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Preferences
import com.example.brewbuddy.domain.model.Recipe
import com.google.firebase.firestore.DocumentSnapshot

data class PreferencesDto(
    val ingredients: List<String>,
    val radius: Float,
    val nutFree: Boolean,
    val kosher: Boolean,
    val halal: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val keto: Boolean,
) {
    companion object {
        fun from(map: Map<String, Any>) = object {
            val data = PreferencesDto(
                radius = map["radius"] as? Float ?: 0f,
                nutFree = map["nutFree"] as? Boolean ?: false,
                kosher = map["kosher"] as? Boolean ?: false,
                glutenFree = map["glutenFree"] as? Boolean ?: false,
                vegetarian = map["vegetarian"] as? Boolean ?: false,
                keto = map["keto"] as? Boolean ?: false,
                vegan = map["vegan"] as? Boolean ?: false,
                halal = map["halal"] as? Boolean ?: false,
                ingredients = map["ingredients"] as? List<String> ?: emptyList(),
                dairyFree = map["dairyFree"] as? Boolean ?: false,
                )
        }.data
    }

}

fun PreferencesDto.toPreferences(): Preferences {
    return Preferences(
        dairyFree = dairyFree,
        glutenFree = glutenFree,
        vegan = vegan,
        vegetarian = vegetarian,
        ingredients = ingredients,
        kosher=kosher,
        halal=halal,
        nutFree=nutFree,
        radius=radius
    )
}