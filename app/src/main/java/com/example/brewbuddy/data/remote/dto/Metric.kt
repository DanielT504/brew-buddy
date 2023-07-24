package com.example.brewbuddy.data.remote.dto

data class Metric(
    val amount: Number,
    val unitLong: String,
    val unitShort: String
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Metric(
                amount=map["amount"] as? Number ?: 0,
                unitLong=map["unitLong"] as? String ?: "",
                unitShort=map["unitShort"] as? String ?: ""
            )
        }.data
    }

}