package com.example.brewbuddy.requests

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.example.brewbuddy.recipes.Recipe
import com.example.brewbuddy.recipes.createIngredientsFromJSON
import com.example.brewbuddy.recipes.createPreparationStepsFromJSON
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

suspend fun getRecipe(id: String): Recipe {
    Log.d("RECIPE_BY_ID", id)
    var res = Recipe()
//    val functions = FirebaseFunctions.getInstance()

    return withContext(Dispatchers.IO) {
        val dataDeferred = async {
            getFunctions()
                .getHttpsCallable("getRecipeById")
                .call(hashMapOf("recipeId" to id)).await()
        }
        val task = dataDeferred.await()
        val data = task.data as HashMap<String, Object>
        return@withContext Recipe(
            name = data["title"] as String,
            desc = "",
            ingredientList = createIngredientsFromJSON(data["ingredients"] as List<HashMap<String, Object>>),
            backgroundImage = data["bannerUrl"] as String,
            preparationSteps = createPreparationStepsFromJSON(data["steps"] as List<HashMap<String, Object>>)

        )
    }
}
