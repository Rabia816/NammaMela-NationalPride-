package com.example.nammamela.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seats")
data class Seat(
    @PrimaryKey val seatNo: String,
    var isBooked: Boolean = false,
    var bookedBy: String = ""
)
