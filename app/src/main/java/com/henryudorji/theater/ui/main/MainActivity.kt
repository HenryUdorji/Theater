package com.henryudorji.theater.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Theater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}