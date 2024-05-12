package com.kjc.cms.ui.fragments.home

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.kjc.cms.R
import com.kjc.cms.adapter.ConfirmCartAdapter
import com.kjc.cms.adapter.PDFTableRowAdapter
import com.kjc.cms.databinding.FragmentConfirmBookingBinding
import com.kjc.cms.model.CartComponent
import com.kjc.cms.model.CurrentUser
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class ConfirmBookingFragment(private var itemsList: MutableSet<String>?, private var editor: Editor, private var sp: SharedPreferences, private var userSp: SharedPreferences) : Fragment() {
    private lateinit var binding: FragmentConfirmBookingBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var confirmCartItemList: ArrayList<CartComponent>
    private lateinit var confirmCartAdapter: ConfirmCartAdapter
    private lateinit var tableRowAdapter: PDFTableRowAdapter
    private lateinit var tableRowRecyclerView: RecyclerView
    private lateinit var user: CurrentUser
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
        firestore = FirebaseFirestore.getInstance()
        confirmCartAdapter = ConfirmCartAdapter()
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
            createPDF()
        }
    }

    private fun fetchConfirmedCart() {
        val itemList = convertToComponentArray()
        confirmCartAdapter.saveData(itemList)
    }

    private fun convertToComponentArray(): ArrayList<CartComponent> {
        val cartItems = itemsList
        val arrayItem = arrayListOf<CartComponent>()
        cartItems?.forEach { item->
            arrayItem.add(Gson().fromJson(item, CartComponent::class.java))
        }
        return arrayItem
    }

    private fun createPDF() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.pdf_layout, null)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(1550, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(1135, View.MeasureSpec.EXACTLY))
        view.layout(0,0, view.measuredWidth, view.measuredHeight)

        user = Gson().fromJson(userSp.getString("currentUser", "NO USER"), CurrentUser::class.java)

        val personName: TextView = view.findViewById(R.id.personName)
        val eventName: TextView = view.findViewById(R.id.eventName)
        val department: TextView = view.findViewById(R.id.personDepartment)
        val bookingDate: TextView = view.findViewById(R.id.booking_Date)
        val returnDate: TextView = view.findViewById(R.id.return_date)
        personName.text = user.Name
        eventName.text = binding.eventName.text
        department.text = user.Department
        bookingDate.text = binding.bookingDate.text
        returnDate.text = binding.returnDate.text

        tableRowRecyclerView = view.findViewById(R.id.table_recycler)
        val itemList = convertToComponentArray()
        tableRowAdapter = PDFTableRowAdapter(itemList)
        tableRowRecyclerView.layoutManager = LinearLayoutManager(context)
        tableRowRecyclerView.adapter = tableRowAdapter

        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(1550, 1135, 1).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas
        view.draw(canvas)
        pdfDoc.finishPage(page)

        downloadAndStorePDF(eventName, pdfDoc)
    }

    private fun downloadAndStorePDF(eventName: TextView, pdfDoc: PdfDocument) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "${eventName.text}.pdf"
        val file = File(downloadDir, fileName)
        try {
            val fos = FileOutputStream(file)
            pdfDoc.writeTo(fos)
            pdfDoc.close()
            fos.close()
            firestore.collection("History").document().set(
                hashMapOf( "BookedOn" to binding.bookingDate.text,
                "Component" to itemsList,
                "EventName" to eventName,
                "ReturnOn" to binding.returnDate.text,
                "userEmail" to user.Email
                )).addOnSuccessListener { Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show() }
        } catch (e: FileNotFoundException) {
            Log.d("file exception", e.toString())
        } catch (e: IOException) {
            Log.d("IO exception", e.toString())
        }
    }
}