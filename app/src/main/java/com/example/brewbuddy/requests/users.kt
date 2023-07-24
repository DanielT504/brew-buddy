package com.example.brewbuddy.requests

import android.util.Log
import com.example.brewbuddy.profile.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

suspend fun getUserById(id: String): User {
    Log.d("USER_BY_ID", id)
//    val functions = FirebaseFunctions.getInstance()
//    functions.useEmulator("10.0.2.2", 5001)

    return withContext(Dispatchers.IO) {
        val dataDeferred = async {
            getFunctions()
                .getHttpsCallable("getUserById")
                .call(hashMapOf("userId" to id)).await()
        }
        val task = dataDeferred.await()
        val data = task.data as HashMap<String, Object>
        return@withContext User(
            username=data["username"] as String,
            email=data["email"] as String,
            bannerUrl = data["bannerUrl"] as String?,
            avatarUrl = data["avatarUrl"] as String?,
            userId=data["uid"] as String
//            avatarUrl=data["avatarUrl"] as String,

//            name = data["title"] as String,
//            desc = "",
//            ingredientList = createIngredientsFromJSON(data["ingredients"] as List<HashMap<String, Object>>),
//            backgroundImage = data["bannerUrl"] as String,
//            preparationSteps = createPreparationStepsFromJSON(data["steps"] as List<HashMap<String, Object>>)

        )
    }
}
