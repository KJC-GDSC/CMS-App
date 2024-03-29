package com.kjc.cms.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kjc.cms.R
import com.kjc.cms.databinding.FragmentAboutBinding
import com.kjc.cms.databinding.FragmentConfirmBookingBinding

class ConfirmBookingFragment : Fragment() {


    private lateinit var binding: FragmentConfirmBookingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}