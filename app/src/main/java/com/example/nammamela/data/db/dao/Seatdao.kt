package com.example.nammamela.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nammamela.data.db.entities.Seat

@Dao
interface Seatdao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeat(seat: Seat)

    @Query("SELECT * FROM seats")
    suspend fun getAllSeats(): List<Seat>

    @Query("SELECT * FROM seats WHERE bookedBy = :email")
    suspend fun getUserBookings(email: String): List<Seat>
}