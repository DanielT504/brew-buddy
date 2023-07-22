package com.example.brewbuddy.recipes

data class PreparationStepSection (
    val sectionName: String,
    val steps: List<String>,
)

fun createPreparationStepsFromJSON(sections: List<HashMap<String, Object>>): List<PreparationStepSection> {
    val list = ArrayList<PreparationStepSection>()

    sections.forEach {
        section ->
        val steps = PreparationStepSection(
            sectionName = section["sectionName"] as String,
            steps = section["steps"] as List<String>
        )
        list.add(steps)
    }
    return list.toList()
}