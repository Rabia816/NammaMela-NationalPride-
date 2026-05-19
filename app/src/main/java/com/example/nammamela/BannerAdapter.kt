package com.example.nammamela

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BannerAdapter(
    private val list: List<Banner>
) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val image =
            view.findViewById<ImageView>(R.id.imgBanner)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.image.setImageResource(list[position].image)
    }
}