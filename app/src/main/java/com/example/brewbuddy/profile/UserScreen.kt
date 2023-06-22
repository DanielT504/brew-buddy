package com.example.brewbuddy.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.CardWithIconAndTitle
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.R
import com.example.brewbuddy.getUser
import com.example.brewbuddy.ui.theme.TitleLarge

@Composable
fun UserScreen(menuButton: @Composable () -> Unit) {
    val user = getUser()

    Column() {
        ProfileHeader(user, menuButton)
        Surface() {
            TitleLarge(text="Pinned Recipes")
        }
        CardWithIconAndTitle(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ProfileHeader(user: User, menuButton: @Composable () -> Unit) {
    val profilePictureSize = 126.dp
    val username = user.getUsername()
    val avatar = user.getAvatar()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(){
            ProfileBanner(R.drawable.profile_banner)
            menuButton()
        }
        Box(
            modifier= Modifier
                .offset(y = -(profilePictureSize / 2 + 25.dp))
                .fillMaxWidth()
        ) {
            Surface(
                modifier= Modifier
                    .offset(y = profilePictureSize / 2)
                    .height(profilePictureSize)
                    .fillMaxWidth(),
                shape= RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
            ) {
                Text(
                    text=username,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y=profilePictureSize/2)
                )

            }
            Box(modifier=Modifier.align(Alignment.TopCenter)) {
                ProfilePicture(avatar, profilePictureSize)
            }
        }
    }
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