package com.kjc.cms.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kjc.cms.model.Component
import com.google.gson.Gson
import com.kjc.cms.databinding.ActivitySecondaryBinding
import com.kjc.cms.ui.fragments.home.ConfirmBookingFragment
import com.kjc.cms.ui.fragments.home.EachComponentFragment
import com.kjc.cms.utils.Utils

class SecondaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("Cart", MODE_PRIVATE)
        val editor = sp.edit()
        val cartItems = sp.getStringSet("items", mutableSetOf())

        val data = intent.getStringExtra("Data")
        val frag = intent.getStringExtra("fragName")
        val comp = Gson().fromJson(data, Component::class.java)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (frag == "EachComponent") {
            Utils.fragMan(supportFragmentManager, EachComponentFragment(comp, cartItems, editor))
        } else {
            Utils.fragMan(supportFragmentManager, ConfirmBookingFragment(cartItems, editor, sp))
        }
    }
}