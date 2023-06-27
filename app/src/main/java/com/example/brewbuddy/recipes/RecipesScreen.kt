package com.example.brewbuddy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.brewbuddy.recipes.IndividualRecipeScreen

@Composable
fun RecipesScreen(
    navController: NavHostController
) {
       IndividualRecipeScreen(navController)


}
