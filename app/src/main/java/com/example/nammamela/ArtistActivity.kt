package com.example.nammamela

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_artist)

        val recycler =
            findViewById<RecyclerView>(R.id.recyclerArtists)

        recycler.layoutManager =
            LinearLayoutManager(this)

        val artistList = listOf(

            Artist(
                R.drawable.a1,
                "Ramesh",
                "Lead Hero",
                "32",
                "Bangalore",
                "10 Years",
                "Best Actor",
                "Kannada",
                "Action Drama",
                "1M Fans"
            ),

            Artist(
                R.drawable.a3,
                "Umashree",
                "Comedian",
                "55",
                "Mysore",
                "25 Years",
                "Comedy Star",
                "Kannada",
                "Comedy",
                "800K Fans"
            ),

            Artist(
                R.drawable.a2,
                "Kiran",
                "Singer",
                "35",
                "Hubli",
                "12 Years",
                "Best Singer",
                "Kannada",
                "Folk Singing",
                "2M Fans"
            ),
            Artist(
                R.drawable.a4,
                "narsimha",
                "villain",
                "35",
                "Jamkandi",
                "10 Years",
                "Best negative character",
                "Kannada",
                "villain",
                "1M Fans"
        )
        )

        recycler.adapter =
            ArtistAdapter(artistList)
    }
}