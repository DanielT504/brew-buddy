import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

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

data class SignInResult(var success: Boolean, var email: String?)

fun signIn(username: String, password: String, activity: Activity): SignInResult {
    val usersCollection = db.collection("users")

    val result = SignInResult(false, null)

    usersCollection.whereEqualTo("username", username)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val email = document.getString("email")
                if (email != null) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                updateUI(user)
                                result.success = true
                                result.email = email
                            } else {
                                Toast.makeText(
                                    activity.baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateUI(null)
                            }
                        }
                } else {
                    Log.d("SIGN_IN", "Email not found for username: $username")
                    Toast.makeText(
                        activity.baseContext,
                        "Authentication failed. User not found.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            } else {
                Log.d("SIGN_IN", "User not found for username: $username")
                Toast.makeText(
                    activity.baseContext,
                    "Authentication failed. User not found.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }
        }
        .addOnFailureListener { e ->
            Log.d("SIGN_IN", "Error getting user document: $e")
            Toast.makeText(
                activity.baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
            updateUI(null)
        }

    return result
}


private fun updateUI(user: FirebaseUser?) {
    if (user != null) {
        Log.d("UPDATE_UI", "User is signed in: ${user.uid}")
        Log.d("UPDATE_UI", "User is signed in: ${user.email}")
    } else {
        Log.d("UPDATE_UI", "User is signed out.")
    }
}