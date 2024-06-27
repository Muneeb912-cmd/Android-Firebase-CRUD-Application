package com.example.week3_challenge

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CardDataViewModel(private val repository: CardDataRepository):ViewModel() {
    val cardData: LiveData<List<DataClass>> = repository.getTasks()

    fun addTask(cardData: DataClass,imageUri: Uri) = viewModelScope.launch {
        repository.uploadImageAndAddTask(cardData,imageUri)
    }

    fun updateTask(cardData: DataClass,imageUri: Uri) = viewModelScope.launch {
        repository.uploadImageAndUpdateTask(cardData,imageUri)
    }

    fun deleteTask(taskId: String) = viewModelScope.launch {
        repository.delete(taskId)
    }
}