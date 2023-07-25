package com.example.brewbuddy.marketplace

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.profile.ImageUpload
import com.example.brewbuddy.profile.IngredientInput
import com.example.brewbuddy.profile.StepInput
import com.example.brewbuddy.profile.db
import com.example.brewbuddy.profile.uploadImageToFirebaseStorage
import com.example.brewbuddy.recipes.IngredientsList
import com.example.brewbuddy.ui.theme.GreenDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.UUID


@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 20.sp),
        modifier=Modifier.padding(top=4.dp)
    )

}

fun uploadListingImageToFirebaseStorage(imageUri: Uri?, onUrlReady: (String) -> Unit) {
    if (imageUri != null) {
        val filename = "recipe_image_${UUID.randomUUID()}"
        val storage = Firebase.storage
        val storageRef = storage.reference

        val imageRef = storageRef.child("images/$filename")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                onUrlReady(downloadUrl)
            } else {
                onUrlReady(Constants.DEFAULT_IMAGE_URL)
            }
        }
    } else {
        onUrlReady(Constants.DEFAULT_IMAGE_URL)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceItemModal(viewModel: MarketplaceViewModel, openDialog: MutableState<Boolean>, onClose: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isEquipment by remember {mutableStateOf(false)}
    var isIngredients by remember {mutableStateOf(false)}
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }

    var uri by remember { mutableStateOf<Uri?>(null) }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { onClose() },
            confirmButton = {
                Column() {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                            onClick = {
                                contact = ""
                                price = ""
                                title = ""
                                description = ""
                                uri = null
                                onClose()
                            }
                        ) {
                            Text("Exit")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                            onClick = {
                                var uriAsString = ""
                                uploadListingImageToFirebaseStorage(
                                    uri
                                ) { newValue: String ->
                                    uriAsString = newValue
                                    viewModel.post(
                                        isEquipment=isEquipment,
                                        isIngredients=isIngredients,
                                        contact=contact,
                                        city=city,
                                        province=province,
                                        bannerUrl=if(newValue.isNotEmpty()) {newValue} else Constants.DEFAULT_IMAGE_URL,
                                        title=title,
                                        description = description,
                                        price=price.toFloat()
                                    )
                                    Log.d("NEWVAL", newValue)
                                    Log.d("URIASSTRING", uriAsString)
                                }
                                onClose()
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            },
            dismissButton = {},
            icon = {},
            title = {
                Text(text = "Upload Listing")
            },
            text = {
                Column(modifier = Modifier.height(400.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)) {
                            Label("Title")
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)) {
                            TextField(
                                value = title,
                                onValueChange = { newValue: String -> title = newValue },
                                placeholder = { Text(text = "Title") },
                            )
                        }
                        Column() {
                            Label("Location")
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                            ) {
                                TextField(
                                    value = city,
                                    onValueChange = { newValue: String -> city = newValue },
                                    placeholder = { Text(text = "City") },
                                    modifier = Modifier.weight(1f)

                                )
                                Spacer(modifier=Modifier.width(8.dp))
                                TextField(
                                    value = province,
                                    onValueChange = { newValue: String -> province = newValue },
                                    placeholder = { Text(text = "Province") },
                                    modifier = Modifier.weight(1f)


                                )
                            }
                        }
                        Column() {
                            Label("Price")

                            TextField(
                                value = price,
                                onValueChange = { newValue: String -> price = newValue },
                                placeholder = { Text(text = "Price") },
                            )
                        }

                        Column() {
                            Label("Categories")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(modifier=Modifier.padding(start=0.dp), checked = isEquipment, onCheckedChange = {isEquipment = !isEquipment})
                                Text(text="Equipment", style=TextStyle(fontSize = 16.sp) )
                                Checkbox(checked = isIngredients, onCheckedChange = {isIngredients = !isIngredients})
                                Text(text="Ingredients", style=TextStyle(fontSize = 16.sp) )
                            }
                        }
                        Column() {
                            Label("Description")

                            TextField(
                                value = description,
                                onValueChange = { newValue: String -> description = newValue },
                                minLines = 4
                            )
                        }
                        Row() {
                            ImageUpload("Select Image", returnImageUri = {newUri -> uri = newUri})
                        }
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
            iconContentColor = Color.Black,
            titleContentColor = Color.Black,
            textContentColor = Color.Black,
        )
    }
}
