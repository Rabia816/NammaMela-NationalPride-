package com.example.nammamela.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor")
data class Actor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val role: String,
    val imageUrl: String
)