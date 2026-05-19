package com.example.nammamela

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val etName = findViewById<EditText>(R.id.etName)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {

            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("USERS", MODE_PRIVATE)

            // old data
            val oldUsers = prefs.getString("user_data", "") ?: ""

            // new user format
            val newUser = "$name,$phone,$email;"

            // save permanently
            prefs.edit()
                .putString("user_data", oldUsers + newUser)
                .apply()

            Toast.makeText(this, "User Saved", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, UserProfileActivity::class.java))
            finish()
        }
    }
}