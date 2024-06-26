package com.example.week3_challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CardDataViewModel(private val repository: CardDataRepository):ViewModel() {
    val cardData: LiveData<List<DataClass>> = repository.getTasks()

    fun addTask(cardData: DataClass) = viewModelScope.launch {
        repository.addTask(cardData)
    }

    fun updateTask(cardData: DataClass) = viewModelScope.launch {
        repository.updateTask(cardData)
    }

    fun deleteTask(taskId: String) = viewModelScope.launch {
        repository.deleteTask(taskId)
    }
}