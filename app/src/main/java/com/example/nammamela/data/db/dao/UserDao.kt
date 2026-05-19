package com.example.nammamela.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.nammamela.data.db.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUser(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(
        email: String,
        password: String
    ): User?
}