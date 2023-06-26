package com.example.brewbuddy.recipes

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.R
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenDark
import com.example.brewbuddy.ui.theme.GreenLight


@Composable
fun IndividualRecipeScreen() {
    Column (modifier = Modifier.fillMaxSize()){
        Box() {
            RecipeBanner(R.drawable.individual_recipe_banner)
        }
        Box(modifier = Modifier
            .offset(y = -(20.dp))
            .fillMaxWidth()) {
            RecipeSection()
        }
    }
}

@Composable
private fun RecipeBanner(@DrawableRes img: Int) {
    Box(modifier = Modifier
        .height(230.dp)
        .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "Recipe Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    tint = Cream,
                    painter = painterResource(id = R.drawable.icon_expand_circle_down),
                    contentDescription = "Navigate back",
                    modifier = Modifier
                        .background(Color.Transparent)
                        .size(60.dp)
                        .rotate(90.0F)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)) {
                Row() {
                    Text(style = MaterialTheme.typography.titleLarge, text =  "Card Title goes here", color =  Cream)
                }
                Row() {
                    Text(modifier = Modifier.padding(horizontal = 2.dp), text = "by Jane Doe", color = Cream)
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 2.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box( modifier = Modifier.align(Alignment.CenterVertically)) {
                        IconButton(onClick = { }) {
                            Canvas(modifier = Modifier.size(38.dp)) {
                                drawCircle(color = Cream)
                            }
                            Icon(
                                tint = Brown,
                                painter = painterResource(id = R.drawable.icon_favourite_heart),
                                contentDescription = "Favourite Recipe",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            tint = Cream,
                            painter = painterResource(id = R.drawable.icon_more_vertical),
                            contentDescription = "Explore more",
                            modifier = Modifier.size(42.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeSection() {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Transparent)
    )
    {
        Column(horizontalAlignment = Alignment.Start) {
            Row(modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 4.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    LabelledIcon(img = R.drawable.icon_time, label = "10 min")
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .size(28.dp), color = Brown)
                    LabelledIcon(img = R.drawable.icon_mug, label = "Serves 1")
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .size(28.dp), color = Brown)
                    LabelledIcon(img = R.drawable.icon_star_outline, label = "4.5")
                }
            }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                testList.add(TagType(iconTint = Color.White, tagColor = GreenLight, tagText = "Tree Nuts", img = R.drawable.icon_info))
                testList.add(TagType(iconTint = Color.White, tagColor = GreenLight, tagText = "Dairy", img = R.drawable.icon_info))
                testList.add(TagType(iconTint = Color.White, tagColor = Brown, tagText = "Espresso Machine", img = R.drawable.icon_countertops))
                testList.add(TagType(iconTint = Color.White, tagColor = GreenDark, tagText = "Cappuccino", img = R.drawable.icon_store))
                TagsSection(tags = testList)
            }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                IngredientsSection()
            }
            Row(modifier = Modifier.padding(top = 24.dp)) {
                PreparationSection()
            }
        }
    }
}

@Composable
private fun LabelledIcon(@DrawableRes img: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = Brown,
            painter = painterResource(id = img),
            contentDescription = "Display icon",
            modifier = Modifier.size(28.dp)
        )
        Text(text = label)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsSection(tags: List<TagType>) {
    FlowRow(
        modifier = Modifier
            .padding(start = 22.dp)
            .background(color = Color.Transparent),
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach {
            Tag(it.iconTint, it.tagColor, it.tagText, it.img)
        }
    }
}

@Composable
private fun IngredientsSection() {
    Column(modifier = Modifier.padding(start = 24.dp)) {
        Row() {
            Text("Ingredients", style = MaterialTheme.typography.titleLarge,)
        }
        Column() {
            Row() {
                Text("Main ingredient heading")
            }
            Row() {
                Text("Sub ingredient #1")
            }
        }
    }
}

@Composable
private fun PreparationSection() {
    Column(modifier = Modifier.padding(start = 24.dp)) {
        Row() {
            Text("Preparation", style = MaterialTheme.typography.titleLarge,)
        }
        Column() {
            Row() {
                Text("For the Main ingredient heading")
            }
            Row() {
                Text("Sub ingredient #1")
            }
        }
    }
}

@Composable
private fun Tag(iconTint: Color,
                tagColor: Color,
                tagText: String,
                @DrawableRes img: Int
) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = tagColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Icon(
                    tint = iconTint,
                    painter = painterResource(id = img),
                    contentDescription = "Display icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = tagText,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

data class TagType (
    val iconTint: Color,
    val tagText: String,
    val tagColor: Color,
    @DrawableRes val  img: Int,
)

private var testList = mutableListOf<TagType>()




