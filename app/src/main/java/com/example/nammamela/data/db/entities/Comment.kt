package com.example.nammamela.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String
)