package com.example.brewbuddy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.recipes.RecipeNavigationScreens
import com.example.brewbuddy.featured.FeaturedViewModel
import com.example.brewbuddy.featured.FeaturedState
import com.example.brewbuddy.ui.theme.Cream

@Composable
fun FeaturedScreen(
    navController: NavHostController,
    viewModel: FeaturedViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .padding(
                    start = 0.dp,
                    top = 16.dp,
                    bottom = 0.dp,
                    end = 0.dp
                )
        ) {
            GridLayout(navController, state)
        }
    }
    if(state.recipesError.isNotBlank()) {
        Text(
            text = state.recipesError,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
    if(state.isRecipesLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box() {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(34.dp))
            }
        }
    }
}
@Composable
private fun Heading(text: String) {
    Box(
        modifier = Modifier
            .padding(
                start = 8.dp,
                top = 0.dp,
                bottom = 0.dp,
                end = 0.dp
            )
            .fillMaxWidth()
    ) {
        Text(text,
            fontWeight = FontWeight.Bold,
            fontSize=22.sp
        )
    }
}

@Composable
private fun CardTitle(text: String, fontSize: TextUnit) {
    Text(
        text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PopularRecipes(state: FeaturedState) {
    Row(){
        Heading(text = "Popular")
    }
    Carousel(
        itemsCount = state.popular.size,
        itemContent = {  index ->
            PopularCard(recipe = state.popular[index])
        }
    )

    if(state.popularError.isNotBlank()) {
        Text(
            text = state.popularError,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
    if(state.isPopularLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box() {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(34.dp))
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridLayout(navController: NavHostController, state: FeaturedState) {
    val height = ((state.recipes.size*200) + 70).dp
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 14.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .height(height)
    ) {

        item( span = StaggeredGridItemSpan.FullLine) {
            PopularRecipes(state)
        }
        item(
            span = StaggeredGridItemSpan.FullLine
        ) {
            Heading(text = "Picked for you")
        }
        items(state.recipes) {
                recipe ->
            RecipeCard(
                title = recipe.title ?: "",
                photo = recipe.bannerUrl,
                navController = navController,
                recipeId = recipe.id ?: ""
            )
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PopularCard(recipe: RecipeMetadata) {
    Card(
        onClick = { },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
    ) {
        Box() {
            AsyncImage(
                model = recipe.bannerUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(190.dp)
                    .width(260.dp)
            )
            Box(modifier = Modifier.padding(start = 0.dp, top = 135.dp, bottom = 0.dp, end = 0.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(250.dp)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        CardTitle(
                            text = recipe.title,
                            fontSize = 22.sp
                        )
                    }
                    Box(modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_locator),
                            contentDescription = "Location Pin Icon",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeCard(recipeId: String, title: String, photo: Any, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate(route = RecipeNavigationScreens.IndividualRecipe.route + recipeId) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier.fillMaxWidth()

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = photo,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentHeight()
            )
            Row() {
                Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_page),
                        contentDescription = "Location Pin Icon",
                        modifier = Modifier.size(36.dp),
                        tint = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding
                        (
                        start = 0.dp,
                        top = 130.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
            )
            {
                Box() {
                    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                        CardTitle(
                            /*    formattedTitle,*/
                            title,
                            fontSize = 24.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding
                                (
                                start = 0.dp,
                                top = 120.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            )
                            .align(Alignment.CenterEnd)
                    ) {
                        Text("John Doe", color = Color.White, fontSize = 18.sp)
                        Box(
                            Modifier
                                .padding(horizontal = 6.dp, vertical = 0.dp)
                                .size(30.dp), contentAlignment = Alignment.Center) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(SolidColor(Color.White))
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.icon_user),
                                contentDescription = "User image placeholder"
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Carousel(
    modifier: Modifier = Modifier,
    pagerState: PagerState = remember{ PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(pageCount = itemsCount, state = pagerState, pageSize = PageSize.Fixed(300.dp), pageSpacing = 8.dp) { page ->
            itemContent(page)
        }
    }
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(top = 2.dp, start = 0.dp, end = 0.dp, bottom = 12.dp)
        .offset(x = 0.dp, y = 200.dp)
    )
    {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 12.dp
            )
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = GreyMedium,
    unSelectedColor: Color = GreyLight,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .height(14.dp)
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
