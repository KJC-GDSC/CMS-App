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
import com.kjc.cms.model.Component

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<Component>() {
        override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this,differCallBack)

    fun saveData( dataResponse: List<Component>){
        differ.submitList(dataResponse)
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val componentName : TextView = itemView.findViewById(R.id.grid_item_name)
        val componentImage : ImageView = itemView.findViewById(R.id.grid_item_image)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_grid_item, parent, false))
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        val homeItem = differ.currentList[position]
        holder.componentName.text = homeItem.Name
        Glide.with(holder.itemView.context).load(homeItem.Image).into(holder.componentImage)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}