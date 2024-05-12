package com.kjc.cms.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjc.cms.R
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.kjc.cms.model.CartComponent
import com.kjc.cms.model.Component

class PDFTableRowAdapter(private var tableItem: ArrayList<CartComponent>) : RecyclerView.Adapter<PDFTableRowAdapter.PDFViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<CartComponent>() {
        override fun areItemsTheSame(oldItem: CartComponent, newItem: CartComponent): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CartComponent, newItem: CartComponent): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,differCallBack)

    fun saveData(dataResponse: List<CartComponent>){
        differ.submitList(dataResponse)
    }

    class PDFViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slNo: TextView = itemView.findViewById(R.id.slNo)
        val name: TextView = itemView.findViewById(R.id.rowItemName)
        val model: TextView = itemView.findViewById(R.id.rowItemModel)
        val quantity: TextView = itemView.findViewById(R.id.rowItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDFViewHolder {
        return PDFViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pdf_table_rows, parent, false))
    }

    override fun onBindViewHolder(holder: PDFTableRowAdapter.PDFViewHolder, position: Int) {
        val tableItem = differ.currentList[position]
        holder.slNo.text = (position+1).toString()
        holder.name.text = tableItem.name
        holder.model.text = tableItem.model
        holder.quantity.text = tableItem.quantity.toString()
    }

    override fun getItemCount(): Int {
        return tableItem.size
    }
}