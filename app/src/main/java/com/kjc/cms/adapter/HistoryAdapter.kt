package com.kjc.cms.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kjc.cms.R
import com.kjc.cms.model.BookingHistory

class HistoryAdapter() : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<BookingHistory>() {
        override fun areItemsTheSame(oldItem: BookingHistory, newItem: BookingHistory): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: BookingHistory, newItem: BookingHistory): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,differCallBack)

    fun saveData( dataResponse: List<BookingHistory>){
        differ.submitList(dataResponse)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val componentName : TextView = itemView.findViewById(R.id.history_item_name)
        val componentQuantity : TextView = itemView.findViewById(R.id.history_item_quantity)
        val componentImage : ImageView = itemView.findViewById(R.id.history_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = differ.currentList[position]
        holder.componentQuantity.text = "Quantity"+historyItem.Quantity
        holder.componentName.text = historyItem.Name
        Glide.with(holder.itemView.context).load(historyItem.Image).into(holder.componentImage)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}