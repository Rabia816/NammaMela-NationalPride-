package com.example.nammamela

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MyProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvSavedCount: TextView
    private lateinit var tvBookingsCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        loadLocale() 
        setContentView(R.layout.activity_my_profile)

        auth = FirebaseAuth.getInstance()
        val prefs = getSharedPreferences("users", MODE_PRIVATE)

        val user = auth.currentUser
        val loggedInEmail = prefs.getString("loggedInUser", null)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val imgProfile = findViewById<ImageView>(R.id.imgProfile)
        
        tvSavedCount = findViewById(R.id.tvSavedCount)
        tvBookingsCount = findViewById(R.id.tvBookingsCount)

        val btnSaved = findViewById<LinearLayout>(R.id.btnSaved)
        val btnBookings = findViewById<LinearLayout>(R.id.btnBookings)
        val btnLanguage = findViewById<LinearLayout>(R.id.btnLanguage)
        val btnSignOut = findViewById<LinearLayout>(R.id.btnSignOut)

        // Display User Data
        if (user != null) {
            tvName.text = user.displayName ?: getString(R.string.default_user_name)
            tvEmail.text = user.email
            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(imgProfile)
        } else if (loggedInEmail != null) {
            tvEmail.text = loggedInEmail
            tvName.text = prefs.getString(loggedInEmail + "_name", getString(R.string.default_user_name))
            imgProfile.setImageResource(R.mipmap.ic_launcher)
        }

        btnSaved.setOnClickListener {
            val savedPrefs = getSharedPreferences("saved_dramas", MODE_PRIVATE)
            val count = savedPrefs.all.filter { it.value == true }.size
            Toast.makeText(this, "${getString(R.string.my_saved)}: $count", Toast.LENGTH_SHORT).show()
        }

        btnBookings.setOnClickListener {
            startActivity(Intent(this, BookingHistoryActivity::class.java))
        }

        btnLanguage.setOnClickListener {
            showLanguageDialog()
        }

        btnSignOut.setOnClickListener {
            auth.signOut()
            prefs.edit().remove("loggedInUser").apply()
            Toast.makeText(this, getString(R.string.sign_out), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCounts()
    }

    private fun refreshCounts() {
        val prefs = getSharedPreferences("users", MODE_PRIVATE)
        val savedPrefs = getSharedPreferences("saved_dramas", MODE_PRIVATE)
        val bookingPrefs = getSharedPreferences("booking", MODE_PRIVATE)

        val user = auth.currentUser
        val loggedInEmail = prefs.getString("loggedInUser", null)
        val currentUserEmail = user?.email ?: loggedInEmail ?: "Guest"

        // 1. Update Saved Count
        val savedCount = savedPrefs.all.filter { it.value == true }.size
        tvSavedCount.text = savedCount.toString()

        // 2. Update Bookings Count (Based on tickets in history string)
        val historyKey = "history_$currentUserEmail"
        val userHistory = bookingPrefs.getString(historyKey, "") ?: ""
        val bookedCount = if (userHistory.isEmpty()) 0 else userHistory.split("🎟️").size - 1
        tvBookingsCount.text = bookedCount.toString()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "ಕನ್ನಡ (Kannada)")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.change_lang))
        builder.setItems(languages) { _, which ->
            val lang = if (which == 0) "en" else "kn"
            setLocale(lang)
            
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder.show()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        prefs.edit().putString("My_Lang", lang).apply()
    }

    private fun loadLocale() {
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = prefs.getString("My_Lang", "") ?: ""
        if (language.isNotEmpty()) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }
}
