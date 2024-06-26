package com.example.week3_challenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]

        // Create a storage reference from the gs:// URI
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.imgId)

        // Get the download URL
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            // Use Glide to load the image from the download URL
            Glide.with(holder.rvImage.context)
                .load(uri.toString())
                .into(holder.rvImage)
        }.addOnFailureListener {
            // Handle any errors
            holder.rvImage.setImageResource(R.drawable.ic_photo) // Set a placeholder image or handle error
        }

        holder.rvTitle.text = currentItem.cardTitle
        holder.rvImageCaption.text = currentItem.imgCaption
        holder.rvStartTime.text = currentItem.startTime
        holder.rvEndTime.text = currentItem.endTime
        holder.rvProgress.progress = currentItem.progress
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
