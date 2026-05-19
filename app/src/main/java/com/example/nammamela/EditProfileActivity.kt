package com.example.nammamela

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val etName = findViewById<EditText>(R.id.etName)
        val etRole = findViewById<EditText>(R.id.etRole)
        val etShows = findViewById<EditText>(R.id.etShows)

        val prefs = getSharedPreferences("PROFILE", MODE_PRIVATE)

        findViewById<Button>(R.id.btnSave).setOnClickListener {

            val editor = prefs.edit()

            editor.putString("name", etName.text.toString())
            editor.putString("role", etRole.text.toString())
            editor.putString("shows", etShows.text.toString())

            editor.apply()

            finish()
        }
    }
}