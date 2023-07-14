package com.example.brewbuddy.data.remote.dto

import retrofit2.http.GET
import retrofit2.http.Path

interface SpoonacularApi {
    @GET("/recipes/random?apiKey=68f7e11dd7524f4791945da8d6769eb6&number=20&tags=drinks,maxAlcohol=0")
    suspend fun getRecipes(): RecipesResponse


    @GET("/recipes/{recipeId}/information?apiKey=68f7e11dd7524f4791945da8d6769eb6")
    suspend fun getRecipeById(@Path("recipeId") recipeId: String): RecipeByIdResponse
}