package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.FragmentHomeBinding
import com.henryudorji.theater.databinding.FragmentHomeSearchBinding
import com.henryudorji.theater.databinding.FragmentMovieDetailBinding

//
// Created by hash on 5/2/2021.
//
class HomeSearchFragment: Fragment(R.layout.fragment_home_search) {
    private lateinit var binding: FragmentHomeSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeSearchBinding.bind(view)
    }
}