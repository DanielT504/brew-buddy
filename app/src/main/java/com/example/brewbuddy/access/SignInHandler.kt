import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.AuthResult

private val auth = Firebase.auth
val db = FirebaseFirestore.getInstance()

fun addUserToFirestore(email: String, username: String, password: String, uid: String) {
    Log.d("FIRESTORE", "User data stored in Firestore: $username")
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

fun createAccount(username: String, password: String, email: String, activity: Activity): Task<AuthResult?> {
    return auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val uid: String? = user?.uid
                Log.d("CREATE_ACCOUNT", "Account created successfully. User: $user")
                Log.d("CREATE_ACCOUNT", "User UID: $uid")
                addUserToFirestore(email, username, password, uid!!)
            } else {
                Log.d("CREATE_ACCOUNT", "Account creation failed: ${task.exception}")
            }
        }
}