package com.kjc.cms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kjc.cms.R
import com.kjc.cms.model.BookingHistory

class HistoryAdapter(private val historyItemsList: ArrayList<BookingHistory>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val componentName : TextView = itemView.findViewById(R.id.history_item_name)
        val componentQuantity : TextView = itemView.findViewById(R.id.history_item_quantity)
        val componentOrderedOn : TextView = itemView.findViewById(R.id.history_item_ordered_on)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyItemsList[position]

        holder.componentName.text = currentItem.Component["Name"].toString()
        holder.componentQuantity.text = currentItem.Component["Quantity"].toString()
        holder.componentOrderedOn.text = currentItem.BookedOn.toString()
    }

    override fun getItemCount(): Int {
        return historyItemsList.size
    }
}