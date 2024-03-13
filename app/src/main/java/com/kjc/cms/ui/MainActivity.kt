package com.kjc.cms.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kjc.cms.R
import com.kjc.cms.databinding.ActivityMainBinding
import com.kjc.cms.ui.fragments.once.LoginFragment
import com.kjc.cms.ui.fragments.once.SplashScreenFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //opens splashscreen
        fragMan(SplashScreenFragment())
        // after 1.5 secs delay
        Thread.sleep(1500)
        //if user is already logged in then opens the home page
        if (GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // else opens login page
            fragMan(LoginFragment())
        }
    }

    private fun fragMan(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}