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
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.brewbuddy.ui.theme.BrewBuddyTheme

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrewBuddyTheme {
        Greeting("BrewBuddy")
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
        content = {innerPadding ->
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
    val vmStoreOwner = rememberViewModelStoreOwner()

    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides vmStoreOwner
    ) {
        NavHost(navController, startDestination = BottomNavigationScreens.Profile.route) {
            composable(BottomNavigationScreens.Profile.route) {
                ProfileScreen()
            }
            composable(BottomNavigationScreens.ShopLocator.route) {
                ShopLocatorScreen("shoplocator")
            }
            composable(BottomNavigationScreens.Marketplace.route) {
                MarketplaceScreen("marketplace")
            }
            composable(BottomNavigationScreens.Featured.route) {
                FeaturedScreen("featured")
            }
            composable(BottomNavigationScreens.Recipes.route) {
                RecipesScreen("recipes")
            }

        }

    }

}