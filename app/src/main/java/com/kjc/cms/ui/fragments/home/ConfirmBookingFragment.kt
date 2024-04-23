package com.kjc.cms.ui.fragments.home

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kjc.cms.R
import com.kjc.cms.adapter.ConfirmCartAdapter
import com.kjc.cms.databinding.FragmentConfirmBookingBinding
import com.kjc.cms.model.CartComponent
import java.util.Calendar

class ConfirmBookingFragment(private var itemsList: MutableSet<String>?, private var editor: Editor, private var sp: SharedPreferences) : Fragment() {
    private lateinit var binding: FragmentConfirmBookingBinding
    private lateinit var confirmCartItemList: ArrayList<CartComponent>
    private lateinit var confirmCartAdapter: ConfirmCartAdapter
    val calender = Calendar.getInstance()
    var day = calender.get(Calendar.DAY_OF_MONTH)
    var month = calender.get(Calendar.MONTH)
    var year = calender.get(Calendar.YEAR)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConfirmBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        confirmCartAdapter = ConfirmCartAdapter()
        Log.d("dksloes", "does it work here")
        binding.recyclerView.adapter = confirmCartAdapter
        confirmCartItemList = arrayListOf()
        fetchConfirmedCart()
        binding.bookingDate.setOnClickListener{
            DatePickerDialog(requireContext(),
                { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                    binding.bookingDate.text = "$day/$month/$year"
                },
                year, month, day).
            show()
        }
        binding.returnDate.setOnClickListener{
            DatePickerDialog(requireContext(),
                { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                    binding.returnDate.text = "$day/$month/$year"
                },
                year, month, day+1).
            show()
        }
        binding.downloadPDF.setOnClickListener{
            downloadPDF()
        }
    }

    private fun fetchConfirmedCart() {
        val cartItems = itemsList
        val arrayItem = arrayListOf<CartComponent>()
        cartItems?.forEach { item->
            arrayItem.add(Gson().fromJson(item, CartComponent::class.java))
        }
        confirmCartAdapter.saveData(arrayItem)
    }

    private fun downloadPDF() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.pdf_layout, null)
        val displayMetrics: DisplayMetrics
        
        editor.putString("Event Name", binding.eventName.text.toString())
        editor.putString("Booking Date", binding.bookingDate.text.toString())
        editor.putString("Return Date", binding.returnDate.text.toString())
        
        TODO("create pdf function")
    }
}