package com.example.nammamela

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dramaAdapterComedy: DramaAdapter
    private lateinit var dramaAdapterTrending: DramaAdapter
    private lateinit var dramaAdapterRecent: DramaAdapter

    private val allDramas = mutableListOf<Drama>()

    // DYNAMIC EVENT DATA (Industry Ready: Managed from a central point)
    private val currentDramaName = "Nann Aase Nataka"
    private val currentVenue = "Shivamogga Town Hall"
    private val currentAddress = "Near Gandhi Square, Ward 5"
    private val currentLocation = "Shivamogga, Karnataka"
    private val currentDateTime = "Tonight | 9:00 PM"
    private val currentGeoLocation = "13.9299,75.5681" // Coordinates for Town Hall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        loadLocale() 
        setContentView(R.layout.activity_main)

        // Bind Dynamic Play of the Day to UI
        findViewById<TextView>(R.id.tvPlayTitle).text = currentDramaName
        findViewById<TextView>(R.id.tvPlayDetails).text = "$currentVenue | $currentDateTime"

        setupData()
        setupRecyclerViews()
        setupSearch()

        findViewById<CardView>(R.id.cardPlayOfDay).setOnClickListener {
            showPlayOfDayDetails()
        }

        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_booking -> {
                    startActivity(Intent(this, BookingHistoryActivity::class.java))
                    true
                }
                R.id.nav_fanwall -> {
                    startActivity(Intent(this, FanWallActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, MyProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        findViewById<android.widget.Button>(R.id.btnSeats).setOnClickListener {
            openSeatBooking()
        }

        findViewById<android.widget.Button>(R.id.btnArtists).setOnClickListener {
            startActivity(Intent(this, ArtistActivity::class.java))
        }
    }

    private fun openSeatBooking() {
        val intent = Intent(this, SeatActivity::class.java)
        intent.putExtra("dramaName", currentDramaName)
        intent.putExtra("venue", currentVenue)
        intent.putExtra("address", currentAddress)
        intent.putExtra("location", currentLocation)
        intent.putExtra("dateTime", currentDateTime)
        intent.putExtra("geoLocation", currentGeoLocation)
        startActivity(intent)
    }

    private fun setupData() {
        // Expanded Data with unique posters per category (Industry Ready Dashboard)
        // Added 2 more items to each category as requested
        val comedy = listOf(
            Drama(1, "Huchcha Venkat Comedy", "Gubbi Troupes", R.drawable.c1__2_, "Comedy"),
            Drama(2, "Master Anand Show", "KBR Drama", R.drawable.c2__2_, "Comedy"),
            Drama(3, "Comedy Kiladigalu Live", "Shivamogga Troupes", R.drawable.c3__2_, "Comedy"),
            Drama(17, "Hasya Sanje", "Star Mela", R.drawable.c4, "Comedy"),
            Drama(18, "Rural Giggles", "Village Kings", R.drawable.c5, "Comedy"),
            Drama(23, "Laughter Express", "Namma Troupes", R.drawable.c6, "Comedy"),
            Drama(24, "Hasya Sangama", "Uttara Karnataka Arts", R.drawable.c7, "Comedy"),
            Drama(29, "Silly Lallu Live", "Modern Arts", R.drawable.c8, "Comedy"),
            Drama(30, "Papa Pandu Special", "Laughter Club", R.drawable.c9, "Comedy"),
            Drama(35, "Comedy Circus", "Urban Troupes", R.drawable.c10, "Comedy"),
            Drama(36, "Malgudi Jokes", "Heritage Arts", R.drawable.c1, "Comedy"), // NEW
            Drama(41, "Village Laughter", "Rural Mela", R.drawable.c2, "Comedy")  // NEW
        )

        val trending = listOf(
            Drama(5, "Kantara Stage Play", "B. Jayashree Troupes", R.drawable.t1__2_, "Trending"),
            Drama(6, "Sangolli Rayanna", "Belagavi Nataka Sangha", R.drawable.t2__2_, "Trending"),
            Drama(7, "Kurukshetra Live", "Mythological Troupes", R.drawable.t3, "Trending"),
            Drama(19, "Babruvahana", "Classic Arts", R.drawable.t4, "Trending"),
            Drama(20, "Satya Harishchandra", "Old School Theatre", R.drawable.t5, "Trending"),
            Drama(25, "Tipu Sultan", "Mysore Troupes", R.drawable.t6, "Trending"),
            Drama(26, "Rana Pratapa", "Heritage Theatre", R.drawable.t7, "Trending"),
            Drama(31, "Kittur Chennamma", "Veera Sangha", R.drawable.t8, "Trending"),
            Drama(32, "Basavanna Drama", "Lingayat Arts", R.drawable.t9, "Trending"),
            Drama(37, "Akka Mahadevi", "Siddaganga Arts", R.drawable.t10, "Trending"),
            Drama(42, "Kadamba Kings", "Banavasi Troupes", R.drawable.t1, "Trending"), // NEW
            Drama(43, "Hoysala Pride", "Belur Theatre", R.drawable.t2, "Trending") // NEW
        )

        val recent = listOf(
            Drama(9, "Malgudi Days Drama", "Shankar Nag Troupes", R.drawable.r1__2_, "Recent"),
            Drama(10, "Anna Thangi Sentiments", "Mandya Troupes", R.drawable.r2__2_, "Recent"),
            Drama(11, "Yajamana Stage", "Appu Fans Theatre", R.drawable.r3__2_, "Recent"),
            Drama(21, "Vikrant Rona Live", "Mysuru Troupes", R.drawable.r4, "Recent"),
            Drama(22, "Navagraha Live", "Darshan Fans Club", R.drawable.r5, "Recent"),
            Drama(27, "Jogi Theatrical", "Star Arts", R.drawable.r6, "Recent"),
            Drama(28, "Kranti Stage", "Modern Troupes", R.drawable.r7, "Recent"),
            Drama(33, "Lucia Stage", "New Gen Theatre", R.drawable.r8, "Recent"),
            Drama(34, "U-Turn Play", "Suspense Troupes", R.drawable.r9, "Recent"),
            Drama(39, "RangiTaranga Stage", "Coastal Arts", R.drawable.r10, "Recent"),
            Drama(40, "777 Charlie Drama", "Animal Love Mela", R.drawable.r11, "Recent"),
            Drama(44, "KGF Chapter 3", "Action Theatre", R.drawable.r2, "Recent"), // NEW
            Drama(45, "Kantari 2", "Divine Arts", R.drawable.r1, "Recent")         // NEW
        )

        allDramas.clear()
        allDramas.addAll(comedy)
        allDramas.addAll(trending)
        allDramas.addAll(recent)
    }

    private fun setupRecyclerViews() {
        val rvComedy = findViewById<RecyclerView>(R.id.rvComedy)
        val rvTrending = findViewById<RecyclerView>(R.id.rvTrending)
        val rvRecent = findViewById<RecyclerView>(R.id.rvRecent)

        dramaAdapterComedy = DramaAdapter(this, allDramas.filter { it.category == "Comedy" })
        dramaAdapterTrending = DramaAdapter(this, allDramas.filter { it.category == "Trending" })
        dramaAdapterRecent = DramaAdapter(this, allDramas.filter { it.category == "Recent" })

        rvComedy.adapter = dramaAdapterComedy
        rvTrending.adapter = dramaAdapterTrending
        rvRecent.adapter = dramaAdapterRecent
    }

    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDramas(s.toString().lowercase())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterDramas(query: String) {
        val filteredComedy = allDramas.filter { 
            it.category == "Comedy" && (it.name.lowercase().contains(query) || it.company.lowercase().contains(query)) 
        }
        val filteredTrending = allDramas.filter { 
            it.category == "Trending" && (it.name.lowercase().contains(query) || it.company.lowercase().contains(query)) 
        }
        val filteredRecent = allDramas.filter { 
            it.category == "Recent" && (it.name.lowercase().contains(query) || it.company.lowercase().contains(query)) 
        }

        dramaAdapterComedy.updateList(filteredComedy)
        dramaAdapterTrending.updateList(filteredTrending)
        dramaAdapterRecent.updateList(filteredRecent)
    }

    private fun showPlayOfDayDetails() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_play_details, null)
        val builder = AlertDialog.Builder(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        builder.setView(dialogView)

        val dialog = builder.create()

        dialogView.findViewById<ImageView>(R.id.imgClose).setOnClickListener { dialog.dismiss() }
        dialogView.findViewById<TextView>(R.id.tvDialogTitle).text = currentDramaName
        dialogView.findViewById<TextView>(R.id.tvDialogVenue).text = "🏛️ Venue: $currentVenue"
        dialogView.findViewById<TextView>(R.id.tvDialogLocation).text = "🌍 Location: $currentLocation"
        dialogView.findViewById<TextView>(R.id.tvDialogDuration).text = "🕒 Date: $currentDateTime"
        dialogView.findViewById<TextView>(R.id.tvCastList).text = "Lead Actor: Ramesh Arvind\nComedian: Master Anand\nSinger: Singer Sunitha\nVillain: Sai Kumar"

        dialogView.findViewById<android.widget.Button>(R.id.btnBookNow).setOnClickListener {
            dialog.dismiss()
            openSeatBooking()
        }

        dialog.show()
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
    
    override fun onResume() {
        super.onResume()
        loadLocale()
    }
}
