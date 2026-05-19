package com.example.nammamela

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        val tvHistory = findViewById<TextView>(R.id.tvHistory)

        // 1. Get the email of the user who is currently logged in
        val userPrefs = getSharedPreferences("users", Context.MODE_PRIVATE)
        val userEmail = userPrefs.getString("loggedInUser", "Guest") ?: "Guest"

        // 2. Load history specific to this user email
        val prefs = getSharedPreferences("booking", Context.MODE_PRIVATE)
        val historyKey = "history_$userEmail"
        
        val history = prefs.getString(historyKey, "No bookings yet for $userEmail 🎟️")

        tvHistory.text = history
    }
}
