package com.example.brewbuddy
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

// Request code used for the sign-in intent
private const val RC_SIGN_IN = 9001

// Configure Google Sign-In options
private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestEmail()
    .build()

@Composable
fun GoogleSignInButton(onGoogleSignInSuccess: (GoogleSignInAccount) -> Unit) {
    val context = LocalContext.current
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, onGoogleSignInSuccess)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { startGoogleSignInActivity(googleSignInClient, launcher) },
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier
                .width(280.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Sign in with Google")
        }
    }
}

private fun startGoogleSignInActivity(googleSignInClient: GoogleSignInClient, launcher: ActivityResultLauncher<Intent>) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, onGoogleSignInSuccess: (GoogleSignInAccount) -> Unit) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        if (account != null) {
            // Google sign-in successful, pass the account to the callback function
            onGoogleSignInSuccess(account)
        }
    } catch (e: ApiException) {
        Log.e("GoogleSignIn", "Sign-in failed with exception: $e")
    }
}