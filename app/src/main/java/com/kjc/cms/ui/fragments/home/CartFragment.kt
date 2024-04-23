package com.kjc.cms.ui.fragments.home

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kjc.cms.R
import com.kjc.cms.adapter.CartAdapter
import com.kjc.cms.databinding.FragmentAboutBinding
import com.kjc.cms.databinding.FragmentCartBinding
import com.kjc.cms.model.CartComponent
import com.kjc.cms.ui.SecondaryActivity
import com.kjc.cms.utils.Utils

class CartFragment(private var sharedPreferences: SharedPreferences, private var editor: Editor) : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartItemList: ArrayList<CartComponent>
    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cartRecycler.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter()
        binding.cartRecycler.adapter = cartAdapter
        cartItemList = arrayListOf()
        fetchCartItems()
        binding.continueBooking.setOnClickListener{
            val intent = Intent(context, SecondaryActivity::class.java)
            intent.putExtra("fragName", "Confirm Booking")
            startActivity(intent)
        }
    }

    private fun fetchCartItems() {
        val cartItems = sharedPreferences.getStringSet("items", null)
        val arrayItem = arrayListOf<CartComponent>()
        cartItems?.forEach { item->
            arrayItem.add(Gson().fromJson(item, CartComponent::class.java))
        }
        cartAdapter.saveData(arrayItem)
    }
}