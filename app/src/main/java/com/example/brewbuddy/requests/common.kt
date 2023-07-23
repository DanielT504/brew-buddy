package com.example.brewbuddy.requests

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import io.github.cdimascio.dotenv.dotenv

fun getFunctions(): FirebaseFunctions {

    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    val debug = (dotenv["DEBUG"] ?: "false").lowercase()

    Log.d("Run Mode", if(debug === "true") { "DEBUG"} else { "PROD"});

    val functions = FirebaseFunctions.getInstance()

    if(debug == "true") {
        functions.useEmulator("10.0.2.2", 5001)
    }
    return functions
}
