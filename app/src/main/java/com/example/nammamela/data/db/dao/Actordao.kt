package com.example.nammamela.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nammamela.data.db.entities.Actor

@Dao
interface Actordao {

    @Query("SELECT * FROM actor")
    fun getActors(): LiveData<List<Actor>>

    @Insert
    suspend fun insert(actor: Actor)
}