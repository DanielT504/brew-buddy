package com.example.brewbuddy.data.remote.dto.RecipeById

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)