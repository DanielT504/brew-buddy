package com.example.brewbuddy

import android.util.Log
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

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.marketplace.MarketplaceItem
import com.example.brewbuddy.recipes.RecipeResultsVeiwModel
import coil.compose.AsyncImage
import com.example.brewbuddy.recipes.IngredientSection
import com.example.brewbuddy.recipes.IngredientsList
import com.example.brewbuddy.recipes.RecipeNavigationScreens
import com.example.brewbuddy.recipes.TagDto
import com.example.brewbuddy.recipes.TagList
import com.example.brewbuddy.recipes.createTag
//import com.example.brewbuddy.recipes.TagType
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateLight
import com.example.brewbuddy.ui.theme.TitleLarge
import com.example.brewbuddy.util.randomSampleImageUrl

@Composable
fun RecipesScreen (
    name: String
) {
    var activeFilters =  remember { mutableStateListOf<Filter>() }

    var searchQuery = remember { mutableStateOf("") }
    var marketplaceSectionOffset = 20.dp
    if (activeFilters.size >= 3) {
        marketplaceSectionOffset = -(5.dp)
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(modifier = Modifier
            .fillMaxSize()) {
            SearchBarWrapper(activeFilters, searchQuery)
            Box(modifier = Modifier
//                .offset(y = -(marketplaceSectionOffset))
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
            ) {
                if(searchQuery.value.length === 0) {
                    TitleLarge(text = "Search for your next recipe.")
                } else {
                    RecipeSearchResults()

                }
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
private fun SearchBar(activeFilters: SnapshotStateList<Filter>, searchQuery: MutableState<String>) {
    var filtersExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.icon_search),
                    contentDescription = null
                )
            }
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
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

                modifier = Modifier
                    .width(302.dp)
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

                Row(
                    modifier = Modifier.padding(top = 4.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .padding(top = 8.dp)
                            .padding(top = 8.dp, start = 2.dp, end = 8.dp)

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
                    RecipeFilters(
                        state=filtersExpanded,
                        onDismissRequest = { filtersExpanded = false },
                        activeFilters=activeFilters
                    )
                }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            for (filter in activeFilters) {
                FilterTag(filter = filter, activeFilters = activeFilters)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RecipeFilters(state: Boolean, onDismissRequest: () -> Unit, activeFilters: SnapshotStateList<Filter>) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column() {
                Text("Filters")
                TagList.forEach { tagInfo ->
                    val filter = Filter(name=tagInfo.name, filterLabel = tagInfo.label, enabled=true)
                    CheckboxFilter(
                        text = tagInfo.label,
                        checked = activeFilters.contains(filter),
                        onCheckChanged = {
                            updateActiveFilters(filter, activeFilters)
                        }
                    )
                }
            }
            Column() {
                Text("Sort by")
                SortFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(filter.filterLabel) },
                        onClick = {
                            updateActiveFilters(filter, activeFilters)
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun CheckboxFilter(text: String, checked: Boolean, onCheckChanged: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckChanged)
        Text(text)
    }

}
private fun updateActiveFilters(
    filterToAdd: Filter,
    activeFilters: SnapshotStateList<Filter>
): SnapshotStateList<Filter> {
    val oldestToNewest = filters.last { filter: Filter -> filter.name == "date_asce" }
    val newestToOldest = filters.last { filter: Filter -> filter.name == "date_desc" }
    val priceLowToHigh = filters.last { filter: Filter -> filter.name == "price_asce"}
    val priceHighToLow = filters.last { filter: Filter -> filter.name == "price_desc" }
    if (activeFilters.contains(filterToAdd)) {
        Log.d("FILTERS", "Removing")
        activeFilters.remove(filterToAdd)
        return activeFilters
    }
    if (filterToAdd.name == "date_desc"
        && activeFilters.contains(oldestToNewest))
    {
        activeFilters.remove(oldestToNewest)
        activeFilters.add(filterToAdd)
        return activeFilters
    }
    if (filterToAdd.name == "date_asce"
        && activeFilters.contains(newestToOldest))
    {
        activeFilters.remove(newestToOldest)
        activeFilters.add(filterToAdd)

        return activeFilters
    }
    if (filterToAdd.name == "price_asce"
        && activeFilters.contains(priceHighToLow))
    {
        activeFilters.remove(priceHighToLow)
        activeFilters.add(filterToAdd)

        return activeFilters
    }
    if (filterToAdd.name == "price_desc"
        && activeFilters.contains(priceLowToHigh))
    {
        activeFilters.remove(priceLowToHigh)
        activeFilters.add(filterToAdd)

        return activeFilters
    }
    activeFilters.add(filterToAdd)
    return activeFilters
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
private fun SearchBarWrapper(activeFilters: SnapshotStateList<Filter>, searchQuery: MutableState<String>) {
    Box(modifier = Modifier
        .background(color = Color.White)) {
        SearchBar(activeFilters, searchQuery)
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
private fun RecipeSearchResults(viewStateModel: RecipeResultsVeiwModel = hiltViewModel()) {
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
            for (marketplaceItem in marketplaceItems) {
                ResultCard(
                    title = marketplaceItem.postTitle,
                    price = marketplaceItem.price,
                    city = marketplaceItem.city,
                    province = marketplaceItem.province,
                    userName = marketplaceItem.userName
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultCard(
    title: String,
    price: String,
    city: String,
    province: String,
    userName: String,
) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = {/*TODO*/ }
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
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
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
                            Text(text = price, fontSize = 20.sp)
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
//
private val PreferenceFilters = listOf(
    TagList.forEach {tagInfo ->
        Filter(tagInfo.name, tagInfo.label, false);
    }
)

private val SortFilters = listOf(
    Filter("date_asce", "Oldest to Newest", false),
    Filter("date_desc", "Newest to Oldest", false),
    Filter("likes_asce", "Popularity (Low to High)", false),
    Filter("likes_desc", "Popularity (High to Low)", false)
)
//
//data class PreferenceFilter