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

val MAX_IMAGE_SIZE: Long = 1024 * 1024

//fun getRecipes(): ArrayList<HashMap<String, Object>> {
//    var result = ArrayList<HashMap<String, Object>>();
//    FirebaseFunctions
//        .getInstance()
//        .getHttpsCallable("getRecipesMetadata")
//        .call()
//        .addOnCompleteListener() {task ->
//            result = task.result?.data as ArrayList<HashMap<String, Object>>
//            Log.d("RECIPES", result.toString())
//        }
//    return result;
//}

suspend fun getRecipe(id: String): Recipe {
    Log.d("RECIPE_BY_ID", id)
    var res = Recipe()
    val functions = FirebaseFunctions.getInstance()

    return withContext(Dispatchers.IO) {
        val dataDeferred = async {
            functions
                .getHttpsCallable("getRecipeById")
                .call(hashMapOf("recipeId" to id)).await()
//            .addOnCompleteListener() { task ->
//                val data = task.result?.data as HashMap<String, Object>
//                Log.d("RECIPEBYID", data.toString())
//                return task.result?.data as HashMap<String, Object>
//            }}
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

@Composable
fun getImageByUrl(url: String) {
    val storage = Firebase.storage

    // [START download_create_reference]
    // Create a storage reference from our app
    val storageRef = storage.reference

    val gsReference = storage.getReferenceFromUrl(url)

    lateinit var imgBytes: ImageBitmap;
    gsReference.getBytes(MAX_IMAGE_SIZE).addOnSuccessListener {
            bitmap ->
        imgBytes = bitmap as ImageBitmap
        // Data for "images/island.jpg" is returned, use this as needed
    }.addOnFailureListener {
        // Handle any errors
    }

    Image(
        bitmap = imgBytes,
        contentDescription = url,
    )


}