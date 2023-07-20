package com.example.brewbuddy.profile

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.AccessScreens
import com.example.brewbuddy.PinnedCard
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.R
import com.example.brewbuddy.getUser
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.TitleLarge
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.brewbuddy.recipes.IndividualIngredient
import com.example.brewbuddy.recipes.IngredientsList
import com.example.brewbuddy.shoplocator.Store
import com.example.brewbuddy.store1
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private fun getIndex(currentIndex: Int, startIndex: Int, pageCount: Int): Int {
    val diff = currentIndex - startIndex;
    if (diff < 0) {
        return currentIndex % pageCount
    }
    return diff % pageCount
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(pagerState: PagerState = remember{ PagerState() },) {
    val pageCount = 5
    val bounds = 100 // arbitrarily large # to give the illusion of infinite scroll
    val startIndex = bounds / 2

    val focusColor = GreyMedium
    val unfocusedColor = GreyLight
//    val tempRecipe = Recipe("Latte")

    Column(modifier=Modifier.fillMaxWidth()) {

        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 30.dp),
            pageSize = PageSize.Fixed(220.dp),
            key = null,
            beyondBoundsPageCount = 1,
            pageCount = pageCount,
            pageContent = { index ->
                val page = getIndex(index, startIndex, pageCount)

                // TODO: Temporarily commented out because there is no pinned recipes data being returned by a user
//                Box(contentAlignment = Alignment.Center) {
//                    PinnedCard(modifier = Modifier
//                        .width(210.dp)
//                        .height(150.dp), tempRecipe)
//
//                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientInput(ingredientNumber: Number, ingredientData: IndividualIngredient? = null, onIngredientChange: (IndividualIngredient) -> Unit)  {
    var ingredient by remember { mutableStateOf("")}
    var quantity by remember { mutableStateOf("")}
    var quantityAsNum by remember { mutableStateOf(0)}
    var unit by remember { mutableStateOf("")}

    if (ingredientData != null) {
        ingredient = ingredientData.label
        quantity = ingredientData.quantity.toString()
        quantityAsNum = ingredientData.quantity.toInt()
        unit = ingredientData.unit
    }

    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
            Text(
                text = "Ingredient $ingredientNumber"
            )
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = ingredient,
                onValueChange = { newValue ->
                    ingredient = newValue
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, unit, newValue)
                    )
                },
                label = { Text("Ingredient", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            )
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = quantity,
                onValueChange = { newQuantity ->
                    quantity = newQuantity
                    quantityAsNum = newQuantity.toInt()
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, unit, ingredient)
                    )
                },
                label = { Text("Quantity", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(4.dp),
            )
            TextField(
                value = unit,
                onValueChange = { newValue ->
                    unit = newValue
                    onIngredientChange(
                        IndividualIngredient(quantityAsNum, newValue, ingredient)
                    )
                },
                label = { Text("Unit", style = TextStyle(fontSize = 12.sp, color = Color.Gray)) },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(4.dp),
            )
        }
    }
}

@Composable
fun RecipeModal(openDialog: MutableState<Boolean>, onClose: () -> Unit) {
    val ingredients = remember { mutableStateListOf<IndividualIngredient>() }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { onClose() },
            confirmButton = {
                Row {
                    Button(
                        onClick = { ingredients.add(IndividualIngredient(0, "", "")) }
                    ) {
                        Text("Add Ingredient")
                    }
                    Button(
                        onClick = {
                            var labelList = mutableStateListOf<String>()
                            var unitList = mutableStateListOf<String>()
                            var quantityList = mutableStateListOf<Number>()

                            ingredients.forEach { ingredient ->
                                labelList.add(ingredient.label)
                                unitList.add(ingredient.unit)
                                quantityList.add(ingredient.quantity)
                            }

                            val completedRecipe = IngredientsList(quantityList, unitList, labelList)
                            onClose()
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            },
            dismissButton = {},
            icon = {},
            title = {
                Text(text = "Upload Recipe")
            },
            text = {
                Column(modifier = Modifier.height(400.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        ingredients.forEachIndexed { index, ingredient ->
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                IngredientInput(
                                    index + 1,
                                    ingredientData = ingredient,
                                    onIngredientChange = { updatedIngredient ->
                                        ingredients[index] = updatedIngredient
                                    }
                                )
                            }
                        }
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
//            containerColor = MaterialTheme.colors.surface,
            iconContentColor = Color.Black,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,
//            tonalElevation = AlertDialogDefaults.TonalElevation,
//            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
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
            ImageGrid(3, modifier = Modifier.padding(16.dp))
            var showDialog = remember { mutableStateOf(false) }
            Button(
                onClick = {
                    showDialog.value = true
                }
            ) {
                Text(text = "Upload Recipe")
            }
            RecipeModal(showDialog,  onClose = { showDialog.value = false })
            Box() {
                TitleLarge(text = "Saved Shops near you")
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(32.dp))
                .padding(16.dp, 0.dp, 16.dp, 100.dp)) {
                if (store1.saved) {
                    MapWrapper(stores = arrayOf(store1))
                }
                else {
                    Text(
                        text="You haven't saved any shops near you yet!",
                        style=MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                }
            }

        }

    }


@Composable
fun MapWrapper(stores: Array<Store>) {
    val savedStore = LatLng(stores[0].latitude, stores[0].longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(savedStore, 15f)
    }


    GoogleMap(
        modifier = Modifier
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = savedStore),
            title = stores[0].storeName,
            snippet = "Marker in Waterloo"
        )
    }
}

@Composable
fun ProfileHeader(user: User, menuButton: @Composable () -> Unit) {
    val profilePictureSize = 126.dp
    val username = user.getUsername()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(){
            ProfileBanner(user.getBannerUrl())
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
                ProfilePicture(user.getAvatarUrl(), profilePictureSize)
            }
        }
    }
}
@Composable
fun ProfileBanner(bannerUrl: String) {
    Box(modifier = Modifier.height(200.dp)) {
        AsyncImage(
            model = bannerUrl,
            contentDescription = "Profile Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}