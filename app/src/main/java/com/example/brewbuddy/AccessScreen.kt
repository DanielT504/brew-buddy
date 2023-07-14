package com.example.brewbuddy

import createAccount
import signIn
import GoogleRegisterButton
import GoogleSignInButton
import android.app.Activity
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.ui.theme.InterFont
import com.example.brewbuddy.ui.theme.OrangeBrownMedium
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.GreenMedium
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class AccessScreens(val route: String, @StringRes val resourceId: Int) {
    object Login : AccessScreens("Profile", R.string.login_route)
    object Register : AccessScreens("Register", R.string.register_route)
}

@Composable
fun FormWrapper(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        content()
    }
}

//TODO: logout button from profile screen, register screen UX, email confirmation, login/signup testing, prevent reuse of emails
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, activity: Activity) {

    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val nonWhitespaceFilter = remember { Regex("^[^\n ]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    val errorMsg = remember {mutableStateOf("")}

    val focusRequester = remember {FocusRequester()}
    val passwordFocusRequester = remember {FocusRequester()}
    var isLoginEnabled by remember {mutableStateOf(false)}

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            FormWrapper() {
                Box(modifier = Modifier.padding(bottom = 20.dp))
                Title()
                TextField(
                    value = username,
                    onValueChange = {
                        if(it.text. matches(alphanumericFilter)){
                            username = it
                        }
                    },
                    placeholder = { Text(text = "Username")},
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            passwordFocusRequester.requestFocus()
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester)
                )
                TextField(
                    value = password,
                    onValueChange = {
                        if(it.text. matches(nonWhitespaceFilter)){
                            password = it
                        }

                        isLoginEnabled = username.text.isNotBlank() && password.text.isNotBlank()
                    },
                    placeholder = { Text(text = "Password")},
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(if (passwordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isLoginEnabled) {
                                // Launch a coroutine in the CoroutineScope
                                CoroutineScope(Dispatchers.Main).launch {
                                    val loginResult = loginUser(username.text, password.text, errorMsg, currentUserViewModel, activity)
                                    Log.d("UPDATE_UI", "User is signed in: 1")
                                    if (!loginResult.first) {
                                        password = TextFieldValue("") // Clear the password field
                                        errorMsg.value = "Incorrect password or username."
                                    } else {
                                        currentUserViewModel.loginUser(username.text, loginResult.second!!)
                                    }
                                }
                            }
                        }
                    ),
                    modifier = Modifier.focusRequester(passwordFocusRequester)
                )
                Button(
                    onClick = {
                        Log.d("LOGIN_USER", "Attempting login for username: ${username.text}")
                        // Launch a coroutine in the CoroutineScope
                        CoroutineScope(Dispatchers.Main).launch {
                            val loginResult = loginUser(username.text, password.text, errorMsg, currentUserViewModel, activity)
                            if (!loginResult.first) {
                                password = TextFieldValue("") // Clear the password field
                                errorMsg.value = "Incorrect password or username."
                            } else {
                                Log.d("UPDATE_UI", "User is signed in: 2")
                                currentUserViewModel.loginUser(username.text, loginResult.second!!)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                    modifier= Modifier.size(width=280.dp, height=40.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("LOGIN")
                }
                ErrorMessage(errorMsg.value)
                Button(
                    onClick = { navController.navigate(AccessScreens.Register.route) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Sign up")
                }
                GoogleSignInButton(onGoogleSignInSuccess = { account ->
                    Log.d("GOOGLE_SIGN_IN", "Successfully signed in with Google: $account")
                    currentUserViewModel.registerUserWithGoogle(account.displayName!!, account.email!!)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, activity: Activity) {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val nonWhitespaceFilter = remember { Regex("^[^\n]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    val errorMsg = remember {mutableStateOf("")}
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.navigate(AccessScreens.Login.route)}) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
            }
        }

        FormWrapper() {
            Title()
            TextField(
                value = username,
                onValueChange = {
                    if(it.text. matches(alphanumericFilter)){
                        username = it
                    }
                },
                placeholder = { Text(text = "Username")},
            )
            TextField(
                value = password,
                onValueChange = {
                    if(it.text. matches(nonWhitespaceFilter)){
                        password = it
                    }
                },
                placeholder = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation()
            )
            TextField(
                value = email,
                onValueChange = {
                    if(it.text. matches(nonWhitespaceFilter)){
                        email = it
                    }
                },
                placeholder = { Text(text = "Email")},
            )
            Button(
                onClick = {
                    errorMsg.value = if (password.text.length < 6) {
                        "Password must be at least 6 characters"
                    } else {
                        createAccount(username.text, password.text, email.text, activity)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val loginResult = loginUser(username.text, password.text, errorMsg, currentUserViewModel, activity)
                                        Log.d("UPDATE_UI", "User is signed in: 1")
                                        if (!loginResult.first) {
                                            password = TextFieldValue("") // Clear the password field
                                            errorMsg.value = "Incorrect password or username."
                                        } else {
                                            currentUserViewModel.loginUser(username.text, loginResult.second!!)
                                            navController.navigate(AccessScreens.Login.route)
                                        }
                                    }
                                } else {
                                    errorMsg.value = "Failed to create account"
                                }
                            }
                        ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                modifier = Modifier.size(width = 280.dp, height = 40.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("REGISTER")
            }
            ErrorMessage(errorMsg.value)
            GoogleRegisterButton(onGoogleSignInSuccess = { account ->
                Log.d("GOOGLE_SIGN_IN", "Successfully signed in with Google: ${account.id}")
                currentUserViewModel.registerUserWithGoogle(account.displayName!!, account.email!!)
            })
        }
    }
}

@Composable
fun ErrorMessage(text: String) {
    Text(text=text, color=Color.Red)
}
@Composable
fun Title(color: Color = OrangeBrownMedium) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text="brew",
            style=TextStyle(
                fontFamily = InterFont,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                letterSpacing = 0.sp
            ),
            color= color)
        Text(
            text="buddy",
            style=TextStyle(
                fontFamily = InterFont,
                fontSize = 32.sp,
                letterSpacing = 0.sp
            ),
            color= color
        )
    }
}
@Composable
fun AccessScreen(activity: Activity) {
    val navController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()

    Surface() {
        CompositionLocalProvider(
            LocalNavGraphViewModelStoreOwner provides vmStoreOwner
        ) {
            NavHost(navController, startDestination = AccessScreens.Login.route) {
                composable(AccessScreens.Login.route) {
                    LoginScreen(navController, activity)
                }
                composable(AccessScreens.Register.route) {
                    RegisterScreen(navController, activity)
                }

            }

        }
    }
}

private suspend fun loginUser(
    username: String,
    password: String,
    errorMsg: MutableState<String>,
    currentUserViewModel: CurrentUserViewModel,
    activity: Activity
): Pair<Boolean, String?> {
    val signInResult = signIn(username, password, activity)
    if (signInResult.success) {
        val email = signInResult.email
        return Pair(true, email)
    } else {
        errorMsg.value = "Incorrect password or username."
        Log.d("UPDATE_UI", "User is signed in 123")
        return Pair(false, null)
    }
}
