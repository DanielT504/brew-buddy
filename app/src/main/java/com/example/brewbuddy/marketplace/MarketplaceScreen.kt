package com.example.brewbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.marketplace.MarketplaceItemModal
import com.example.brewbuddy.marketplace.MarketplaceViewModel
import com.example.brewbuddy.profile.RecipeModal
import com.example.brewbuddy.ui.theme.AuthorCardDisplay
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.Label
import com.example.brewbuddy.ui.theme.SearchBar
import com.example.brewbuddy.ui.theme.SearchResultCard
import com.example.brewbuddy.ui.theme.SearchResults
import com.example.brewbuddy.ui.theme.SlateDark


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
    var showDialog = remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarWrapper(viewModel, showDialog)
            Surface(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                MarketplaceSearchResults(navController, viewModel)
            }
            MarketplaceItemModal(viewModel, showDialog,  onClose = { showDialog.value = false })

        }
    }

}

@Composable
private fun MarketplaceSearchResults(navController: NavHostController, viewModel: MarketplaceViewModel) {
    SearchResults(viewModel = viewModel) {
        MarketplaceCard(
            id=it.id,
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
                MarketplaceCategories.forEach { filter ->
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
                        text = { Text(filter.filterLabel) },
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

//    val oldestToNewest = SortFilters.last { filter: Filter -> filter.name == "dateAsce" }
//    val newestToOldest = SortFilters.last { filter: Filter -> filter.name == "dateDesc" }
    val priceLowToHigh = SortFilters.last { filter: Filter -> filter.name == "priceAsce"}
    val priceHighToLow = SortFilters.last { filter: Filter -> filter.name == "priceDesc" }
    if (viewModel.filters.contains(filterToAdd)) {
        viewModel.removeFilter(filterToAdd)
        return
    }
//    if (filterToAdd.name == "dateDesc"
//        && viewModel.filters.contains(oldestToNewest))
//    {
//        viewModel.removeFilter(oldestToNewest)
//        viewModel.addFilter(filterToAdd)
//        return
//    }
//    if (filterToAdd.name == "dateAsce"
//        && viewModel.filters.contains(newestToOldest))
//    {
//        viewModel.removeFilter(newestToOldest)
//        viewModel.addFilter(filterToAdd)
//
//        return
//    }
    if (filterToAdd.name == "priceAsce"
        && viewModel.filters.contains(priceHighToLow))
    {
        viewModel.removeFilter(priceHighToLow)
        viewModel.addFilter(filterToAdd)
        viewModel.sort(filterToAdd.name)
        return
    }
    if (filterToAdd.name == "priceDesc"
        && viewModel.filters.contains(priceLowToHigh))
    {
        viewModel.removeFilter(priceLowToHigh)
        viewModel.addFilter(filterToAdd)
        viewModel.sort(filterToAdd.name)

        return
    }
    viewModel.addFilter(filterToAdd)
    viewModel.sort(filterToAdd.name)
    return
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarWrapper(viewModel: MarketplaceViewModel, showDialog: MutableState<Boolean>) {
    var filtersExpanded = remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .background(color = Color.White)
    ) {
        Box(modifier=Modifier.weight(1f)){
            SearchBar(viewModel, filtersExpanded,
                bar={
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .padding(top = 8.dp)
                            .padding(top = 8.dp, end = 8.dp)
                    ) {
                        IconButton(onClick = { showDialog.value = true }) {
                            Icon(
                                painterResource(id = R.drawable.icon_add_outline),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = Color.Gray,
                            )
                        }

                    }
                }
            ) {
                MarketplaceFilters(
                    state=filtersExpanded.value,
                    onDismissRequest = { filtersExpanded.value = false },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun MarketplaceCard(
    id: String,
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
        onClick = { navigateToItem(id, navController) }
    ) {
        Column() {
            Row() {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            }
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AuthorCardDisplay(author, textColor = Color.Black)
                }
            }
        }
    }
}

private val MarketplaceCategories = listOf(
    Filter(filterLabel = "Equipment", enabled = true, name="equipment"),
    Filter(filterLabel = "Ingredients", enabled = true, name="ingredients"),
)

private val SortFilters = listOf(
//    Filter(filterLabel = "Newest to Oldest", enabled = false, name="dateDesc"),
//    Filter(filterLabel = "Oldest to Newest", enabled = false, name="dateAsce"),
    Filter(filterLabel = "Price (Low to High)", enabled = false, name="priceAsce"),
    Filter(filterLabel = "Price (High to Low)", enabled = false, name="priceDesc"),
//    Filter(filterLabel = "Range (10km-25km)", enabled = false, name="range"),
)
