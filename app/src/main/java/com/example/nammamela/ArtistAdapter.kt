package com.example.nammamela

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtistAdapter(
    private val list: List<Artist>
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imgArtist =
            view.findViewById<ImageView>(R.id.imgArtist)

        val tvName =
            view.findViewById<TextView>(R.id.tvName)

        val tvRole =
            view.findViewById<TextView>(R.id.tvRole)

        val tvDetails =
            view.findViewById<TextView>(R.id.tvDetails)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val artist = list[position]

        holder.imgArtist.setImageResource(artist.image)

        holder.tvName.text = artist.name

        holder.tvRole.text = artist.role

        holder.tvDetails.text =
            """
Age: ${artist.age}

City: ${artist.city}

Experience: ${artist.experience}

Awards: ${artist.awards}

Language: ${artist.language}

Speciality: ${artist.speciality}

Fans: ${artist.fans}
            """.trimIndent()
    }
}