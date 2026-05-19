package com.example.nammamela

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otp)

        auth = FirebaseAuth.getInstance()

        val phone = findViewById<EditText>(R.id.phoneNumber)
        val otp = findViewById<EditText>(R.id.otp)
        val sendBtn = findViewById<Button>(R.id.sendOtpBtn)
        val verifyBtn = findViewById<Button>(R.id.verifyOtpBtn)

        // SEND OTP
        sendBtn.setOnClickListener {

            val number = phone.text.toString().trim()

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signIn(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(this@OtpActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onCodeSent(
                        id: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        verificationId = id
                        Toast.makeText(this@OtpActivity, "OTP Sent", Toast.LENGTH_SHORT).show()
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        // VERIFY OTP
        verifyBtn.setOnClickListener {

            val code = otp.text.toString().trim()

            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signIn(credential)
        }
    }

    private fun signIn(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    Toast.makeText(this, "Login Successful 🎉", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed ❌", Toast.LENGTH_LONG).show()
                }
            }
    }
}