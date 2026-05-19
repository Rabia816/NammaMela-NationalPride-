package com.example.nammamela.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nammamela.data.db.entities.Comment

@Dao
interface Commentdao {

    @Query("SELECT * FROM comment")
    fun getComments(): LiveData<List<Comment>>

    @Insert
    suspend fun insert(comment: Comment)
}