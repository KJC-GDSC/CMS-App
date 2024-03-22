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
import com.kjc.cms.adapter.HistoryAdapter
import com.kjc.cms.databinding.FragmentHistoryBinding
import com.kjc.cms.model.BookingHistory

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var collectionReference: CollectionReference
    private lateinit var historyItemList: ArrayList<BookingHistory>
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyRecycler.layoutManager = LinearLayoutManager(context)
        binding.historyRecycler.hasFixedSize()

        historyItemList = arrayListOf()
        getHistoryOfUser()
    }

    private fun getHistoryOfUser() {
        collectionReference = FirebaseFirestore.getInstance().collection("History")
        collectionReference.addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null)
                {
                    Log.e("FireStore error", error.message.toString())
                    return
                }
                // TODO: Add user specific History
                for (historyItemSnapshot in value?.documentChanges!!){
                    val historyItem = historyItemSnapshot.document.toObject(BookingHistory::class.java)
                    historyItemList.add(historyItem)
                }
                historyAdapter.notifyDataSetChanged()
                binding.historyRecycler.adapter = HistoryAdapter(historyItemList)
            }
        })
    }
}