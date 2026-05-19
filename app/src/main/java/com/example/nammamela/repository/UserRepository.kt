package com.example.nammamela.repository

import com.example.nammamela.data.db.dao.UserDao
import com.example.nammamela.data.db.entities.User

class UserRepository(
    private val userDao: UserDao
) {

    // REGISTER USER

    suspend fun registerUser(user: User) {

        userDao.registerUser(user)
    }

    // LOGIN USER

    suspend fun login(
        email: String,
        password: String
    ): User? {

        return userDao.login(email, password)
    }
}