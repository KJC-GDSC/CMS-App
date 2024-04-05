package com.kjc.cms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kjc.cms.R
import com.kjc.cms.model.AboutData

class AboutAdapter() : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private val differCallBack = object : DiffUtil.ItemCallback<AboutData>() {
        override fun areItemsTheSame(oldItem: AboutData, newItem: AboutData): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: AboutData, newItem: AboutData): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    fun saveData(dataResponse: List<AboutData>) {
        differ.submitList(dataResponse)
    }

    class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.domain)
        val tech: TextView = itemView.findViewById(R.id.tech_details)
        val contributors: TextView = itemView.findViewById(R.id.contributors_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.about_groups, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val AboutItem = differ.currentList[position]
        holder.title.text = AboutItem.title
        for(tech in AboutItem.tech){
            val text = holder.tech.text.toString()
            holder.tech.text =  text + tech + "\n"
        }
        for(contributor in AboutItem.contributor){
            val text = holder.contributors.text.toString()
            holder.contributors.text =  text + contributor + "\n"
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
