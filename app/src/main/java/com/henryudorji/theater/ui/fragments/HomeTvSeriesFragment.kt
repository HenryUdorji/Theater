package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.henryudorji.theater.utils.Constants
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.LATEST
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.TRENDING
import com.henryudorji.theater.utils.Constants.TV_SERIES
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
class HomeTvSeriesFragment: Fragment(R.layout.fragment_home_detail) {
    private val TAG = "HomeTvSeriesFragment"
    private lateinit var binding: FragmentHomeDetailBinding
    private lateinit var movieRepository: MovieRepository
    private lateinit var popularAdapter: MovieRecyclerAdapter
    private lateinit var latestAdapter: MovieRecyclerAdapter
    private lateinit var topRatedAdapter: MovieRecyclerAdapter
    private lateinit var trendingAdapter: MovieRecyclerAdapter

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
                val popularTvSeriesData = movieRepository.getPopularTvSeries(moviePage)
                val latestTvSeriesData = movieRepository.getLatestTvSeries(moviePage)
                val topRatedTvSeriesData = movieRepository.getTopRatedTvSeries(moviePage)
                val trendingTvSeriesData = movieRepository.getTrendingTvSeries(moviePage)
                val airingTodayTvSeriesData = movieRepository.getAiringTodayTvSeries(moviePage)

                if (popularTvSeriesData.isSuccessful && latestTvSeriesData.isSuccessful
                        && topRatedTvSeriesData.isSuccessful && trendingTvSeriesData.isSuccessful
                        && airingTodayTvSeriesData.isSuccessful) {
                    popularTvSeriesData.body()?.let { movieResponse ->
                        popularAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    latestTvSeriesData.body()?.let { movieResponse ->
                        latestAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    topRatedTvSeriesData.body()?.let { movieResponse ->
                        topRatedAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    trendingTvSeriesData.body()?.let { movieResponse ->
                        trendingAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    airingTodayTvSeriesData.body()?.let { movieResponse ->
                        withContext(Dispatchers.Main) {
                            setupViewFlipper(movieResponse.movies)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        hideShimmerPlaceHolder()
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(popularTvSeriesData.message())
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
                        Log.e(TAG, "getTvSeriesData: ${t.message}")
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(getString(R.string.conversion_error_msg))
                        Log.e(TAG, "getTvSeriesData: ${t.message}")
                    }
                }
            }
        }
    }

    private fun setupViewFlipper(movies: MutableList<Movie>) {
        Picasso.get().load(Constants.BASE_URL_IMAGE + movies[0].posterPath).into(binding.firstImage)
        Picasso.get().load(Constants.BASE_URL_IMAGE + movies[1].posterPath).into(binding.secondImage)
        Picasso.get().load(Constants.BASE_URL_IMAGE + movies[2].posterPath).into(binding.thirdImage)
        binding.swipeViewFlipper.startFlipping()

        binding.firstImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[0].id)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
        binding.secondImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[1].id)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
        binding.thirdImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[2].id)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
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
        latestAdapter = MovieRecyclerAdapter()
        topRatedAdapter = MovieRecyclerAdapter()
        trendingAdapter = MovieRecyclerAdapter()

        binding.popularRecyclerView.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        popularAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.latestRecyclerView.apply {
            adapter = latestAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        latestAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.topRatedMoviesRecyclerView.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        topRatedAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.trendingMoviesRecyclerView.apply {
            adapter = trendingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        trendingAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }


        binding.showAllPopularText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, POPULAR)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        //Latest is the same as Upcoming
        binding.showAllUpcomingMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, LATEST)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllTopRatedMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, TOP_RATED)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllTrendingMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, TRENDING)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }
    }
}