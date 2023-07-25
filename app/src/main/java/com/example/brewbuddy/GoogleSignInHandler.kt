import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.brewbuddy.MainActivity
import com.example.brewbuddy.profile.CurrentUserRepository
import com.example.brewbuddy.profile.AccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//private const val RC_SIGN_IN = 9001

private val auth = Firebase.auth

private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken("936295543644-ovaailsfjpdibr169fqkqufb2kpf8ian.apps.googleusercontent.com")
    .requestEmail()
    .build()

@Composable
fun GoogleSignInButton(
    activity: MainActivity,
    onGoogleSignInSuccess: (GoogleSignInAccount) -> Unit,
    currentUserRepository: CurrentUserRepository,
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    val signInResult = remember { mutableStateOf<GoogleSignInAccount?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Sign-in was successful
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            signInResult.value = task.getResult(ApiException::class.java)
        } else {

            Log.w("GoogleSignInButton", "Sign-in was canceled or failed with result code: ${result.resultCode}")
        }
    }


    LaunchedEffect(signInResult.value) {
        signInResult.value?.let { account ->
            handleSignInResult(account, onGoogleSignInSuccess, currentUserRepository, navController)            // Now that we successfully signed in with Google, we can call the suspend function here.
            viewModel.registerUserWithGoogle(context, account.displayName!!, account.email!!) {
                activity.setLogin(true)
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { startGoogleSignInActivity(googleSignInClient, launcher) },
            modifier = Modifier
                .width(270.dp)
                .padding(vertical = 0.dp)
        ) {
            Text(text = "Sign in with device Google account")
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { startGoogleSignOutActivity(googleSignInClient, launcher) },
            modifier = Modifier
                .width(250.dp)
                .padding(vertical = 0.dp)
        ) {
            Text(text = "Sign in with a different account")
        }
    }

    signInResult.value?.let { account ->
        signInResult.value = null

        CoroutineScope(Dispatchers.Main).launch {
            onGoogleSignInSuccess(account)
        }
    }
}

@Composable
fun GoogleRegisterButton(
    onGoogleSignInSuccess: suspend (GoogleSignInAccount) -> Unit,
) {
    val context = LocalContext.current
    val googleRegisterClient = remember { GoogleSignIn.getClient(context, gso) }
    val signInResult = remember { mutableStateOf<GoogleSignInAccount?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        signInResult.value = task.getResult(ApiException::class.java)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { startGoogleSignOutActivity(googleRegisterClient, launcher) },
            modifier = Modifier
                .width(280.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Register with Google")
        }
    }

    signInResult.value?.let { account ->
        signInResult.value = null

        CoroutineScope(Dispatchers.Main).launch {
            onGoogleSignInSuccess(account)
        }
    }
}

private fun startGoogleSignInActivity(googleSignInClient: GoogleSignInClient, launcher: ActivityResultLauncher<Intent>) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun startGoogleSignOutActivity(googleSignInClient: GoogleSignInClient, launcher: ActivityResultLauncher<Intent>) {
    googleSignInClient.signOut()
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}

private fun handleSignInResult(
    account: GoogleSignInAccount,
    onGoogleSignInSuccess: (GoogleSignInAccount) -> Unit,
    currentUserRepository: CurrentUserRepository,
    navController: NavController
) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

    auth.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val uid = currentUser.uid
                CoroutineScope(Dispatchers.IO).launch {
                    val isDeleted = currentUserRepository.deleteAccount(uid)

                    if (isDeleted) {

                        navController.popBackStack()
                    } else {

                    }
                }
            }

            onGoogleSignInSuccess(account)
        } else {
            Log.e("GoogleSignIn", "Sign-in failed: ${task.exception}")
        }
    }
}
