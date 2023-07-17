package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.domain.model.Author

data class Imperial(
    val amount: Number,
    val unitLong: String,
    val unitShort: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Imperial(
                amount=map["amount"] as Number,
                unitLong=map["unitLong"] as String,
                unitShort=map["unitShort"] as String
            )
        }.data
    }
}