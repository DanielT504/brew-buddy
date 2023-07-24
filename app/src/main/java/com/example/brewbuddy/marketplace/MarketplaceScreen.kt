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
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.marketplace.MarketplaceViewModel
import com.example.brewbuddy.recipes.RecipeTagList
import com.example.brewbuddy.recipes.RecipesViewModel
import com.example.brewbuddy.ui.theme.AuthorCardDisplay
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.Label
import com.example.brewbuddy.ui.theme.SearchBar
import com.example.brewbuddy.ui.theme.SearchResultCard
import com.example.brewbuddy.ui.theme.SearchResults
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
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarWrapper(viewModel)
            Surface(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                MarketplaceSearchResults(navController, viewModel)
            }
        }
    }

}

@Composable
private fun MarketplaceSearchResults(navController: NavHostController, viewModel: MarketplaceViewModel) {
    SearchResults(viewModel = viewModel) {
        MarketplaceCard(
            title = it.title,
            price = it.price,
            city = it.city,
            province = it.province,
            author = it.author,
            imageUrl = it.imageUrl,
            navController = navController
        )
    }
}
@Composable
private fun MarketplaceFilters(state: Boolean, onDismissRequest: () -> Unit, viewModel: MarketplaceViewModel) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier=Modifier.width(200.dp)) {
                Label("Categories")
                RecipeTagList.forEach { tagInfo ->
                    val filter = Filter(name=tagInfo.name, filterLabel = tagInfo.label, enabled=true)
                    DropdownMenuItem(
                        leadingIcon = {if(viewModel.filters.contains(filter)) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_checked_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }},
                        text = { Text(tagInfo.label) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                            viewModel.search()
                        }
                    )
                }
            }
            Column() {
                Label("Sort by")
                SortFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(filter.filterLabel) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                        }
                    )
                }
            }
        }
    }

}

private fun updateActiveFilters(
    filterToAdd: Filter,
    viewModel: MarketplaceViewModel
) {

    val oldestToNewest = SortFilters.last { filter: Filter -> filter.name == "dateAsce" }
    val newestToOldest = SortFilters.last { filter: Filter -> filter.name == "dateDesc" }
    val priceLowToHigh = SortFilters.last { filter: Filter -> filter.name == "priceAsce"}
    val priceHighToLow = SortFilters.last { filter: Filter -> filter.name == "priceDesc" }
    if (viewModel.filters.contains(filterToAdd)) {
        viewModel.removeFilter(filterToAdd)
        return
    }
    if (filterToAdd.name == "dateDesc"
        && viewModel.filters.contains(oldestToNewest))
    {
        viewModel.removeFilter(oldestToNewest)
        viewModel.addFilter(filterToAdd)
        return
    }
    if (filterToAdd.name == "dateAsce"
        && viewModel.filters.contains(newestToOldest))
    {
        viewModel.removeFilter(newestToOldest)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "priceAsce"
        && viewModel.filters.contains(priceHighToLow))
    {
        viewModel.removeFilter(priceHighToLow)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "priceDesc"
        && viewModel.filters.contains(priceLowToHigh))
    {
        viewModel.removeFilter(priceLowToHigh)
        viewModel.addFilter(filterToAdd)

        return
    }
    viewModel.addFilter(filterToAdd)
    return
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
private fun SearchBarWrapper(viewModel: MarketplaceViewModel) {
    var filtersExpanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(color = Color.White)) {
        SearchBar(viewModel, filtersExpanded) {
            MarketplaceFilters(
                state=filtersExpanded.value,
                onDismissRequest = { filtersExpanded.value = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MarketplaceCard(
    title: String,
    price: Number,
    city: String,
    province: String,
    imageUrl: String,
    author: Author,
    navController: NavHostController
) {
    SearchResultCard(
        image= imageUrl,
        onClick = { navigateToItem("123", navController) }
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
                    AuthorCardDisplay(author, textColor = Color.Black)
//                    Text(
//                        text = userName,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Light,
//                        color = Color.DarkGray
//                    )
//                    Box(contentAlignment = Alignment.Center) {
//                        Canvas(modifier = Modifier.size(22.dp)) {
//                            drawCircle(color = Color.Gray)
//                        }
//                        Icon(
//                            painterResource(id = R.drawable.icon_user),
//                            contentDescription = "User image placeholder",
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
                }
            }
        }
    }
}

//val marketplaceItems = listOf(
//    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe"),
//    MarketplaceItem(postTitle = "20lbs of fresh-grounded black tea", price = "$150", city = "Cambridge", province = "ON", userName = "Jane Doe"),
//    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe"),
//    MarketplaceItem(postTitle = "Used industrial espresso machine, good condition", price = "$50", city = "Kitchener", province = "ON", userName = "Jane Doe")
//)

private val SortFilters = listOf(
    Filter(filterLabel = "Equipment", enabled = false, name="equipment"),
    Filter(filterLabel = "Ingredients", enabled = false, name="ingredients"),
    Filter(filterLabel = "Newest to Oldest", enabled = false, name="dateDesc"),
    Filter(filterLabel = "Oldest to Newest", enabled = false, name="dateAsce"),
    Filter(filterLabel = "Price (Low to High)", enabled = false, name="priceAsce"),
    Filter(filterLabel = "Price (High to Low)", enabled = false, name="priceDesc"),
    Filter(filterLabel = "Range (10km-25km)", enabled = false, name="range"),
)
