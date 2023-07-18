package com.example.brewbuddy.data.remote.dto

import android.util.Log
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe

data class RecipeDto(
    val id: String,
    val bannerUrl: String,
    val summary: String,
    val title: String,
    val instructions: List<Instructions>,
    val ingredientLists: List<IngredientList>,
    val preparationMinutes: Int,
    val servings: Int,
    val likes: Int,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val sustainable: Boolean,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val author: Author
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val instructions = map["instructions"] as List<HashMap<String, Object>>
            val ingredientLists = map["ingredientLists"] as List<HashMap<String, Object>>
            val data = RecipeDto(
                likes=map["likes"] as Int,
                servings=map["servings"] as Int,
                preparationMinutes=map["preparationMinutes"] as Int,
                glutenFree=map["glutenFree"] as Boolean,
                dairyFree=map["dairyFree"] as Boolean,
                sustainable=map["sustainable"] as Boolean,
                vegetarian=map["vegetarian"] as Boolean,
                vegan=map["vegan"] as Boolean,
                author=Author.from(map["author"] as HashMap<String, Object>),
                title=map["title"] as String,
                instructions=instructions.map{Instructions.from(it)},
                ingredientLists=ingredientLists.map{IngredientList.from(it)},
                id=map["id"] as String,
                bannerUrl=map["bannerUrl"] as String,
                summary=map["summary"] as String,
                )
        }.data
    }

}

fun RecipeDto.toRecipe(): Recipe {
    return Recipe(
        likes = likes,
        instructions = instructions,
        dairyFree = dairyFree,
        glutenFree = glutenFree,
        id = id,
        bannerUrl = bannerUrl,
        preparationMinutes = preparationMinutes,
        servings = servings,
        summary = summary,
        sustainable = sustainable,
        title = title,
        vegan = vegan,
        vegetarian = vegetarian,
        ingredientLists = ingredientLists,
        author=author
    )
}