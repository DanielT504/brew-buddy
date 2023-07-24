package com.example.brewbuddy.data.remote.dto

data class Measures(
    val metric: Metric,
    val imperial: Imperial
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = Measures(
                metric=Metric.from(map["metric"] as? HashMap<String, Object> ?: hashMapOf()),
                imperial=Imperial.from(map["imperial"] as? HashMap<String, Object> ?: hashMapOf()),
            )
        }.data
    }
}