package com.example.brewbuddy.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.BoxWithConstraints
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.AccessScreens
import com.example.brewbuddy.PinnedCard
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.R
import com.example.brewbuddy.getUser
import com.example.brewbuddy.recipes.Recipe
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.TitleLarge
import androidx.navigation.NavController

private fun getIndex(currentIndex: Int, startIndex: Int, pageCount: Int): Int {
    val diff = currentIndex - startIndex;
    if (diff < 0) {
        return currentIndex % pageCount
    }
    return diff % pageCount
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel() {
    val pageCount = 5
    val bounds = 100 // arbitrarily large # to give the illusion of infinite scroll
    val startIndex = bounds / 2

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        initialPageOffsetFraction = 0f,
    ) {
        bounds
    }
    val focusColor = GreyMedium
    val unfocusedColor = GreyLight
    val tempRecipe = Recipe("Latte")

    Column(modifier=Modifier.fillMaxWidth()) {

        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 100.dp),
            pageSize = PageSize.Fixed(200.dp),
            key = null,
            beyondBoundsPageCount = 1,
            pageContent = { index ->
                val page = getIndex(index, startIndex, pageCount)

                Box(contentAlignment = Alignment.Center) {
                    PinnedCard(modifier = Modifier
                        .width(200.dp)
                        .height(150.dp), tempRecipe)

                }
            },
        )

        Row(
            modifier= Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(pageCount) { index ->
                val color = if ( getIndex(pagerState.currentPage, startIndex, pageCount) == index) focusColor else unfocusedColor
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(12.dp)
                        .background(color)
                )
            }

        }

    }
}

@Composable
fun ImageGrid(
    columns: Int,
    modifier: Modifier = Modifier,
) {
    val images = listOf(
        R.drawable.x_recipe1,
        R.drawable.x_recipe2,
        R.drawable.x_recipe3,
        R.drawable.x_recipe4,
        R.drawable.x_recipe5,
        R.drawable.x_recipe6,
        R.drawable.x_recipe7,
        R.drawable.x_recipe8,
        R.drawable.x_recipe9,
    )
    var itemCount = images.size
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
//                            navController = NavController()

                            // todo: make images clickable
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(4.dp)
//                                    .clickable( onClick = { navController.navigate(AccessScreens.Login.route)} )
                            ) {
                                Image(
                                    painter = painterResource(images[index]),
                                    contentDescription = "Recipe Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .aspectRatio(1F),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserScreen(menuButton: @Composable () -> Unit) {
    val user = getUser()
    // todo: change to lazycolumn
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ProfileHeader(user, menuButton)

        Column(modifier = Modifier.fillMaxSize()) {
            TitleLarge(text="Pinned Recipes")
            Carousel()

            Box(modifier = Modifier.padding(top = 35.dp)) {
                TitleLarge(text = "Your Recipes")
            }
            ImageGrid(3, modifier = Modifier.padding(bottom = 80.dp))
        }

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
                    .height(profilePictureSize - 25.dp)
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
    Box(modifier = Modifier.height(200.dp)) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "Profile Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }
}