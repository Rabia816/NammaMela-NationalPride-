package com.example.nammamela

data class Drama(
    val id: Int,
    val name: String,
    val company: String,
    val posterResId: Int,
    val category: String, // Comedy, Trending, Recent
    val duration: String = "3 Hours",
    var isSaved: Boolean = false,
    val venue: String = "Shivamogga Town Hall",
    val address: String = "Near Gandhi Square, Ward 5",
    val location: String = "Shivamogga",
    val dateTime: String = "Tonight | 9:00 PM"
)
