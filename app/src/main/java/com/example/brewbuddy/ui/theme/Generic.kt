package com.example.brewbuddy.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.Author

@Composable
fun TitleLarge(text: String) {
    Text(
        text=text,
        modifier = Modifier.padding(start=24.dp, bottom=12.dp),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun AuthorCardDisplay(author: Author) {
    Row(
        modifier=Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(author.username, color = Color.White, fontSize = 18.sp)
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