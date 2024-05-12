package com.kjc.cms.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kjc.cms.databinding.ActivityMainBinding
import com.kjc.cms.ui.fragments.once.LoginFragment
import com.kjc.cms.ui.fragments.once.SplashScreenFragment
import com.kjc.cms.utils.Utils.Companion.fragMan

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //opens splashscreen
        fragMan(supportFragmentManager, SplashScreenFragment(), menuId = 1)
        // after 1.5 secs delay
        Thread.sleep(1500)
        //if user is already logged in then opens the home page
        if (GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, ContainerActivity::class.java))
            finish()
        } else {
            // else opens login page
            fragMan(supportFragmentManager, LoginFragment(), menuId = 1)
        }
    }
}