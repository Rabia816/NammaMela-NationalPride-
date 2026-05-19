

package com.example.nammamela.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nammamela.data.db.entities.Play

@Dao
interface Playdao {

    @Query("SELECT * FROM play WHERE id = 1")
    fun getPlay(): LiveData<Play>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(play: Play)
}