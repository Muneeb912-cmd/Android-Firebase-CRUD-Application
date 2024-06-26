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
        val cardButton:Button=itemView.findViewById<Button>(R.id.cardButton)

        init {
            itemView.setOnClickListener(this)
            cardButton.setOnClickListener (this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v == cardButton) {
                    listener.onButtonClick(position) // Implement this interface method
                } else {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_card_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        Glide.with(holder.rvImage.context)
            .load(currentItem.imgId)
            .into(holder.rvImage)
        holder.rvTitle.text = currentItem.cardTitle
        holder.rvImageCaption.text = currentItem.imgCaption
        holder.rvStartTime.text = currentItem.startTime.toString()
        holder.rvEndTime.text = currentItem.endTime.toString()
        holder.rvProgress.progress = currentItem.progress
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
