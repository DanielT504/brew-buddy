package com.example.brewbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.InterFont
import com.example.brewbuddy.ui.theme.OrangeBrownLight
import com.example.brewbuddy.ui.theme.OrangeBrownMedium
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginUser: (String, String) -> Unit) {

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize(),

    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            Title()
            TextField(
                value = username,
                onValueChange = {username = it},
                placeholder = { Text(text = "Username")},
            )
            TextField(
                value = password,
                onValueChange = {password = it},
                placeholder = { Text(text = "Password")},
            )
            Button(
                onClick = { loginUser("yes", "yes")},
                colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                modifier= Modifier.size(width=280.dp, height=40.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("LOGIN")
            }
            Button(
                onClick = { loginUser("yes", "yes")},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier= Modifier.size(width=280.dp, height=40.dp)
            ) {
                Text("No account? Register now.", color= SlateDark)
            }
        }

    }
}

@Composable
fun Title(color: Color = OrangeBrownMedium) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text="brew", style=MaterialTheme.typography.titleLarge, color= color)
        Text(text="buddy", style=TextStyle(
            fontFamily = InterFont,
            fontSize = 32.sp,
            letterSpacing = 0.sp
        ), color= color)
    }
}