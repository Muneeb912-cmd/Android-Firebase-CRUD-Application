package com.example.week3_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CardDataViewModelFactory(private val repository: CardDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardDataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
