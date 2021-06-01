package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.FragmentHomeBinding
import com.henryudorji.theater.databinding.FragmentMovieDetailBinding

//
// Created by hash on 5/2/2021.
//
class MovieDetailFragment: Fragment(R.layout.fragment_movie_detail) {
    private lateinit var binding: FragmentMovieDetailBinding
    private val args: MovieDetailFragmentArgs by navArgs()
    private var movieID: Int = 0
    private var fragID: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailBinding.bind(view)

        movieID = args.movieid
    }
}