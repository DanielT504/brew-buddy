import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.auth.AuthResult

private val auth = Firebase.auth
val db = FirebaseFirestore.getInstance()
val documentRef = db.collection("users").document("BKu9YXGYqXhn1uMFeT1J")

fun addUserToFirestore(email: String, username: String, password: String, uid: String) {
    val user = hashMapOf(
        "email" to email,
        "username" to username,
        "password" to password,
        "uid" to uid
    )

    val documentRef = db.collection("users").document(uid)

    documentRef.set(user)
        .addOnSuccessListener {
            Log.d("ADD_USER", "User added to Firestore successfully")
        }
        .addOnFailureListener { exception ->
            Log.d("ADD_USER", "Error adding user to Firestore: $exception")
        }
}

fun createAccount(username: String, password: String, email: String, activity: Activity): Boolean {
    var isSuccess = false

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid: String? = user?.uid
                Log.d("CREATE_ACCOUNT", "Account created successfully. User: $user")
                Log.d("CREATE_ACCOUNT", "User UID: $uid")
                updateUI(user)
                addUserToFirestore(email, username, password, uid!!)
                isSuccess = true // Set isSuccess as true in case of success
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

    return isSuccess
}

data class SignInResult(var success: Boolean, var email: String?, var userId: String?)

suspend fun signIn(username: String, password: String, activity: Activity): SignInResult {
    val usersCollection = db.collection("users")

    return withContext(Dispatchers.IO) {
        val querySnapshotDeferred = async { usersCollection.whereEqualTo("username", username).get().await() }
        val querySnapshot = querySnapshotDeferred.await()

        if (!querySnapshot.isEmpty) {
            val document = querySnapshot.documents[0]
            val email = document.getString("email")

            if (email != null) {
                val authResultDeferred = async { auth.signInWithEmailAndPassword(email, password).await() }
                val authResult = authResultDeferred.await()

                if (authResult != null) {
                    val user = auth.currentUser
                    updateUI(user)
                    return@withContext SignInResult(true, email, user?.uid)
                }
            }
        }

        return@withContext SignInResult(false, null, null)
    }
}


private fun updateUI(user: FirebaseUser?) {
    if (user != null) {
        Log.d("UPDATE_UI", "User is signed in: ${user.uid}")
        Log.d("UPDATE_UI", "User is signed in: ${user.email}")
    } else {
        Log.d("UPDATE_UI", "User is signed out.")
    }
}