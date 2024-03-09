package com.kjc.cms.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.kjc.cms.R
import com.kjc.cms.databinding.ActivityMainBinding
import com.kjc.cms.ui.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fragMan(LoginFragment())

//        val fragmentManager = supportFragmentManager

//        fragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView, LoginFragment())
//            .commit()
    }

    private fun fragMan(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}