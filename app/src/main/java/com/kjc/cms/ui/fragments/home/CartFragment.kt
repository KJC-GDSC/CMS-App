package com.kjc.cms.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjc.cms.R
import com.kjc.cms.adapter.CartAdapter
import com.kjc.cms.databinding.FragmentAboutBinding
import com.kjc.cms.databinding.FragmentCartBinding
import com.kjc.cms.model.CartComponent

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var localStorage: String //TODO( connect to local storage to fetch values )
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
    }

    private fun fetchCartItems() {
        TODO("Not yet implemented")
    }
}