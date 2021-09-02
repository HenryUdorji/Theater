package com.henryudorji.theater.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Theater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}