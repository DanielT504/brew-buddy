package com.example.brewbuddy.data.remote.dto

data class Instructions(
    val name: String,
    val steps: List<Step>
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val steps = map["steps"] as List<HashMap<String, Object>>
            val data = Instructions(
                name=map["name"] as String,
                steps=steps.map{Step.from(it)},
            )
        }.data
    }
}