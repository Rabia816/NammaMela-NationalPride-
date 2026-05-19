package com.example.nammamela

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {

    private lateinit var profileContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        profileContainer = findViewById(R.id.profileContainer)

        // Fixed profile
        addProfile(
            "Admin User",
            "9999999999",
            "admin@gmail.com",
            R.drawable.artist1
        )

        // Load saved users
        loadUsers()

        findViewById<Button>(R.id.btnAddUser).setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    private fun loadUsers() {

        val prefs = getSharedPreferences("USERS", MODE_PRIVATE)
        val savedUsers = prefs.getString("user_data", "") ?: ""

        if (savedUsers.isNotEmpty()) {

            val users = savedUsers.split(";")

            for (user in users) {

                if (user.isNotEmpty()) {

                    val data = user.split(",")

                    if (data.size == 3) {
                        addProfile(
                            data[0],
                            data[1],
                            data[2],
                            R.drawable.artist2
                        )
                    }
                }
            }
        }
    }

    private fun addProfile(
        name: String,
        number: String,
        email: String,
        imageRes: Int
    ) {

        val card = LinearLayout(this)
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(30, 30, 30, 30)
        card.setBackgroundColor(getColor(android.R.color.darker_gray))

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 30)
        card.layoutParams = params

        val image = ImageView(this)
        image.setImageResource(imageRes)
        image.layoutParams =
            LinearLayout.LayoutParams(180, 180)

        val details = LinearLayout(this)
        details.orientation = LinearLayout.VERTICAL
        details.setPadding(30, 0, 0, 0)

        val tvName = TextView(this)
        tvName.text = "Name: $name"
        tvName.textSize = 20f
        tvName.setTextColor(getColor(android.R.color.white))

        val tvPhone = TextView(this)
        tvPhone.text = "Phone: $number"
        tvPhone.setTextColor(getColor(android.R.color.white))

        val tvEmail = TextView(this)
        tvEmail.text = "Email: $email"
        tvEmail.setTextColor(getColor(android.R.color.white))

        details.addView(tvName)
        details.addView(tvPhone)
        details.addView(tvEmail)

        card.addView(image)
        card.addView(details)

        profileContainer.addView(card)
    }
}