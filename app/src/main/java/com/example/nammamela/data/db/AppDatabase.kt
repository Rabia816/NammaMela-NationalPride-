package com.example.nammamela.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nammamela.data.db.dao.Seatdao
import com.example.nammamela.data.db.dao.UserDao
import com.example.nammamela.data.db.entities.Seat
import com.example.nammamela.data.db.entities.User

@Database(
    entities = [
        User::class,
        Seat::class
    ],
    version = 4
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun seatDao(): Seatdao
}