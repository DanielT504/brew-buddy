package com.example.brewbuddy.profile

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.brewbuddy.AccessScreens
import com.example.brewbuddy.LocalViewModel
import com.example.brewbuddy.MainActivity
import com.example.brewbuddy.ShopLocatorScreen
import com.example.brewbuddy.ui.theme.TitleLarge
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    currentUserViewModel: CurrentUserViewModel,
    currentUserRepository: CurrentUserRepository,
    menuButton: @Composable () -> Unit
) {
    val currentUserRepository = CurrentUserRepository()
    val currentUserViewModel: CurrentUserViewModel = viewModel()

    // Get the coroutine scope
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    var sliderPosition by remember { mutableStateOf(0f) }
    val (checkedState, onStateChange) = remember { mutableStateOf(false) }
    val (checkedState1, onStateChange1) = remember { mutableStateOf(false) }
    val (checkedState2, onStateChange2) = remember { mutableStateOf(false) }
    val (checkedState3, onStateChange3) = remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 128.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                menuButton()
                TitleLarge(text = "Settings")
            }
            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "Set your maximum shop radius",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Slider(
                        modifier = Modifier.semantics {
                            contentDescription = "Localized Description"
                        },
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 0f..100f,
                        onValueChangeFinished = {
                            //todo
                        },
                        steps = 9
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 16.dp, 40.dp, 0.dp)) {
                    Text(text = sliderPosition.roundToInt().toString() + " km")
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 0.dp)) {
                    Text(
                        text = "Choose your dietary preferences",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            item {
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
            }

            item {
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
            }

            item {
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
            }

            item {
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
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 24.dp, 40.dp, 32.dp)) {
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

            item {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            // Logout the user and navigate back to the main activity
                            // currentUserViewModel.setUser(null)

                            // Start the MainActivity to simulate a restart
                            val intent = Intent(navController.context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            navController.context.startActivity(intent)

                            // Finish the current activity to clear it from the back stack
                            (navController.context as? ComponentActivity)?.finish()
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Logout")
                    }
                }
            }

            item {
                Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                handleAccountDeletion(currentUserRepository, currentUserViewModel, navController)
                            }
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Delete Account")
                    }
                }
            }
        }
    }
}

private suspend fun handleAccountDeletion(
    currentUserRepository: CurrentUserRepository,
    currentUserViewModel: CurrentUserViewModel,
    navController: NavController
) {
    val currentUser = currentUserRepository.getCurrentUser()
    if (currentUser != null) {
        val deleteResult = currentUserRepository.deleteAccount(currentUser.uid)

        if (deleteResult) {
            currentUserViewModel.setUser(null)

            val intent = Intent(navController.context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            navController.context.startActivity(intent)

            (navController.context as? ComponentActivity)?.finish()
        } else {
            // Handle deletion failure
        }
    }
}