package com.example.nammamela

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    // ✅ SHOWS DATA FOR SEARCH
    private val showsList = listOf(
        "Kantara Drama",
        "KGF Drama",
        "Comedy Nights",
        "Standup Comedy Show",
        "Bigg Boss Trending",
        "IPL Highlights",
        "Recently Added Movie 1",
        "Recently Added Movie 2"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // SharedPreferences
        val prefs = getSharedPreferences("namma_features", MODE_PRIVATE)

        // Buttons
        val btnSeatBooking = findViewById<Button>(R.id.btnSeatBooking)
        val btnFanWall = findViewById<Button>(R.id.btnFanWall)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        val btnRate = findViewById<Button>(R.id.btnRate)
        val btnFavorite = findViewById<Button>(R.id.btnFavorite)

        // Search + Rating
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val tvRating = findViewById<TextView>(R.id.tvRating)

        // Load rating
        var rating = prefs.getFloat("rating", 0f)
        tvRating.text = "⭐ Rating: $rating"

        // ⭐ RATING SYSTEM
        btnRate.setOnClickListener {

            val ratings = arrayOf(
                "1 Star",
                "2 Star",
                "3 Star",
                "4 Star",
                "5 Star"
            )

            AlertDialog.Builder(this)
                .setTitle("Rate Show")
                .setItems(ratings) { _, which ->

                    rating = (which + 1).toFloat()

                    tvRating.text = "⭐ Rating: $rating"

                    prefs.edit()
                        .putFloat("rating", rating)
                        .apply()
                }
                .show()
        }

        // ❤️ FAVORITE
        btnFavorite.setOnClickListener {

            prefs.edit()
                .putBoolean("favorite_kantara", true)
                .apply()

            Toast.makeText(
                this,
                "Added to Favorites ❤️",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 🔍 WORKING SEARCH (FULL FIX)
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val query = s.toString().lowercase()

                if (query.isNotEmpty()) {

                    val results = showsList.filter {
                        it.lowercase().contains(query)
                    }

                    if (results.isNotEmpty()) {

                        Toast.makeText(
                            this@HomeActivity,
                            "Found: ${results.joinToString(", ")}",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@HomeActivity,
                            "No shows found ❌",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 🎟 SEAT BOOKING
        btnSeatBooking.setOnClickListener {
            startActivity(
                Intent(this, SeatActivity::class.java)
            )
        }

        // 🎭 FAN WALL
        btnFanWall.setOnClickListener {
            startActivity(
                Intent(this, FanWallActivity::class.java)
            )
        }

        // 📜 HISTORY
        btnHistory.setOnClickListener {
            startActivity(
                Intent(this, BookingHistoryActivity::class.java)
            )
        }

        // 👤 PROFILE
        btnProfile.setOnClickListener {
            startActivity(
                Intent(this, MyProfileActivity::class.java)
            )
        }
    }
}