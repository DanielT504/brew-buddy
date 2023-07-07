import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Activity
import com.google.firebase.auth.FirebaseUser

private val auth = Firebase.auth

fun createAccount(username: String, password: String, activity: Activity): Boolean {
    auth.createUserWithEmailAndPassword(username, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("CREATE_ACCOUNT", "Account created successfully. User: $user")
                updateUI(user)
            } else {
                Log.d("CREATE_ACCOUNT", "Account creation failed: ${task.exception}")
                Toast.makeText(
                    activity.baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI(null)
            }
        }
    return true;
}

fun signIn(username: String, password: String, activity: Activity): Boolean {
    auth.signInWithEmailAndPassword(username, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(
                    activity.baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI(null)
            }
        }
    return true;
}

private fun updateUI(user: FirebaseUser?) {
}
