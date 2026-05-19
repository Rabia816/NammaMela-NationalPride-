package com.example.nammamela

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DramaAdapter(
    private val context: Context,
    private var dramas: List<Drama>
) : RecyclerView.Adapter<DramaAdapter.DramaViewHolder>() {

    fun updateList(newList: List<Drama>) {
        dramas = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DramaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_drama, parent, false)
        return DramaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DramaViewHolder, position: Int) {
        val drama = dramas[position]
        holder.tvName.text = drama.name
        holder.tvCompany.text = drama.company
        holder.imgPoster.setImageResource(drama.posterResId)

        val prefs = context.getSharedPreferences("saved_dramas", Context.MODE_PRIVATE)
        drama.isSaved = prefs.getBoolean(drama.id.toString(), false)

        updateSaveIcon(holder.btnSave, drama.isSaved)

        holder.btnSave.setOnClickListener {
            drama.isSaved = !drama.isSaved
            prefs.edit().putBoolean(drama.id.toString(), drama.isSaved).apply()
            updateSaveIcon(holder.btnSave, drama.isSaved)
            val msg = if (drama.isSaved) "Saved to watchlist" else "Removed from watchlist"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        holder.btnTrailer.setOnClickListener {
            // Simulated trailer link - opens YouTube search for the drama
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=${drama.name}+drama+trailer"))
            context.startActivity(intent)
        }
    }

    private fun updateSaveIcon(btn: ImageButton, isSaved: Boolean) {
        if (isSaved) {
            btn.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else {
            btn.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }
    }

    override fun getItemCount(): Int = dramas.size

    class DramaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val btnSave: ImageButton = view.findViewById(R.id.btnSaveDrama)
        val tvName: TextView = view.findViewById(R.id.tvDramaName)
        val tvCompany: TextView = view.findViewById(R.id.tvCompany)
        val btnTrailer: Button = view.findViewById(R.id.btnTrailer)
    }
}
