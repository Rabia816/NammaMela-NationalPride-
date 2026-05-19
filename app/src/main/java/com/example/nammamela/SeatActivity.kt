package com.example.nammamela

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nammamela.data.db.entities.Seat
import com.google.firebase.database.*

class SeatActivity : AppCompatActivity() {

    private lateinit var seatList: MutableList<Seat>
    private lateinit var adapter: SeatAdapter
    private val database = FirebaseDatabase.getInstance().getReference("booked_seats")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerSeats)
        val btnConfirmPay = findViewById<Button>(R.id.btnConfirmPay)

        val dramaName = intent.getStringExtra("dramaName") ?: "Nann Aase Nataka"
        val venue = intent.getStringExtra("venue") ?: "Shivamogga Town Hall"
        val address = intent.getStringExtra("address") ?: "Near Gandhi Square, Ward 5"
        val location = intent.getStringExtra("location") ?: "Shivamogga"
        val dateTime = intent.getStringExtra("dateTime") ?: "Tonight | 9:00 PM"
        val geoLocation = intent.getStringExtra("geoLocation") ?: "13.9299,75.5681"

        seatList = mutableListOf()
        // Initialize empty seats
        for (row in 'A'..'J') {
            for (num in 1..5) {
                seatList.add(Seat(seatNo = "$row$num", isBooked = false, bookedBy = ""))
            }
        }

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        adapter = SeatAdapter(this, seatList)
        recyclerView.adapter = adapter

        // FETCH BOOKED SEATS FROM FIREBASE
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (row in 'A'..'J') {
                    for (num in 1..5) {
                        val seatNo = "$row$num"
                        val isBooked = snapshot.hasChild(seatNo)
                        val seat = seatList.find { it.seatNo == seatNo }
                        seat?.isBooked = isBooked
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        btnConfirmPay.setOnClickListener {
            val selectedSeats = seatList.filter { it.bookedBy == "selected" }

            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Select a seat first", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("seatNo", selectedSeats[0].seatNo) 
                intent.putExtra("dramaName", dramaName)
                intent.putExtra("venue", venue)
                intent.putExtra("address", address)
                intent.putExtra("location", location)
                intent.putExtra("dateTime", dateTime)
                intent.putExtra("geoLocation", geoLocation)
                startActivity(intent)
            }
        }
    }
}
