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

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_login
        )

        auth = FirebaseAuth.getInstance()

        val etEmail =
            findViewById<EditText>(
                R.id.etEmail
            )

        val etPassword =
            findViewById<EditText>(
                R.id.etPassword
            )

        val btnLogin =
            findViewById<Button>(
                R.id.btnLogin
            )

        val tvRegister =
            findViewById<TextView>(
                R.id.tvRegister
            )

        val btnGoogle =
            findViewById<SignInButton>(
                R.id.btnGoogle
            )

        val prefs =
            getSharedPreferences(
                "users",
                MODE_PRIVATE
            )

        // GOOGLE SIGN IN

        val gso =
            GoogleSignInOptions.Builder(
                GoogleSignInOptions
                    .DEFAULT_SIGN_IN
            )
                .requestIdToken("312019400976-4cotjvhpbv75a3p73at5mkb3jo0sh4tn.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient =
            GoogleSignIn.getClient(
                this,
                gso
            )

        btnGoogle.setOnClickListener {

            val signInIntent =
                googleSignInClient
                    .signInIntent

            startActivityForResult(
                signInIntent,
                100
            )
        }

        // NORMAL LOGIN

        btnLogin.setOnClickListener {

            val email =
                etEmail.text
                    .toString()
                    .trim()

            val password =
                etPassword.text
                    .toString()
                    .trim()

            // EMPTY CHECK

            if (
                email.isEmpty() ||
                password.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please enter all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // GET SAVED PASSWORD

            val savedPassword =
                prefs.getString(
                    email,
                    null
                )

            // USER NOT FOUND

            if (
                savedPassword == null
            ) {

                Toast.makeText(
                    this,
                    "User not found. Register first",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // WRONG PASSWORD

            else if (
                savedPassword != password
            ) {

                Toast.makeText(
                    this,
                    "Wrong password",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // LOGIN SUCCESS

            else {

                Toast.makeText(
                    this,
                    "Login Success",
                    Toast.LENGTH_SHORT
                ).show()

                prefs.edit()

                    .putString(
                        "loggedInUser",
                        email
                    )

                    .apply()

                startActivity(

                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )

                finish()
            }
        }

        // REGISTER SCREEN

        tvRegister.setOnClickListener {

            startActivity(

                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }

    // GOOGLE RESULT

    override fun onActivityResult(

        requestCode: Int,

        resultCode: Int,

        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

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