package com.example.nammamela.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammamela.data.db.DatabaseProvider
import com.example.nammamela.data.db.entities.User
import com.example.nammamela.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: UserRepository

    init {

        val dao = DatabaseProvider
            .getDatabase(application)
            .userDao()

        repository = UserRepository(dao)
    }

    fun registerUser(user: User) {

        viewModelScope.launch {

            repository.registerUser(user)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): User? {

        return repository.login(email, password)
    }
}