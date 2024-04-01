package com.kjc.cms.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kjc.cms.adapter.HomeAdapter
import com.kjc.cms.databinding.FragmentHomeBinding
import com.kjc.cms.model.Component

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var collectionReference: CollectionReference
    private lateinit var homeItemList: ArrayList<Component>
    private lateinit var homeAdapter: HomeAdapter
    private var homeRecycler : RecyclerView? = null

    //TODO: On click listener on each adapter item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeRecycler?.layoutManager = GridLayoutManager(context, 2)
        homeAdapter = HomeAdapter()
        binding.homeGridRecycler.adapter = homeAdapter
        homeItemList = arrayListOf()
        getAllComponents()
    }

    private fun getAllComponents() {
        collectionReference = FirebaseFirestore.getInstance().collection("Components")
        collectionReference.get().addOnSuccessListener { result ->
            for (doc in result) {
                val component = Component(
                    Id = doc.id,
                    AvailableQuantity= doc.data["AvailableQuantity"].toString(),
                    Image= doc.data["Image"].toString(),
                    LastUpdated= doc.data["LastUpdated"].toString(),
                    Model= doc.data["Model"].toString(),
                    Name= doc.data["Name"].toString(),
                    Price= doc.data["Price"].toString(),
                    Quantity= doc.data["Quantity"].toString())
                homeItemList.add(component)
            }
            homeAdapter.saveData(homeItemList)
        }.addOnFailureListener { exception ->
            Log.d("FireStore Error", exception.toString())
        }
    }
}