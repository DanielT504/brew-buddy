package com.example.brewbuddy.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

suspend fun getRecipeById(id: String): HashMap<String, Object> {
    Log.d("RECIPE_BY_ID", id)
//    var res = Recipe()
//    val functions = FirebaseFunctions.getInstance()

    return withContext(Dispatchers.IO) {
        val dataDeferred = async {
            getFunctions()
                .getHttpsCallable("getRecipeById")
                .call(hashMapOf("recipeId" to id)).await()
        }
        val task = dataDeferred.await()
        val data = task.data as HashMap<String, Object>
        return@withContext data
//        return@withContext Recipe(
//            name = data["title"] as String,
//            desc = "",
//            ingredientList = createIngredientsFromJSON(data["ingredients"] as List<HashMap<String, Object>>),
//            backgroundImage = data["bannerUrl"] as String,
//            preparationSteps = createPreparationStepsFromJSON(data["steps"] as List<HashMap<String, Object>>)
//
//        )
    }
}
