package Adapters

import DataClass.DataClass
import Models.CardDataRepository
import ViewModels.CardDataViewModel
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week3_challenge.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RecyclerViewAdapter(
    private val dataList: ArrayList<DataClass>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onButtonClick(position: Int)
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rvImage: ImageView = itemView.findViewById(R.id.image)
        val rvImageCaption: TextView = itemView.findViewById(R.id.imgCaption)
        val rvTitle: TextView = itemView.findViewById(R.id.title)
        val rvStartTime: TextView = itemView.findViewById(R.id.startTime)
        val rvEndTime: TextView = itemView.findViewById(R.id.endTime)
        val rvProgress: ProgressBar = itemView.findViewById(R.id.linearProgressbar)
        val cardButton: Button = itemView.findViewById(R.id.cardButton)

        init {
            itemView.setOnClickListener(this)
            cardButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v == cardButton) {
                    listener.onButtonClick(position)
                } else {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_card_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.imgId)
        // Get the download URL
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.rvImage.context)
                .load(uri.toString())
                .into(holder.rvImage)
        }.addOnFailureListener {
            holder.rvImage.setImageResource(R.drawable.ic_photo)
        }

        holder.rvTitle.text = currentItem.cardTitle
        holder.rvImageCaption.text = currentItem.imgCaption
        holder.rvStartTime.text = currentItem.startTime
        holder.rvEndTime.text = currentItem.endTime
        holder.rvProgress.progress = progressMonitoring(currentItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun progressMonitoring(cardData: DataClass): Int {
        val current = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val currentTime = current.format(timeFormatter)
        val startTime = LocalTime.parse(cardData.startTime, timeFormatter)
        val endTime = LocalTime.parse(cardData.endTime, timeFormatter)
        val currentLocalTime = LocalTime.parse(currentTime, timeFormatter)
        val totalDuration = Duration.between(startTime, endTime)
        val elapsedDuration = Duration.between(startTime, currentLocalTime)

        val progress = if (currentLocalTime.isBefore(startTime)) {
            0
        } else if (currentLocalTime.isAfter(endTime)) {
            100
        } else {
            (elapsedDuration.toMillis() * 100 / totalDuration.toMillis()).toInt()
        }

        return progress
    }
}
