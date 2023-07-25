package com.example.brewbuddy.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.R
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.navigateToRecipe
import com.example.brewbuddy.recipes.RecipesViewModel
import com.example.brewbuddy.recipes.SearchViewModel

@Composable
fun TitleLarge(text: String) {
    Text(
        text=text,
        modifier = Modifier.padding(start=24.dp, bottom=12.dp),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun AuthorCardDisplay(author: Author, textColor: Color = Color.White) {
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(author.username, color=textColor, fontSize = 18.sp)
        AsyncImage(
            model = author.avatarUrl,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(30.dp)
                .border(4.dp, Color.White, shape = CircleShape)
        )
    }
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun <T>SearchBar(viewModel: SearchViewModel<T>, filtersExpanded: MutableState<Boolean>, bar: @Composable () -> Unit = {}, filters: @Composable () -> Unit) {
    var searchQuery = viewModel.search.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(52.dp)
                    .padding(top = 8.dp)
                    .padding(top = 8.dp, start = 2.dp, end = 8.dp)

                    .align(Alignment.CenterVertically)
            ) {
                IconButton(onClick = { viewModel.search() }) {
                    Icon(
                        painterResource(id = R.drawable.icon_search),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.Gray,
                    )
                }
            }
            BasicTextField(
                modifier = Modifier.fillMaxWidth().weight(1f).fillMaxHeight().padding(top = 16.dp),
                value = searchQuery,
                onValueChange = {viewModel.setKeywords(it)},
                textStyle = TextStyle(
                    color = Color.DarkGray
                ),
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxSize().padding(0.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search",
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxSize().padding(0.dp), verticalAlignment = Alignment.CenterVertically) {
                        innerTextField()
                    }

                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    /*onSearch(text)*/
                    // Hide the keyboard after submitting the search
                    keyboardController?.hide()
                    //or hide keyboard
                    focusManager.clearFocus()

                })
            )

//            TextField(
//                value = searchQuery,
//                onValueChange = {viewModel.setKeywords(it)},
//                placeholder = { Text("Search") },
//                colors = TextFieldDefaults
//                    .textFieldColors(
//                        containerColor = Color.White,
//                        textColor = Color.DarkGray,
//                        unfocusedIndicatorColor = Color.LightGray,
//                        focusedIndicatorColor = Color.Gray,
//                        disabledLeadingIconColor = Color.DarkGray,
//                        disabledIndicatorColor = Color.DarkGray
//                    ),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(bottom=0.dp),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                keyboardActions = KeyboardActions(onSearch = {
//                    /*onSearch(text)*/
//                    // Hide the keyboard after submitting the search
//                    keyboardController?.hide()
//                    //or hide keyboard
//                    focusManager.clearFocus()
//
//                })
//            )
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
                    IconButton(onClick = { filtersExpanded.value = !filtersExpanded.value }) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = GreenMedium) {
                                    Text(
                                        text = viewModel.filters.size.toString(),
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
                filters()
            }
            bar()
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            for (filter in viewModel.filters) {
                FilterTag(filter = filter, viewModel = viewModel)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun <T>FilterTag(filter: Filter, viewModel: SearchViewModel<T>) {
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
                    IconButton(
                        onClick = {
                            viewModel.filters.remove(filter);
                            viewModel.search()
                        },
                        modifier = Modifier.size(16.dp)) {
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
fun <T>SearchResults(
    viewModel: SearchViewModel<T>,
    card: @Composable (el: T) -> Unit
) {
    val state = viewModel.state.value
    if(state.error.isNotBlank()) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    } else if(state.isLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box(modifier=Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(34.dp))
            }
        }
    } else {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        )
        {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 34.dp, bottom = 94.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                for (el in state.results) {
                    card(el)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultCard(image: String, onClick: () -> Unit, content: @Composable () -> Unit) {
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
        onClick = { onClick() }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.width(135.dp)) {
                AsyncImage(
                    model = image,
                    contentDescription = "Image",
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
                content()
            }
        }
    }
}

@Composable
fun Label(text: String) {
    Text(text=text, modifier=Modifier.padding(start=12.dp))
}