package com.kjc.cms.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kjc.cms.R
import com.kjc.cms.databinding.ActivityComponentBinding
import com.kjc.cms.databinding.ActivityMainBinding

class ComponentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComponentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityComponentBinding.inflate(layoutInflater)
    setContentView(binding.root)
    }
}