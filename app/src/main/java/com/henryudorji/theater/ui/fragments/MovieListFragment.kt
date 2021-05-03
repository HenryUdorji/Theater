package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.FragmentHomeBinding
import com.henryudorji.theater.databinding.FragmentMovieDetailBinding
import com.henryudorji.theater.databinding.FragmentMovieListBinding

//
// Created by hash on 5/2/2021.
//
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    private lateinit var binding: FragmentMovieListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieListBinding.bind(view)
    }
}