package com.example.brewbuddy.util

fun formatTitle(title: String): String {
    val slicedTitle = title.lowercase().replace("how to make", "")
    return slicedTitle.lowercase().split(" ").joinToString(" ") { it.capitalize() }
}