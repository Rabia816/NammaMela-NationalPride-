package com.example.nammamela.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "play")
data class Play(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val duration: String,
    val posterUrl: String
)