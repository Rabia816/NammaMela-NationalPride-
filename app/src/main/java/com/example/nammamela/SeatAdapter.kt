package com.example.nammamela

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nammamela.data.db.entities.Seat

class SeatAdapter(
    private val context: Context,
    private val seatList: MutableList<Seat>
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    inner class SeatViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val button = Button(context)
        button.layoutParams = GridLayout.LayoutParams().apply {
            width = 150
            height = 150
            setMargins(8, 8, 8, 8)
        }
        button.textSize = 14f
        return SeatViewHolder(button)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seatList[position]
        holder.button.text = seat.seatNo

        updateSeatUI(holder.button, seat)

        holder.button.setOnClickListener {
            if (seat.isBooked) {
                Toast.makeText(context, "Seat Already Booked", Toast.LENGTH_SHORT).show()
            } else {
                // TOGGLE SELECTION
                if (seat.bookedBy == "selected") {
                    seat.bookedBy = ""
                } else {
                    // Deselect others if only single selection is allowed (Industry standard for simple apps)
                    seatList.forEach { if(it.bookedBy == "selected") it.bookedBy = "" }
                    seat.bookedBy = "selected"
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = seatList.size

    private fun updateSeatUI(button: Button, seat: Seat) {
        when {
            seat.isBooked -> {
                button.setBackgroundColor(Color.parseColor("#444444")) // Grey for booked
                button.setTextColor(Color.WHITE)
                button.isEnabled = false
            }
            seat.bookedBy == "selected" -> {
                button.setBackgroundColor(Color.parseColor("#FFD700")) // Gold for selected
                button.setTextColor(Color.BLACK)
            }
            else -> {
                button.setBackgroundColor(Color.parseColor("#2E7D32")) // Green for available
                button.setTextColor(Color.WHITE)
            }
        }
    }
}
