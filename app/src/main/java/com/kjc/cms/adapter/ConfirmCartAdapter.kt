package com.kjc.cms.adapter

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
import com.kjc.cms.model.CartComponent

class ConfirmCartAdapter() : RecyclerView.Adapter<ConfirmCartAdapter.ConfirmCartViewHolder>() {
    private val differCallBack = object: DiffUtil.ItemCallback<CartComponent>() {
        override fun areItemsTheSame(oldItem: CartComponent, newItem: CartComponent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartComponent, newItem: CartComponent): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,differCallBack)

    fun saveData( dataResponse: List<CartComponent>){
        differ.submitList(dataResponse)
    }

    class ConfirmCartViewHolder(itemVew: View): RecyclerView.ViewHolder(itemVew){
        val componentName: TextView = itemVew.findViewById(R.id.confirm_cart_name)
        val componentQuantity: TextView = itemVew.findViewById(R.id.confirm_cart_quantity)
        val componentIndex: TextView = itemVew.findViewById(R.id.cart_index)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmCartViewHolder {
        return ConfirmCartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.confirm_booking_item, parent, false))
    }

    override fun onBindViewHolder(holder: ConfirmCartViewHolder, position: Int) {
        val cartItem = differ.currentList[position]
        holder.componentQuantity.text = "x ${cartItem.quantity}"
        holder.componentName.text = cartItem.name
        holder.componentIndex.text = "${position+1}. "
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}