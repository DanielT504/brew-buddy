package com.example.brewbuddy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.marketplace.MarketplaceItem
import com.example.brewbuddy.marketplace.MarketplaceViewModel
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateLight


sealed class MarketplaceNavigationScreens(val route: String) {
    object IndividualItem : MarketplaceNavigationScreens("items/")
}

private fun navigateToItem(itemId: String, navController: NavHostController) {
    navController.navigate(route = MarketplaceNavigationScreens.IndividualItem.route + itemId)
}

@Composable
fun MarketplaceScreen (
    navController: NavHostController,
    viewModel: MarketplaceViewModel = hiltViewModel()
) {
    var activeFilters =  remember { mutableStateListOf<Filter>() }
//    var state = viewModel.marketplaceState.value
    var marketplaceSectionOffset = 20.dp
    if (activeFilters.size >= 3) {
        marketplaceSectionOffset = -(5.dp)
    }
//    if(state.error.isNotBlank()) {
//        Text(
//            text = state.error,
//            color = MaterialTheme.colorScheme.error,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//        )
//    }
//    if(state.isLoading){
//        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
//            Box() {
//                CircularProgressIndicator(modifier = Modifier
//                    .align(Alignment.Center)
//                    .size(34.dp))
//            }
//        }
//    }
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(modifier = Modifier
            .fillMaxSize()) {
            Box() {
                SearchBarWrapper(activeFilters)
            }
            Box(modifier = Modifier
                .offset(y = -(marketplaceSectionOffset))
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ) {
//                Marketplace(navController, viewModel)
            }
        }
    }

}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun SearchBar(activeFilters: SnapshotStateList<Filter>) {
    var text by remember { mutableStateOf("")}
    var filtersExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
            ) {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Search") },
                        colors = TextFieldDefaults
                            .textFieldColors(
                                containerColor = Color.White,
                                textColor = Color.DarkGray,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedIndicatorColor = Color.Gray,
                                disabledLeadingIconColor = Color.DarkGray,
                                disabledIndicatorColor = Color.DarkGray
                            ),
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.icon_search),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .width(300.dp)
                            .padding(start = 16.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            /*onSearch(text)*/
                            // Hide the keyboard after submitting the search
                            keyboardController?.hide()
                            //or hide keyboard
                            focusManager.clearFocus()

                        })
                    )
                }
                Column(modifier = Modifier.background(color = Color.White)) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(52.dp)
                                .padding(top = 8.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            IconButton(onClick = { filtersExpanded = !filtersExpanded }) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = GreenMedium) {
                                            Text(
                                                text = activeFilters.size.toString(),
                                                color = Color.White
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.icon_tune),
                                        contentDescription = null,
                                        modifier = Modifier.size(28.dp),
                                        tint = Color.Gray,
                                    )
                                }
                            }
                        }
                        DropdownMenu(
                            expanded = filtersExpanded,
                            onDismissRequest = { filtersExpanded = false },
                            modifier = Modifier.background(color = Color.White)
                        ) {
                            for (filter in filters) {
                                DropdownMenuItem(
                                    text = { Text(filter.filterLabel) },
                                    onClick = {
                                        canAddToActiveFilters(
                                            filter,
                                            activeFilters
                                        ) && activeFilters.add(filter)
                                    }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .height(52.dp)
                                .padding(top = 8.dp, start = 2.dp, end = 8.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            IconButton(onClick = { /*TODO*/ }) {
/*                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = GreenMedium) {
                                            Text("2", color = Color.White)
                                        }
                                    }
                                ) {*/
                                    Icon(
                                        painterResource(id = R.drawable.icon_add_outline),
                                        contentDescription = null,
                                        modifier = Modifier.size(36.dp),
                                        tint = Color.Gray,
                                    )
                                /*}*/
                            }
                        }
                    }
                }
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            ) {
                for (filter in activeFilters) {
                    FilterTag(filter = filter, activeFilters = activeFilters)
                }
            }
        }
}

private fun canAddToActiveFilters(
    filterToAdd: Filter,
    activeFilters: SnapshotStateList<Filter>
): Boolean {
    val oldestToNewest = filters.last { filter: Filter -> filter.filterLabel == "Oldest to Newest" }
    val newestToOldest = filters.last { filter: Filter -> filter.filterLabel == "Newest to Oldest" }
    val priceLowToHigh = filters.last { filter: Filter -> filter.filterLabel == "Price (Low to High)" }
    val priceHighToLow = filters.last { filter: Filter -> filter.filterLabel == "Price (High to Low)" }
    if (activeFilters.contains(filterToAdd)) {
        return false
    }
    if (filterToAdd.filterLabel == "Newest to Oldest"
        && activeFilters.contains(oldestToNewest))
    {
        return false
    }
    if (filterToAdd.filterLabel == "Oldest to Newest"
        && activeFilters.contains(newestToOldest))
    {
        return false
    }
    if (filterToAdd.filterLabel == "Price (Low to High)"
        && activeFilters.contains(priceHighToLow))
    {
        return false
    }
    if (filterToAdd.filterLabel == "Price (High to Low)"
        && activeFilters.contains(priceLowToHigh))
    {
        return false
    }
    return true
}

@Composable
private fun FilterTag(filter: Filter, activeFilters: SnapshotStateList<Filter>) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = GreenLight)
                .height(28.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    IconButton(onClick = { activeFilters.remove(filter) }, modifier = Modifier.size(16.dp)) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = SlateLight)
                        }
                        Icon(
                            tint = Color.Black,
                            painter = painterResource(id = R.drawable.icon_close),
                            contentDescription = "Display icon",
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = filter.filterLabel,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SearchBarWrapper(activeFilters: SnapshotStateList<Filter>) {
    /*Modify this height based on number of applied filters*/
    var searchbarHeight = 120.dp
    if (activeFilters.size > 3) {
        searchbarHeight = 140.dp
    }
    Box(modifier = Modifier
        .height(searchbarHeight)
        .background(color = Color.White)) {
        SearchBar(activeFilters)
    }
}

@Composable
private fun ActiveFilters() {
    Row() {
        for (filter in filters) {
            if (filter.enabled){
                Text(text = filter.filterLabel)
            }
        }
    }
}

@Composable
private fun Marketplace(
    navController: NavHostController,
    viewModel: MarketplaceViewModel
) {
    val state = viewModel.marketplaceState.value
    Surface(shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Transparent))
    {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 34.dp, bottom = 94.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            for (marketplaceItem in state.data) {
                MarketplaceCard(
                    title = marketplaceItem.postTitle,
                    price = marketplaceItem.price,
                    city = marketplaceItem.city,
                    province = marketplaceItem.province,
                    userName = marketplaceItem.authorId,
                    navController = navController
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarketplaceCard(
    title: String,
    price: Number,
    city: String,
    province: String,
    userName: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = { navigateToItem("123", navController)}
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.width(135.dp)) {
                Image(
                    painterResource(id = R.drawable.coffee_image_1),
                    contentDescription = "Recipe Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
               verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row() {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 8.dp)
                ) {
                    Column() {
                        Row(modifier = Modifier.padding(bottom = 16.dp)) {
                            Text(text = "$ $price", fontSize = 20.sp)
                        }
                        Row() {
                            Text(
                                text = "$city, $province",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.DarkGray
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = userName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.DarkGray
                            )
                            Box(contentAlignment = Alignment.Center) {
                                Canvas(modifier = Modifier.size(22.dp)) {
                                    drawCircle(color = Color.Gray)
                                }
                                Icon(
                                    painterResource(id = R.drawable.icon_user),
                                    contentDescription = "User image placeholder",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

val marketplaceItems = listOf(
    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe"),
    MarketplaceItem(postTitle = "20lbs of fresh-grounded black tea", price = "$150", city = "Cambridge", province = "ON", userName = "Jane Doe"),
    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe"),
    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe")
)

val filters = mutableListOf<Filter>(
    Filter(filterLabel = "Equipment", enabled = false, name="equipment"),
    Filter(filterLabel = "Ingredients", enabled = false, name="ingredients"),
    Filter(filterLabel = "Newest to Oldest", enabled = false, name="dateDesc"),
    Filter(filterLabel = "Oldest to Newest", enabled = false, name="dateAsce"),
    Filter(filterLabel = "Price (Low to High)", enabled = false, name="priceAsce"),
    Filter(filterLabel = "Price (High to Low)", enabled = false, name="priceDesc"),
    Filter(filterLabel = "Range (10km-25km)", enabled = false, name="range"),
)
