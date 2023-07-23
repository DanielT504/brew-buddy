package com.example.brewbuddy.data.remote.dto

data class Step(
    val number: Int,
    val step: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Step(
                number=map["number"] as Int,
                step=map["step"] as String,
            )
        }.data
    }
}