package com.kjc.cms.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.kjc.cms.adapter.HistoryAdapter
import com.kjc.cms.databinding.FragmentHistoryBinding
import com.kjc.cms.model.BookingHistory

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var collectionReference: CollectionReference
    private lateinit var historyItemList: ArrayList<BookingHistory>
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyRecycler.layoutManager = LinearLayoutManager(context)
        historyAdapter = HistoryAdapter()
        binding.historyRecycler.adapter = historyAdapter
        historyItemList = arrayListOf()
        getHistoryOfUser()
    }

    private fun getHistoryOfUser() {
        collectionReference = FirebaseFirestore.getInstance().collection("History")
        collectionReference.get().addOnSuccessListener { result ->
            for ( doc in result ){
                // TODO: Add user specific History
                val historyItems = doc.get("Component") as ArrayList<Map<String, String>>
                for(item in historyItems){
                    val i = BookingHistory(item["Id"], item["Image"], item["Name"], item["Quantity"])
                    historyItemList.add(i)
                }
            }
            historyAdapter.saveData(historyItemList)
        }.addOnFailureListener { exception ->
            Log.d("FireStore Error", exception.toString())
        }
    }
}