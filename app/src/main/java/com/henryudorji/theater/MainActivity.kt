package com.henryudorji.theater

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.henryudorji.theater.adapters.ViewpagerAdapter
import com.henryudorji.theater.databinding.ActivityMainBinding
import com.henryudorji.theater.ui.fragments.HomeFragment
import com.henryudorji.theater.ui.fragments.MoviesFragment
import com.henryudorji.theater.ui.fragments.TrendingFragment
import com.henryudorji.theater.ui.fragments.TvSeriesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Theater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}