package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.ViewpagerAdapter
import com.henryudorji.theater.databinding.FragmentHomeBinding

//
// Created by hash on 5/2/2021.
//
class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewpagerAdapter: ViewpagerAdapter
    private lateinit var fragments: MutableList<Fragment>
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initViews()
    }

    private fun initViews() {
        fragments = mutableListOf(
            MoviesFragment(),
            TvSeriesFragment(),
            TrendingFragment()
        )
        viewpagerAdapter = ViewpagerAdapter(this, fragments)
        binding.viewpager.adapter = viewpagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewpager) {tab, position ->
            when(position) {
                0 -> tab.text = "MOVIES"
                1 -> tab.text = "TV SERIES"
                2 -> tab.text = "TRENDING"
            }
            binding.viewpager.currentItem = tab.position
        }.attach()
    }

}