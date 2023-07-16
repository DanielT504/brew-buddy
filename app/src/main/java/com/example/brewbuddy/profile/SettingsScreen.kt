package com.example.brewbuddy.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.ui.theme.TitleLarge
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(menuButton: @Composable () -> Unit) {
    Surface(modifier= Modifier.fillMaxSize()) {
        Column {
            menuButton()
            TitleLarge(text = "Settings")
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Text(
                    text = "Set your maximum shop radius",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            var sliderPosition by remember { mutableStateOf(0f) }
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" },
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..100f,
                    onValueChangeFinished
                    = {
                        //todo
                    },
                    steps = 9
                )
            }
            Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                Text(text = sliderPosition.roundToInt().toString()+" km")
            }
            Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                Text(
                    text = "Choose your dietary preferences",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            val (checkedState, onStateChange) = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState,
                            onValueChange = { onStateChange(!checkedState) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Vegan",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (checkedState1, onStateChange1) = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState1,
                            onValueChange = { onStateChange1(!checkedState1) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState1,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Vegetarian",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (checkedState2, onStateChange2) = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState2,
                            onValueChange = { onStateChange2(!checkedState2) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState2,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Lactose-free",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            val (checkedState3, onStateChange3) = remember { mutableStateOf(false) }
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState3,
                            onValueChange = { onStateChange3(!checkedState3) },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedState3,
                        onCheckedChange = null // null recommended for accessibility with screenreaders
                    )
                    Text(
                        text = "Keto",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Save preferences")
                }
            }

        }
    }
}
