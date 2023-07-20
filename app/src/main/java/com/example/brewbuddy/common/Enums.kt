package com.example.brewbuddy.common

enum class DrinkType(val label: String) {
    COFFEE("Coffee"),
    TEA("Tea"),
    JUICE("Juice"),
    SMOOTHIE("Smoothie"),
    ALCOHOL("Alcohol")
}

enum class Diets(val label: String) {
    KETO("Keto"),
    PALEO("Paleo"),
    VEGAN("Vegan"),
    VEGETARIAN("Vegetarian"),
    KOSHER("Kosher"),
    HALAL("Halal"),
}

enum class Allergens(val label: String) {
    TREE_NUTS("Tree Nuts"),
    PEANUTS("Peanuts"),
    SOY("Soy"),
    DAIRY("Dairy")
}