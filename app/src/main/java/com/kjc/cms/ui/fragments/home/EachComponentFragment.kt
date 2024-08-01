package com.kjc.cms.ui.fragments.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentEachComponentBinding
import com.kjc.cms.model.CartComponent
import com.kjc.cms.model.Component
import com.kjc.cms.utils.Utils

class EachComponentFragment(private var data: Component, private var cartItems: MutableSet<String>? = mutableSetOf(), private var editor: Editor) : Fragment() {

    private lateinit var binding: FragmentEachComponentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEachComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MutatingSharedPrefs")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameValue.text = data.Name
        binding.quantityValue.text = data.AvailableQuantity
        binding.modelValue.text = data.Model
        binding.priceValue.text = data.Price
        Glide.with(this).load(data.Image).into(binding.componentImage)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding.addToCart.setOnClickListener{
            openDialogBox()
        }
    }

    private fun openDialogBox() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.select_quantity)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val decrement: ImageView = dialog.findViewById(R.id.decrement)
        val cartQuantity: TextView = dialog.findViewById(R.id.cart_item_quantity)
        val increment: ImageView = dialog.findViewById(R.id.increment)
        val addBtn: TextView = dialog.findViewById(R.id.add_item_to_cart)
        val cancelBtn: TextView = dialog.findViewById(R.id.cancel_cart)
        var selectedQuantity = 1

        decrement.setOnClickListener{
            if (selectedQuantity==data.AvailableQuantity.toInt()){
                increment.visibility = View.VISIBLE
            }
            if (selectedQuantity>1){
                cartQuantity.text = (--selectedQuantity).toString()
                Log.d("tag", selectedQuantity.toString())
            }
            if (selectedQuantity==1){
                decrement.visibility = View.INVISIBLE
            }
        }

        increment.setOnClickListener{
            if (selectedQuantity==1){
                decrement.visibility = View.VISIBLE
            }
            if (selectedQuantity<data.AvailableQuantity.toInt()){
                cartQuantity.text = (++selectedQuantity).toString()
                Log.d("tag", selectedQuantity.toString())
            }
            if (selectedQuantity==data.AvailableQuantity.toInt()){
                increment.visibility = View.INVISIBLE
            }
        }

        addBtn.setOnClickListener{
            handleAdd(data, selectedQuantity)
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun handleAdd(data: Component, selectedQuantity:Int) {
        val item = CartComponent(data.Name, data.Id, selectedQuantity, data.Model.toString(), data.Image)
        val gson = Gson()
        cartItems?.add(gson.toJson(item))
        editor.putStringSet("items", cartItems)
        editor.apply()
    }
}