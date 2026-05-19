package com.example.nammamela

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val name = findViewById<EditText>(R.id.editName)
        val mobile = findViewById<EditText>(R.id.editMobile)
        val confirmMobile = findViewById<EditText>(R.id.editConfirmMobile)
        val email = findViewById<EditText>(R.id.editEmail)
        val password = findViewById<EditText>(R.id.editPassword)
        val confirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnGoogle = findViewById<SignInButton>(R.id.btnGoogle)

        val prefs = getSharedPreferences("users", MODE_PRIVATE)

        // GOOGLE SIGN IN
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("312019400976-4cotjvhpbv75a3p73at5mkb3jo0sh4tn.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        // REGISTER BUTTON
        btnRegister.setOnClickListener {
            val userName = name.text.toString().trim()
            val userMobile = mobile.text.toString().trim()
            val userConfirmMobile = confirmMobile.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val userConfirmPassword = confirmPassword.text.toString().trim()
            val selectedGenderId = genderGroup.checkedRadioButtonId

            if (userName.isEmpty() || userMobile.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userMobile != userConfirmMobile) {
                Toast.makeText(this, "Mobile mismatch", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userPassword != userConfirmPassword) {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SAVE USER
            prefs.edit()
                .putString(userEmail, userPassword)
                .putString(userEmail + "_name", userName)
                .apply()

            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google Sign-In Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}