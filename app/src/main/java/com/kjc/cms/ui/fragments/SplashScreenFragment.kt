package com.kjc.cms.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kjc.cms.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private lateinit var binding : FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.statusBarColor.toColor(getColor(R.color.black))

        // need to work on the import
//        binding.kjcLogo.alpha = 0f
//        binding.kjcLogo.animate().setDuration(1500).alpha(1f).withEndAction {
////            var i = Intent(requireActivity(), ::class.java)
////            startActivity(i)
////            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
////            finish()
//        }
    }
}