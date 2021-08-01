package com.henryudorji.theater.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.MovieRecyclerAdapter
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.databinding.FragmentHomeDetailBinding
import com.henryudorji.theater.ui.main.MainActivity
import com.henryudorji.theater.ui.main.MoviesViewModel
import com.henryudorji.theater.utils.ConnectionManager
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.UPCOMING
import com.henryudorji.theater.utils.resource.Resource
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
    private val viewModel: MoviesViewModel by activityViewModels()
    private lateinit var movieRepository: MovieRepository
    private lateinit var popularAdapter: MovieRecyclerAdapter
    private lateinit var upcomingAdapter: MovieRecyclerAdapter
    private lateinit var topRatedAdapter: MovieRecyclerAdapter

    private var moviePage = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeDetailBinding.bind(view)

        movieRepository = (activity as MainActivity).movieRepository

        initViews()
        viewModel.getMovies()
        //getMoviesData()
        observeMovieData()
    }

    private fun observeMovieData() {
        showShimmerPlaceHolder()
        viewModel.nowPlayingOrAiringTodayLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        setupViewFlipper(it.movies)
                    }
                }
                is Resource.Error -> {
                    state.message?.let { showNoNetworkSnackBar(it) }
                }
                else -> Unit
            }
        }

        viewModel.upcomingOrOnTheAirLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        upcomingAdapter.differ.submitList(it.movies.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    state.message?.let { showNoNetworkSnackBar(it) }
                }
                else -> Unit
            }
        }

        viewModel.topRatedLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        topRatedAdapter.differ.submitList(it.movies.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    state.message?.let { showNoNetworkSnackBar(it) }
                }
                else -> Unit
            }
        }

        viewModel.popularLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    hideShimmerPlaceHolder()
                    state.data?.let {
                        popularAdapter.differ.submitList(it.movies.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    state.message?.let { showNoNetworkSnackBar(it) }
                }
                is Resource.Loading -> {
                    showShimmerPlaceHolder()
                }
            }
        }
    }


    /*private fun getMoviesData() = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) {
            showShimmerPlaceHolder()
        }

        try {
            if (ConnectionManager.hasInternetConnection(requireContext())) {
                val popularMoviesData = movieRepository.getPopularMovies(moviePage)
                val upcomingMoviesData = movieRepository.getUpcomingMovies(moviePage)
                val topRatedMoviesData = movieRepository.getTopRatedMovies(moviePage)
                val nowPlayingMoviesData = movieRepository.getNowPlayingMovies(moviePage)

                if (popularMoviesData.isSuccessful && upcomingMoviesData.isSuccessful && topRatedMoviesData.isSuccessful) {
                    popularMoviesData.body()?.let { movieResponse ->
                        popularAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    upcomingMoviesData.body()?.let { movieResponse ->
                        upcomingAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    topRatedMoviesData.body()?.let { movieResponse ->
                        topRatedAdapter.differ.submitList(movieResponse.movies.shuffled().subList(0, 10))
                    }
                    nowPlayingMoviesData.body()?.let { movieResponse ->
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
    }*/

    private fun setupViewFlipper(movies: MutableList<Movie>) {
        Picasso.get().load(BASE_URL_IMAGE + movies[0].posterPath).into(binding.firstImage)
        Picasso.get().load(BASE_URL_IMAGE + movies[1].posterPath).into(binding.secondImage)
        Picasso.get().load(BASE_URL_IMAGE + movies[2].posterPath).into(binding.thirdImage)
        binding.swipeViewFlipper.startFlipping()

        binding.firstImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[0].id)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
        binding.secondImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[1].id)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
        binding.thirdImage.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movies[2].id)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
    }

    private fun showNoNetworkSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry)) {
                    if (ConnectionManager.hasInternetConnection(requireContext())) {
                        viewModel.getMovies()
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
            upcomingMoviesShimmer.stopShimmer()
            topRatedMoviesShimmer.stopShimmer()
            trendingMoviesShimmer.stopShimmer()
            swipeShimmer.stopShimmer()
        }
    }

    private fun showShimmerPlaceHolder() {
        binding.apply {
            normalLayout.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            popularShimmerFrame.startShimmer()
            upcomingMoviesShimmer.startShimmer()
            topRatedMoviesShimmer.startShimmer()
            trendingMoviesShimmer.startShimmer()
            swipeShimmer.startShimmer()
        }
    }


    private fun initViews() {
        popularAdapter = MovieRecyclerAdapter()
        upcomingAdapter = MovieRecyclerAdapter()
        topRatedAdapter = MovieRecyclerAdapter()

        binding.popularRv.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        popularAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.upcomingRv.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        upcomingAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.topRatedRv.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        topRatedAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.showAllPopularText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, POPULAR)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllUpcomingMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, UPCOMING)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }

        binding.showAllTopRatedMoviesText.setOnClickListener {
            val bundle = Bundle().apply {
                putString(MOVIE_CATEGORY, TOP_RATED)
                putInt(FRAG_ID, MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
        }
    }

}