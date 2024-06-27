package com.example.week3_challenge

import android.net.Uri
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CardDataRepository {
    private val db =FirebaseFirestore.getInstance()
    private val cardDataCollection=db.collection("DummyData")
    private val storage=FirebaseStorage.getInstance()
    private val storageReference=storage.reference

    fun addTask(data: DataClass): DataClass {
        val document = cardDataCollection.document()
        data.id = document.id
        document.set(data)
        return data
    }

    fun updateTask(cardData: DataClass) {
        cardDataCollection.document(cardData.id).set(cardData)
    }

    fun delete(taskId: String) {
        cardDataCollection.document(taskId).delete().addOnFailureListener{
            Log.d("Delete", "Error Occured")
        }
    }

    fun getTasks(): LiveData<List<DataClass>> {
        val tasksLiveData = MutableLiveData<List<DataClass>>()
        cardDataCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                tasksLiveData.value = emptyList()
                return@addSnapshotListener
            }
            val tasks = snapshot.documents.mapNotNull { document ->
                try {
                    val data = document.toObject(DataClass::class.java)
                    data?.id = document.id
                    data
                } catch (e: Exception) {
                    Log.e("Firestore", "Error parsing document ${document.id}", e)
                    null
                }
            }
            tasksLiveData.value = tasks
        }
        return tasksLiveData
    }

    fun uploadImageAndAddTask(cardData: DataClass, imageUri: Uri) {
        val ref = storageReference.child("cardImages/${UUID.randomUUID()}")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    cardData.imgId = uri.toString()
                    addTask(cardData)
                }
            }
    }

    fun uploadImageAndUpdateTask(cardData: DataClass, imageUri: Uri) {
        val ref = storageReference.child("cardImages/${UUID.randomUUID()}")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    cardData.imgId = uri.toString()
                    updateTask(cardData)
                }
            }
            .addOnFailureListener {
                // Handle any errors
            }
    }

}