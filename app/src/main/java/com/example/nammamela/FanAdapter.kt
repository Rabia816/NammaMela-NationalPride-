package com.example.nammamela

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FanAdapter(

    private val list: ArrayList<Post>

) : RecyclerView.Adapter<FanAdapter.PostViewHolder>() {

    class PostViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val textPost =
            view.findViewById<TextView>(
                android.R.id.text1
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
                )

        return PostViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {

        holder.textPost.text =
            list[position].text
    }

    override fun getItemCount(): Int {

        return list.size
    }
}