package com.example.brewbuddy.requests

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun notifyUserOnNewItem() {
    Log.d("notify_user", "notification alert")
//    val functions = FirebaseFunctions.getInstance()
//    functions.useEmulator("10.0.2.2", 5001)

     withContext(Dispatchers.IO) {
            getFunctions()
                .getHttpsCallable("notifyUserOnNewItem")


    }
}
