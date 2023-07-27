package com.example.brewbuddy

import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.Recipe
//import com.example.brewbuddy.profile.SettingsScreen
import com.example.brewbuddy.profile.UserScreen
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.compositionLocalOf
<<<<<<< HEAD
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.brewbuddy.components.Content
import com.example.brewbuddy.domain.model.User
=======
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.profile.CurrentUserRepository
>>>>>>> main
import com.example.brewbuddy.profile.SettingScreen
import com.example.brewbuddy.recipes.UserViewModel
import com.example.brewbuddy.ui.theme.LoadingScreen

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

sealed class ProfileScreens(val route: String, val label: String) {
    object User : ProfileScreens("profile/user/", "Profile")
    object Settings : ProfileScreens("profile/settings", "Settings")

}

@Composable
fun PinnedCard(modifier: Modifier, recipe: Recipe) {
    Card(modifier) {
        Box(modifier = Modifier
            .fillMaxSize()
            .zIndex(2f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )) {
            AsyncImage(
                model = recipe.bannerUrl,
                contentDescription = "Recipe Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize() ,
                alpha = 0.6F
            )

            Row(modifier = Modifier
                .padding(16.dp)
                .background(Color.Transparent), verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)){
                    Text(
                        text = recipe.title,
                        color= Color.White,
                        modifier = Modifier.align(alignment = Alignment.Start)
                    )
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent)){
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Localized description",
                        tint = Color.White,
                        modifier = Modifier.align(alignment = Alignment.End)
                    )
                }
            }
        }

    }
}

@Composable
fun ProfilePicture(avatarUrl: String, size: Dp) {
    AsyncImage(
        model = avatarUrl,
        contentDescription = "Profile Picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .border(4.dp, Color.White, shape = CircleShape)
    )
}


@Composable
fun ProfileScreen(
    activity: MainActivity,
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val userState = viewModel.userState.value
    val coroutineScope = rememberCoroutineScope()
    var menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val localNavController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()
    val menuItems = listOf(
        ProfileScreens.User,
        ProfileScreens.Settings
    )
    Content(userState) {
        ModalNavigationDrawer(
            drawerContent = {
                ProfileMenu(
                    localNavController,
                    menuItems,
                    coroutineScope,
                    menuDrawerState,
                    userState.data ?: User()
                )
            },
            drawerState = menuDrawerState
        ) {
            Surface() {
                CompositionLocalProvider(
                    LocalNavGraphViewModelStoreOwner provides vmStoreOwner,
                    LocalNavController provides localNavController
                ) {
                    NavHost(localNavController, startDestination = ProfileScreens.User.route) {
                        composable(route = ProfileScreens.User.route) {
                            UserScreen(
                                navController = navController as NavHostController,
                                menuButton = {
                                    MenuButton(
                                        coroutineScope,
                                        menuDrawerState,
                                        Color.Black,
                                        top = 200.dp
                                    )
                                })
                        }
                        composable(ProfileScreens.Settings.route) {
                            SettingScreen(
                                activity = activity,
                                navController = navController,
                                profileViewModel = viewModel,
                                menuButton = { MenuButton(coroutineScope, menuDrawerState) }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun toggleMenu(menuDrawerState: DrawerState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        if(menuDrawerState.isClosed) menuDrawerState.open() else menuDrawerState.close()
    }
}

 @Composable
private fun MenuButton(
    coroutineScope: CoroutineScope,
    menuDrawerState: DrawerState,
    color: Color = Color.Black,
    top: Dp = 0.dp
) {
    Box(
        modifier = Modifier
            .padding(top = top) // Use the top parameter for padding
            .padding(6.dp) // Add additional padding if needed
    ) {
        IconButton(
            onClick = { toggleMenu(menuDrawerState, coroutineScope) },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_menu),
                contentDescription = "Profile Menu",
                tint = color
            )
        }
    }
}

@Composable
private fun ProfileMenu(
    navController: NavHostController,
    items: List<ProfileScreens>,
    coroutineScope: CoroutineScope,
    menuDrawerState: DrawerState,
    user: User
){

    ModalDrawerSheet(modifier= Modifier
        .width(280.dp)
        .padding(0.dp)
        .fillMaxHeight(),
        drawerShape = RectangleShape,
        drawerContainerColor = Color.White
    ) {
        val selectedItem = remember { mutableStateOf(items[0].route) }
        var selectedColor = MaterialTheme.colorScheme.primary
        val borderWidth = LocalDensity.current.run { 6.dp.toPx() }

        Button(
            onClick = {
                coroutineScope.launch { menuDrawerState.close() }
                navController.navigate(items[0].route)
                selectedItem.value = items[0].route
            },
            modifier = Modifier.fillMaxWidth(),
            colors=ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.Black
            ),
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ProfilePicture(user.avatarUrl, 64.dp)
                Text(
                    text=user.username,
                    style=MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp))
            }

        }
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                selected = item.route == selectedItem.value,
                onClick = {
                    coroutineScope.launch { menuDrawerState.close() }
                    navController.navigate(item.route)
                    selectedItem.value = item.route
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                    .width(280.dp)
                    .drawBehind {
                        if (item.route == selectedItem.value) {
                            drawLine(
                                color = selectedColor,
                                start = Offset(-24f, 0f),
                                end = Offset(-24f, size.height),
                                strokeWidth = borderWidth
                            )
                        }
                    }
                ,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.White,
                    unselectedContainerColor = Color.White,
                    selectedTextColor = selectedColor
                )
            )

        }
    }
}