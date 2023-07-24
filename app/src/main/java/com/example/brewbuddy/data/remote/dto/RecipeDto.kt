package com.example.brewbuddy.data.remote.dto

import android.util.Log
import com.example.brewbuddy.common.Constants
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
    val author: AuthorDto,
    val tags: List<String>
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val instructions = map["instructions"] as? List<HashMap<String, Object>> ?: emptyList()
            val ingredientLists = map["ingredientLists"] as? List<HashMap<String, Object>> ?: emptyList()
            val data = RecipeDto(
                likes=map["likes"] as? Int ?: 0,
                servings=map["servings"] as? Int ?: 0,
                preparationMinutes=map["preparationMinutes"] as? Int ?: 0,
                glutenFree=map["glutenFree"] as? Boolean ?: false,
                dairyFree=map["dairyFree"] as? Boolean ?: false,
                sustainable=map["sustainable"] as? Boolean ?: false,
                vegetarian=map["vegetarian"] as? Boolean ?: false,
                vegan=map["vegan"] as? Boolean ?: false,
                author=AuthorDto.from(map["author"] as? HashMap<String, Object> ?: hashMapOf()),
                title=map["title"] as? String ?: "",
                instructions=instructions.map{Instructions.from(it)},
                ingredientLists=ingredientLists.map{IngredientList.from(it)},
                id=map["id"] as? String ?: "",
                bannerUrl=map["bannerUrl"] as? String ?: Constants.DEFAULT_BANNER_URL,
                summary=map["summary"] as? String ?: "",
                tags=map["tags"] as? List<String> ?: emptyList()
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
        author=author.toAuthor(),
        tags=tags
    )
}