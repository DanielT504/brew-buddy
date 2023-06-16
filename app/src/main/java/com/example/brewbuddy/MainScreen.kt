package com.example.brewbuddy

import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.SlateLight

sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Profile : BottomNavigationScreens("Profile", R.string.profile_route, R.drawable.icon_user)
    object ShopLocator : BottomNavigationScreens("ShopLocator", R.string.shop_locator_route, R.drawable.icon_locator)
    object Marketplace : BottomNavigationScreens("Marketplace", R.string.marketplace_route, R.drawable.icon_store)
    object Featured : BottomNavigationScreens("Featured", R.string.featured_route, R.drawable.icon_explore)
    object Recipes : BottomNavigationScreens("Recipes", R.string.recipes_route, R.drawable.icon_page)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Profile,
        BottomNavigationScreens.ShopLocator,
        BottomNavigationScreens.Marketplace,
        BottomNavigationScreens.Featured,
        BottomNavigationScreens.Recipes
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(navController, bottomNavigationItems)
        },
        content = {innerPadding ->
            MainScreenNavigationConfigurations(navController, innerPadding)
        }
    )
}

@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController,
padding: PaddingValues
) {
    NavHost(navController, startDestination = BottomNavigationScreens.Profile.route) {
        composable(BottomNavigationScreens.Profile.route) {
            Screen("profile")
        }
        composable(BottomNavigationScreens.ShopLocator.route) {
            Screen("shoplocator")
        }
        composable(BottomNavigationScreens.Marketplace.route) {
            Screen("marketplace")
        }
        composable(BottomNavigationScreens.Featured.route) {
            Screen("featured")
        }
        composable(BottomNavigationScreens.Recipes.route) {
            Screen("recipes")
        }

    }
}

@Composable
fun Screen(
    name: String
) {
    // Adds view to Compose
        // View's been inflated - add logic here if necessary
            // A surface container using the 'background' color from the theme
    Surface(modifier = Modifier.fillMaxSize(), color=MaterialTheme.colorScheme.background) {
        Greeting(name)
    }

}

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            IconButton(
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = "",
                    tint = if (currentRoute == item.route) GreyLight else SlateLight,
                )

            }
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}