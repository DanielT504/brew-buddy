package com.example.brewbuddy

import createAccount
import GoogleRegisterButton
import GoogleSignInButton
import addGoogleUserToFirestore
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.AccountViewModel
import com.example.brewbuddy.ui.theme.GreenMedium
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import com.example.brewbuddy.profile.CurrentUserRepository
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import android.util.Patterns
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.brewbuddy.ui.theme.LoadingScreen
import com.google.firebase.auth.FirebaseAuth


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    currentUserRepository: CurrentUserRepository,
    activity: MainActivity,
    context: Context,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val state = viewModel.loginState.value
    val nonWhitespaceFilter = remember { Regex("^[^\n ]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    val errorMsg = remember {mutableStateOf("")}

    val focusRequester = remember {FocusRequester()}
    val passwordFocusRequester = remember {FocusRequester()}
    var isLoginEnabled by remember {mutableStateOf(false)}

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
        LoadingScreen()
    } else {
        if(state.success == false) {
            errorMsg.value = "Incorrect username or password"
        }
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
                            if (it.text.matches(alphanumericFilter)) {
                                username = it
                            }
                        },
                        placeholder = { Text(text = "Username") },
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
                            if (it.text.matches(nonWhitespaceFilter)) {
                                password = it
                            }

                            isLoginEnabled =
                                username.text.isNotBlank() && password.text.isNotBlank()
                        },
                        placeholder = { Text(text = "Password") },
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
                                    viewModel.signIn(username.text, password.text) {
                                        activity.setLogin(true)
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
                            viewModel.signIn(username.text, password.text) {
                                activity.setLogin(true)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                        modifier = Modifier.size(width = 280.dp, height = 40.dp),
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
                    GoogleSignInButton(
                        onGoogleSignInSuccess = { account ->
                            Log.d("GOOGLE_SIGN_IN", "Successfully signed in with Google: $account")
                            viewModel.registerUserWithGoogle(context, account.displayName!!, account.email!!) {
                                activity.setLogin(true)
                            }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel(),
    activity: MainActivity,
    context: Context
) {
    val state = viewModel.loginState.value
    val nonWhitespaceFilter = remember { Regex("^[^\n]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var showAgeVerificationDialog by remember { mutableStateOf(true) }

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }

    val errorMsg = remember {mutableStateOf("")}
    val passwordVisible = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
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
        LoadingScreen()
    } else {
        if (state.success == false) {
            errorMsg.value = "Incorrect username or password"
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (showAgeVerificationDialog) {
                ShowAgeVerificationDialog(
                    onConfirm = {
                        // The user confirmed their age, show the registration button
                        showAgeVerificationDialog = false // Hide the dialog
                    },
                    onCancel = {
                        // The user declined, close the app
                        activity.finish()
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.navigate(AccessScreens.Login.route) }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
            }

            FormWrapper() {
                Title()
                TextField(
                    value = username,
                    onValueChange = {
                        if (it.text.matches(alphanumericFilter)) {
                            username = it
                        }
                    },
                    placeholder = { Text(text = "Username") },
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
                        if (it.text.matches(nonWhitespaceFilter)) {
                            password = it
                        }
                    },
                    placeholder = { Text(text = "Password") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            emailFocusRequester.requestFocus()
                        }
                    ),
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible.value = !passwordVisible.value },
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(
                                painter = painterResource(if (passwordVisible.value) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.focusRequester(passwordFocusRequester)
                )
                TextField(
                    value = email,
                    onValueChange = {
                        if (it.text.matches(nonWhitespaceFilter)) {
                            email = it
                        }
                    },
                    placeholder = { Text(text = "Email") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            Log.d("REGISTER_USER", username.text)
                            Log.d("REGISTER_PWD", password.text)
                            Log.d("REGISTER_CONF_PWD", email.text)

                            if (password.text.length < 6) {
                                password = TextFieldValue("") // Clear the password field
                                errorMsg.value = "Password must be at least 6 characters"
                                passwordFocusRequester.requestFocus() // Move focus to the password field
                                return@KeyboardActions
                            }

                            errorMsg.value = ""
                            createAccount(username.text, password.text, email.text, activity)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        viewModel.signIn(username.text, password.text) {
                                            activity.setLogin(true)
                                        }
                                    } else {
                                        errorMsg.value = "Failed to create account"
                                    }
                                }
                        }
                    ),
                    modifier = Modifier.focusRequester(emailFocusRequester)
                )

                val auth = FirebaseAuth.getInstance()

                class EmailVerificationState(var emailSent: Boolean = false)

                val emailVerificationState = remember { EmailVerificationState() }
                Button(
                    onClick = {
                        Log.d("REGISTER_USER", username.text)
                        Log.d("REGISTER_PWD", password.text)
                        Log.d("REGISTER_CONF_PWD", email.text)

                        if (password.text.length < 6) {
                            password = TextFieldValue("") // Clear the password field
                            errorMsg.value = "Password must be at least 6 characters"
                            passwordFocusRequester.requestFocus() // Move focus to the password field
                            return@Button
                        }

                        if (!isValidEmail(email.text)) {
                            errorMsg.value = "Invalid email address"
                            return@Button
                        }

                        errorMsg.value = ""
                        createAccount(username.text, password.text, email.text, activity)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    user?.sendEmailVerification()
                                        ?.addOnCompleteListener { emailTask ->
                                            emailVerificationState.emailSent = emailTask.isSuccessful
                                            viewModel.signIn(username.text, password.text) {
                                                activity.setLogin(true)
                                            }
                                        }
                                } else {
                                    errorMsg.value = "Email address already in use"
                                }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                    modifier = Modifier.size(width = 280.dp, height = 40.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("REGISTER")
                }
                ErrorMessage(errorMsg.value)
                GoogleRegisterButton(
                    onGoogleSignInSuccess = { account ->
                        Log.d("GOOGLE_SIGN_IN", "Successfully signed in with Google: ${account.id}")
                        viewModel.registerUserWithGoogle(
                            context,
                            account.displayName!!,
                            account.email!!
                        ) {
                            activity.setLogin(true)
                        }
                    },
                )
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
fun AccessScreen(
    currentUserRepository: CurrentUserRepository,
    activity: MainActivity,
) {
    val navController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()
    Surface() {
        CompositionLocalProvider(
            LocalNavGraphViewModelStoreOwner provides vmStoreOwner
        ) {
            NavHost(navController, startDestination = AccessScreens.Login.route) {
                composable(AccessScreens.Login.route) {
                    LoginScreen(navController=navController, currentUserRepository=currentUserRepository, activity=activity, context = LocalContext.current)
                }
                composable(AccessScreens.Register.route) {
                    RegisterScreen(navController=navController, context = LocalContext.current, activity=activity)
                }
            }
        }
    }
}

@Composable
fun ShowAgeVerificationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = "Age Verification")
        },
        text = {
            Text(text = "Are you 18+ years of age?")
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("No")
            }
        }
    )
}