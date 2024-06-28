package ViewModels

import Models.CardDataRepository
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import DataClass.DataClass
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CardDataViewModel(private val repository: CardDataRepository) : ViewModel() {

    val cardData: LiveData<List<DataClass>> = repository.getTasks()

    private val _filteredCardData = MutableLiveData<List<DataClass>?>()
    val filteredCardData: LiveData<List<DataClass>?> get() = _filteredCardData

    fun addTask(cardData: DataClass, imageUri: Uri) = viewModelScope.launch {
        repository.uploadImageAndAddTask(cardData, imageUri)
    }

    fun updateTask(cardData: DataClass, imageUri: Uri) = viewModelScope.launch {
        repository.uploadImageAndUpdateTask(cardData, imageUri)
    }

    fun deleteTask(taskId: String) = viewModelScope.launch {
        repository.delete(taskId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterDataByTime(filterByTime: String) {
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        val filterTime = LocalTime.parse(filterByTime, timeFormatter)

        val filteredList = cardData.value?.filter {
            val startTime = LocalTime.parse(it.startTime, timeFormatter)
            val endTime = LocalTime.parse(it.endTime, timeFormatter)

            filterTime.isAfter(startTime) && filterTime.isBefore(endTime) ||
                    filterTime == startTime || filterTime == endTime
        }

        _filteredCardData.value = filteredList ?: emptyList()
    }
}
