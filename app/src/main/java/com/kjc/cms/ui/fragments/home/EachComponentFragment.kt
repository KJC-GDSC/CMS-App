package com.kjc.cms.ui.fragments.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentEachComponentBinding
import com.kjc.cms.model.Component

class EachComponentFragment(private var data: Component) : Fragment() {

    private lateinit var binding: FragmentEachComponentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEachComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameValue.text = data.Name
        binding.quantityValue.text = data.AvailableQuantity
        binding.modelValue.text = data.Model
        binding.priceValue.text = data.Price
        binding.addToCart.setOnClickListener{
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.select_quantity)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val okBtn: TextView = dialog.findViewById(R.id.ok_btn_item_not_available)
            okBtn.setOnClickListener{
                dialog.dismiss()
            }
        }
    }
}