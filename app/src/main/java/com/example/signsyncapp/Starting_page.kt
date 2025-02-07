package com.example.signsyncapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.app.Activity
import android.net.Uri
import android.view.HapticFeedbackConstants
import android.view.View

import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException


class Starting_page : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_starting_page)
        try {
            auth = FirebaseAuth.getInstance()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            // Check if user is already signed in
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is signed in, open HomeActivity
                startHomeActivity(currentUser.email, currentUser.displayName)
            }
        } catch (e: Exception) {
            Log.e("Main", "exception: ", e)
        }

        findViewById<Button>(R.id.login).setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        try {
            // Clear any previous sign-in state
            googleSignInClient.signOut().addOnCompleteListener {
                // After clearing, start the sign-in intent
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        } catch (e: Exception) {
            Log.e("mainactivity", "exception: ", e)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Log.e("mainactivity", "exception: ", e)
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    login()
                }
            }
        } catch (e: Exception) {
            Log.e("mainactivity", "exception: ", e)
        }
    }

    private fun startHomeActivity(email: String?, displayName: String?) {
        try {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("email", email)
                putExtra("name", displayName)
            }
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("mainactivity", "exception: ", e)
        }
    }

    private suspend fun getFirebaseIdToken(): String? {
        return try {
            val user = auth.currentUser
            user?.getIdToken(false)?.await()?.token
        } catch (e: Exception) {
            Log.e("HomeActivity", "Error getting Firebase ID token", e)
            null
        }
    }

    private suspend fun loginHelper(authToken: String) {
        try {
            val response = RetrofitInstance.api.loginToBackend(authToken)
            if (response.isSuccessful && response.body() != null) {
                startHomeActivity(response.body()!!.user?.email, response.body()!!.user?.name)
                Toast.makeText(this,"successfull",Toast.LENGTH_SHORT).show()
            } else {
                Log.e("HomeActivity", "Response not successful: ${response.code()} - ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("HomeActivity", "IOException, you might not have internet connection", e)
        } catch (e: HttpException) {
            Log.e("HomeActivity", "HttpException, unexpected response", e)
        }
    }

    private fun login() {
        lifecycleScope.launch {
            val token = getFirebaseIdToken()
            if (token != null) {
                loginHelper("Bearer $token")
            } else {
                Log.e("HomeActivity", "Failed to get Firebase ID token")
            }
        }
    }
}