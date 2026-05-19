package com.example.nammamela.repository

import com.example.nammamela.data.db.dao.Seatdao
import com.example.nammamela.data.db.entities.Seat

class NammaRepository(
    private val Seatdao: Seatdao
) {

    suspend fun getSeats(): List<Seat> {
        return Seatdao.getAllSeats()
    }

    suspend fun updateSeat(seat: Seat) {
        Seatdao.insertSeat(seat)
    }
}