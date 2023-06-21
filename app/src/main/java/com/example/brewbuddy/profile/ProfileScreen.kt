package com.example.brewbuddy

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
sealed class ProfileScreens(val route: String, @StringRes val resourceId: Int) {
    object Login : ProfileScreens("UserProfile", R.string.login_route)
    object Register : ProfileScreens("UserSettings", R.string.register_route)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val user = currentUserViewModel.getUser()
    val username = user!!.username
    val coroutineScope = rememberCoroutineScope()
    var menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerContent = { ProfileMenu() }, drawerState = menuDrawerState) {
        Column() {
            Box(modifier = Modifier.height(100.dp)){
                Image(
                    painter = painterResource(id = R.drawable.profile_banner),
                    contentDescription = "Profile Banner",
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = {
                    coroutineScope.launch {
                        if(menuDrawerState.isClosed) menuDrawerState.open() else menuDrawerState.close()
                    }
                }) {
                    Icon(
                        painter = painterResource(id=R.drawable.icon_menu),
                        contentDescription ="Profile Menu",
                        tint= Color.White
                    )
                }
            }
            Surface(modifier = Modifier.fillMaxSize()) {
                Text("Welcome, $username")
            }
        }
    }
}


@Composable
fun MenuItem(label: String) {
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Text(label, style = TextStyle(textAlign = TextAlign.Left))
    }
}
@Composable
fun ProfileMenu() {
    Surface(modifier=Modifier.width(280.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MenuItem("Profile")
            MenuItem("Profile")
        }
    }
}