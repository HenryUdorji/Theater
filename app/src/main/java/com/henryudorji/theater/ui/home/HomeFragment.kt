package com.henryudorji.theater.ui.home

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initViews()
    }

    private fun initViews() {
        fragments = mutableListOf(
            HomeMoviesFragment(),
            HomeTvSeriesFragment(),
            HomeSearchFragment()
        )
        viewpagerAdapter = ViewpagerAdapter(this, fragments)
        binding.viewpager.apply {
            adapter = viewpagerAdapter
            isUserInputEnabled = false
        }

        TabLayoutMediator(binding.tabs, binding.viewpager) {tab, position ->
            when(position) {
                0 -> tab.text = "MOVIES"
                1 -> tab.text = "TV SERIES"
                2 -> tab.text = "SEARCH"
            }
            binding.viewpager.currentItem = tab.position
        }.attach()
    }

}