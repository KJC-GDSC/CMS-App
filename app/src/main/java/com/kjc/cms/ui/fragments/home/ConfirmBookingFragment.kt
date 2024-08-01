package com.kjc.cms.ui.fragments.home

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var confirmCartItemList: ArrayList<CartComponent>
    private lateinit var confirmCartAdapter: ConfirmCartAdapter

    private lateinit var firestore: FirebaseFirestore

    private lateinit var tableRowAdapter: PDFTableRowAdapter
//    private lateinit var tableRowRecyclerView: RecyclerView

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
        binding.bookingDate.text = String.format("%s/%s/%s",day,month,year)
        binding.returnDate.text = String.format("%s/%s/%s",day+1,month,year)

        binding.bookingDate.setOnClickListener{
            DatePickerDialog(requireContext(),
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    binding.bookingDate.text = String.format("%s/%s/%s",day,month,year)
                },
                year, month, day).
            show()
        }
        binding.returnDate.setOnClickListener{
            DatePickerDialog(requireContext(),
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    binding.returnDate.text = String.format("%s/%s/%s",day,month,year)
                },
                year, month, day+1).
            show()
        }
        binding.downloadPDF.setOnClickListener{
            createPDF()
        }
    }

    private fun fetchConfirmedCart() {
        val list = convertToComponentArray()
        confirmCartAdapter.saveData(list)
    }

    private fun convertToComponentArray(): ArrayList<CartComponent> {
        val cartItems = itemsList
        val arrayItem = arrayListOf<CartComponent>()
        cartItems?.forEach { item ->
            arrayItem.add(Gson().fromJson(item, CartComponent::class.java))
        }
        Log.d("lakjf", arrayItem.toString())
        return arrayItem
    }

    private fun createPDF() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.pdf_layout, null)
        view.measure(View.MeasureSpec.makeMeasureSpec(1550, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(1135, View.MeasureSpec.EXACTLY))
        view.layout(0,0, view.measuredWidth, view.measuredHeight)

        user = Gson().fromJson(userSp.getString("currentUser", "NO USER"), CurrentUser::class.java)
        val pageWidth = 1550
        val pageHeight = 1135

        val personName: TextView = view.findViewById(R.id.personName)
        val eventName: TextView = view.findViewById(R.id.eventName)
        val department: TextView = view.findViewById(R.id.personDepartment)
        val bookingDate: TextView = view.findViewById(R.id.booking_Date)
        val returnDate: TextView = view.findViewById(R.id.return_date)
        val table: TableLayout = view.findViewById(R.id.pdfTable)
        personName.text = user.Name
        eventName.text = binding.eventName.text
        department.text = user.Department
        bookingDate.text = binding.bookingDate.text
        returnDate.text = binding.returnDate.text

        val bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.ARGB_8888)
        val bitCanvas = Canvas(bitmap)

        val itemList = convertToComponentArray()
//        tableRowRecyclerView = view.findViewById(R.id.table_recycler)
//        tableRowRecyclerView.layoutManager = LinearLayoutManager(context)
//        tableRowRecyclerView.hasFixedSize()
//        tableRowAdapter = PDFTableRowAdapter()
//        tableRowRecyclerView.adapter = tableRowAdapter
//        tableRowAdapter.saveData(itemList)


        val pdfDoc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDoc.startPage(pageInfo)
        val canvas = page.canvas
        for(i in 0 until itemList.size){
            val row = addDataToTable(i+1, itemList[i], bitCanvas, table)
            table.addView(row)
            Log.d("row", table.findViewById<TextView>(R.id.rowItemName).text.toString())
        }
        view.draw(canvas)
//        table.draw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
//        tableRowRecyclerView.draw(canvas)
        pdfDoc.finishPage(page)

        downloadAndStorePDF(pdfDoc, itemList)
    }

    private fun addDataToTable(i: Int, itemList: CartComponent, canvas: Canvas, table: TableLayout): TableRow {
        val tableRowView = LayoutInflater.from(context).inflate(R.layout.pdf_table_rows, null) as TableRow

        tableRowView.findViewById<TextView>(R.id.slNo).text = i.toString()
        tableRowView.findViewById<TextView>(R.id.rowItemName).text = itemList.name
        tableRowView.findViewById<TextView>(R.id.rowItemModel).text = itemList.model
        tableRowView.findViewById<TextView>(R.id.rowItemQuantity).text = itemList.quantity.toString()

        return tableRowView
//        tableRowView.draw(canvas)
    }

    private fun downloadAndStorePDF(pdfDoc: PdfDocument, itemList: ArrayList<CartComponent>) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "${binding.eventName.text}.pdf"
        val file = File(downloadDir, fileName)
        try {
            val fos = FileOutputStream(file)
            pdfDoc.writeTo(fos)
            pdfDoc.close()
            fos.close()
            firestore.collection("History").document().set(
                hashMapOf(
                    "BookedOn" to binding.bookingDate.text.toString(),
                    "Component" to itemList,
                    "EventName" to binding.eventName.text.toString(),
                    "ReturnOn" to binding.returnDate.text.toString(),
                    "userEmail" to user.Email
                )).addOnSuccessListener { Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show() }
        } catch (e: FileNotFoundException) {
            Log.d("file exception", e.toString())
        } catch (e: IOException) {
            Log.d("IO exception", e.toString())
        }
    }
}