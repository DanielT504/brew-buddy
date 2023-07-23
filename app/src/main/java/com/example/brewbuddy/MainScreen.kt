package com.example.brewbuddy
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.brewbuddy.marketplace.MarketplaceItemScreen
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.recipes.IndividualRecipeScreen
import com.example.brewbuddy.recipes.Screen
import com.example.brewbuddy.ui.theme.BrewBuddyTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface() {
        Text(
            text = "Greetings from $name!",
            modifier = modifier.padding(24.dp),
            color=MaterialTheme.colorScheme.primary,
            style=MaterialTheme.typography.titleLarge
        )
    }
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
        content = { innerPadding ->
            MainScreenNavigationConfigurations(navController, innerPadding)
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
private fun MainScreenNavigationConfigurations(
    navController: NavHostController,
    padding: PaddingValues
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
        NavHost(navController, startDestination = BottomNavigationScreens.Profile.route) {
            composable(BottomNavigationScreens.Profile.route) {
                ProfileScreen(navController)
            }
            composable(BottomNavigationScreens.ShopLocator.route) {
                ShopLocatorScreen(fusedLocationProviderClient)
            }
            composable(BottomNavigationScreens.Marketplace.route) {
                MarketplaceScreen(navController)
            }
            composable(BottomNavigationScreens.Featured.route) {
                FeaturedScreen(navController)
            }
            composable(BottomNavigationScreens.Recipes.route) {
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