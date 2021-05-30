package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.MovieRecyclerAdapter
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.databinding.FragmentHomeDetailBinding
import com.henryudorji.theater.ui.main.MainActivity
import com.henryudorji.theater.utils.ConnectionManager
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.UPCOMING
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

//
// Created by hash on 5/2/2021.
//
class HomeMoviesFragment: Fragment(R.layout.fragment_home_detail) {
    private val TAG = "HomeMoviesFragment"
    private lateinit var binding: FragmentHomeDetailBinding
    lateinit var movieRepository: MovieRepository
    private lateinit var popularAdapter: MovieRecyclerAdapter
    private lateinit var upcomingAdapter: MovieRecyclerAdapter
    private lateinit var topRatedAdapter: MovieRecyclerAdapter

    private var moviePage = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeDetailBinding.bind(view)

        movieRepository = (activity as MainActivity).movieRepository

        initViews()
        getMoviesData()
    }


    private fun getMoviesData() = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) {
            showShimmerPlaceHolder()
        }

        try {
            if (ConnectionManager.hasInternetConnection(requireContext())) {
                val popularMoviesData = movieRepository.getPopularMovies(moviePage)
                val upcomingMoviesData = movieRepository.getUpcomingMovies(moviePage)
                val topRatedMoviesData = movieRepository.getTopRatedMovies(moviePage)

                if (popularMoviesData.isSuccessful && upcomingMoviesData.isSuccessful && topRatedMoviesData.isSuccessful) {
                    popularMoviesData.body()?.let { movieResponse ->
                        popularAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    upcomingMoviesData.body()?.let { movieResponse ->
                        upcomingAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    topRatedMoviesData.body()?.let { movieResponse ->
                        topRatedAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                        //@todo load viewflipper data from a different endpoint
                        withContext(Dispatchers.Main) {
                            setupViewFlipper(movieResponse.movies)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        hideShimmerPlaceHolder()
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(popularMoviesData.message())
                    }
                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(getString(R.string.connect_unavailable_msg))
                }
            }
        }catch (t: Throwable) {
            when(t) {
                is IOException -> {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(getString(R.string.network_fail_msg))
                        Log.e(TAG, "getMoviesData: ${t.message}")
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(getString(R.string.conversion_error_msg))
                        Log.e(TAG, "getMoviesData: ${t.message}")
                    }
                }
            }
        }
    }

    private fun setupViewFlipper(movies: MutableList<Movie>) {
        Picasso.get().load(BASE_URL_IMAGE + movies[0].posterPath).into(binding.firstImage)
        Picasso.get().load(BASE_URL_IMAGE + movies[1].posterPath).into(binding.secondImage)
        Picasso.get().load(BASE_URL_IMAGE + movies[2].posterPath).into(binding.thirdImage)
        binding.swipeViewFlipper.startFlipping()

        //@todo ClickListener on the Image
    }

    private fun showNoNetworkSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry)) {
                    if (ConnectionManager.hasInternetConnection(requireContext())) {
                        getMoviesData()
                    }else {
                        showNoNetworkSnackBar(message)
                    }
                }.show()

    }

    private fun hideShimmerPlaceHolder() {
        binding.apply {
            normalLayout.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
            popularShimmerFrame.stopShimmer()
            newMoviesShimmer.stopShimmer()
            topRatedMoviesShimmer.stopShimmer()
            swipeShimmer.stopShimmer()
        }
    }

    private fun showShimmerPlaceHolder() {
        binding.apply {
            normalLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            popularShimmerFrame.startShimmer()
            newMoviesShimmer.startShimmer()
            topRatedMoviesShimmer.startShimmer()
            swipeShimmer.startShimmer()
        }
    }


    private fun initViews() {
        popularAdapter = MovieRecyclerAdapter()
        upcomingAdapter = MovieRecyclerAdapter()
        topRatedAdapter = MovieRecyclerAdapter()

        binding.popularRecyclerView.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }

        binding.newMoviesRecyclerView.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }

        binding.topRatedMoviesRecyclerView.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }

        binding.showAllPopularText.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(MOVIE_CATEGORY, POPULAR)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllUpcomingMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(MOVIE_CATEGORY, UPCOMING)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllTopRatedMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(MOVIE_CATEGORY, TOP_RATED)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }
    }

}