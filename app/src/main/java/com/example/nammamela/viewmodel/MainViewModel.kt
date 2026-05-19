package com.example.nammamela.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nammamela.data.db.entities.Seat

class MainViewModel : ViewModel() {

    val seats = MutableLiveData<List<Seat>>()

}