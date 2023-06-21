package com.example.brewbuddy

import android.graphics.Paint.Align
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewbuddy.profile.CurrentUserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.ui.theme.TitleLarge
import kotlinx.coroutines.CoroutineScope


sealed class ProfileScreens(val route: String, val label: String) {
    object User : ProfileScreens("profile/user", "Profile")
    object PinnedRecipes : ProfileScreens("profile/pinned_recipes", "Pinned Recipes")
    object Settings : ProfileScreens("profile/settings", "Settings")
}

@Composable
fun CardWithIconAndTitle(modifier: Modifier) {
    Card(modifier) {
        val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
        val user = currentUserViewModel.getUser()
        val username = user!!.username
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Filled.Face, contentDescription = "Localized description")
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = username,
//                style = InterFont,
            )
        }
    }
}

@Composable
fun ProfilePicture(@DrawableRes img: Int, size: Dp) {
    Image(
        painter = painterResource(id = img),
        contentDescription = "Profile Picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .border(4.dp, Color.White, shape = CircleShape)
    )
}

@Composable
fun ProfileBanner(@DrawableRes img: Int) {
    Box(modifier = Modifier.height(220.dp)) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "Profile Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val coroutineScope = rememberCoroutineScope()
    var menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val localNavController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()
    val menuItems = listOf(
        ProfileScreens.User,
        ProfileScreens.Settings
    )
    ModalNavigationDrawer(
        drawerContent = {
            ProfileMenu(
                localNavController,
                menuItems,
                coroutineScope,
                menuDrawerState)
        },
        drawerState = menuDrawerState
    ) {
        Surface() {

            CompositionLocalProvider(
                LocalNavGraphViewModelStoreOwner provides vmStoreOwner
            ) {
                NavHost(localNavController, startDestination = ProfileScreens.User.route) {
                    composable(ProfileScreens.User.route) {
                        UserScreen(menuDrawerState, coroutineScope)
                    }

                    composable(ProfileScreens.Settings.route) {
                        SettingScreen(menuDrawerState, coroutineScope)
                    }

                }

            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
fun toggleMenu(menuDrawerState: DrawerState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        if(menuDrawerState.isClosed) menuDrawerState.open() else menuDrawerState.close()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuButton(coroutineScope: CoroutineScope, menuDrawerState: DrawerState, color: Color = Color.Black) {
    IconButton(onClick = { toggleMenu(menuDrawerState, coroutineScope) }) {
        Icon(
            painter = painterResource(id=R.drawable.icon_menu),
            contentDescription ="Profile Menu",
            tint= color
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(menuDrawerState: DrawerState, coroutineScope: CoroutineScope) {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val user = currentUserViewModel.getUser()
    val username = user!!.username
    val profilePictureSize = 126.dp

    Column() {
        Box(){
            ProfileBanner(R.drawable.profile_banner)
            MenuButton(coroutineScope, menuDrawerState, Color.White)

        }
        Box(
            modifier= Modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = -(profilePictureSize / 2 + 25.dp))
                .fillMaxWidth()
        ) {
            Surface(
                modifier= Modifier
                    .offset(y = profilePictureSize / 2)
                    .height(profilePictureSize)
                    .fillMaxWidth(),
                shape=RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
            ) {
                Text(
                    text=username,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y=profilePictureSize/2)
                )

            }
            Box(modifier=Modifier.align(Alignment.TopCenter)) {
                ProfilePicture(R.drawable.profile_picture, profilePictureSize)
            }
        }
        Surface() {
            TitleLarge(text="Pinned Recipes")

        }
        CardWithIconAndTitle(modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(menuDrawerState: DrawerState, coroutineScope: CoroutineScope) {
    Surface(modifier=Modifier.fillMaxSize()) {
        Column() {
            MenuButton(coroutineScope, menuDrawerState)
            TitleLarge(text = "Settings")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(label: String,
             route: String,
             navController: NavController,
             coroutineScope: CoroutineScope,
             menuDrawerState: DrawerState
) {
    Button(
        onClick = {
            toggleMenu(menuDrawerState, coroutineScope);
            navController.navigate(route)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
    ) {
        Text(
            text=label,
            textAlign=TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMenu(
    navController: NavController,
    items: List<ProfileScreens>,
    coroutineScope: CoroutineScope,
    menuDrawerState: DrawerState) {
    Surface(modifier= Modifier
        .width(280.dp)
        .fillMaxHeight(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            items.forEach { item ->
                MenuItem(item.label, item.route, navController, coroutineScope, menuDrawerState)
            }
        }
    }
}