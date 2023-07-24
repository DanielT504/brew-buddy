package com.example.brewbuddy.recipes

data class PreparationStepSection (
    val sectionName: String,
    val steps: List<String>,
)

data class IndividualStep (
    val stepNumber: Number,
    val stepInfo: String
)
