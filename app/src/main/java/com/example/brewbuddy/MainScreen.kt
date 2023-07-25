package com.example.brewbuddy
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.brewbuddy.marketplace.MarketplaceItemScreen
import com.example.brewbuddy.recipes.IndividualRecipeScreen
import com.example.brewbuddy.recipes.Screen
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.SlateLight
import com.example.brewbuddy.ui.theme.currentRoute
import com.google.android.gms.location.LocationServices

sealed class NavigationScreens(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Profile : NavigationScreens("Profile", R.string.profile_route, R.drawable.icon_user)
    object ShopLocator : NavigationScreens("ShopLocator", R.string.shop_locator_route, R.drawable.icon_locator)
    object Marketplace : NavigationScreens("Marketplace", R.string.marketplace_route, R.drawable.icon_store)
    object Featured : NavigationScreens("Featured", R.string.featured_route, R.drawable.icon_explore)
    object Recipes : NavigationScreens("Recipes/", R.string.recipes_route, R.drawable.icon_page)
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    items: List<NavigationScreens>
) {

    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val currentRoute = currentRoute(navController)
            var active by remember { mutableStateOf(NavigationScreens.Profile.route) }
            items.forEach { item ->
                IconButton(
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route)
                            active = item.route
                        }
                    },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = "",
                        tint = if (active == item.route) GreyMedium else SlateLight,
                    )

                }

            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(activity: MainActivity) {

    val navController = rememberNavController()
    val bottomNavigationItems = listOf(
        NavigationScreens.Profile,
        NavigationScreens.ShopLocator,
        NavigationScreens.Marketplace,
        NavigationScreens.Featured,
        NavigationScreens.Recipes
    )
    Scaffold(
        bottomBar = {
            BottomNavigation(navController, bottomNavigationItems)
        },
        content = {it ->
            MainNavigation(navController, activity)
        }
    )
}
@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner> {
        TODO("blank")
    }
@Composable
private fun MainNavigation(
    navController: NavHostController,
    activity: MainActivity
) {
    // make sure that when you try to access the view model in the navhost children (the screens)
    // it is accessing the right scope, e.g. the scope of the main activity instead
    // of making its own scope within the navhost
    val vmStoreOwner = rememberViewModelStoreOwner()
    val location = LocalContext.current
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(location)
    }
    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides vmStoreOwner
    ) {
        NavHost(navController, startDestination = NavigationScreens.Profile.route) {
            composable(NavigationScreens.Profile.route) {
                ProfileScreen(activity, navController)
            }
            composable(NavigationScreens.ShopLocator.route) {
                ShopLocatorScreen(fusedLocationProviderClient)
            }
            composable(NavigationScreens.Marketplace.route) {
                MarketplaceScreen(navController)
            }
            composable(NavigationScreens.Featured.route) {
                FeaturedScreen(navController)
            }
            composable(NavigationScreens.Recipes.route) {
                RecipesScreen(navController)
            }
            composable(
                route = Screen.IndividualRecipeScreen.route + "/{recipeId}"
            ){
                IndividualRecipeScreen(navController)
            }
            composable(
                route = Screen.MarketplaceItemScreen.route + "/{itemId}"
            )
            {
                MarketplaceItemScreen(navController)
            }
        }

    }

}