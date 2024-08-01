package com.kjc.cms.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kjc.cms.R
import com.kjc.cms.model.CartComponent

class PDFRowAdapter(private val arrayList: ArrayList<CartComponent>) {
    fun View(position: Int, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.pdf_table_rows, null)

        val slNo: TextView = view.findViewById(R.id.slNo)
        val name: TextView = view.findViewById(R.id.rowItemName)
        val model: TextView = view.findViewById(R.id.rowItemModel)
        val quantity: TextView = view.findViewById(R.id.rowItemQuantity)

        val tableItem = arrayList[position]
        slNo.text = (position+1).toString()
        name.text = tableItem.name
        model.text = tableItem.model
        quantity.text = tableItem.quantity.toString()
        Log.d("sdfghjk", tableItem.toString())

        return view
    }
}