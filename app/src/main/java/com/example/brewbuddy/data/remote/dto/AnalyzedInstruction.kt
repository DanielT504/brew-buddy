package com.example.brewbuddy.data.remote.dto

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)