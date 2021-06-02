package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.MovieListRecyclerAdapter
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.databinding.FragmentMovieListBinding
import com.henryudorji.theater.ui.main.MainActivity
import com.henryudorji.theater.utils.ConnectionManager
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.LATEST
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.QUERY_PAGE_SIZE
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.TRENDING
import com.henryudorji.theater.utils.Constants.UPCOMING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

//
// Created by hash on 5/2/2021.
//
class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    private val TAG = "MovieListFragment"
    private lateinit var binding: FragmentMovieListBinding
    private lateinit var movieListAdapter: MovieListRecyclerAdapter
    private lateinit var movieCategory: String
    private var fragID: Int = 1
    private lateinit var movieRepository: MovieRepository
    private val args: MovieListFragmentArgs by navArgs()
    private var moviePage = 1
    private var dataResponse: MovieResponse? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieListBinding.bind(view)

        movieCategory = args.moviecategory
        fragID = args.fragid
        movieRepository = (activity as MainActivity).movieRepository

        initViews()
        getMoviesData()
    }

    private fun getMoviesData() = CoroutineScope(Dispatchers.Main).launch{
        withContext(Dispatchers.Main) {
            showShimmerPlaceHolder()
        }

        try {
            if (ConnectionManager.hasInternetConnection(requireContext())) {
                val moviesData = when (movieCategory) {
                    POPULAR -> if (fragID == MOVIE) movieRepository.getPopularMovies(moviePage) else movieRepository.getPopularTvSeries(moviePage)
                    TOP_RATED -> if (fragID == MOVIE) movieRepository.getTopRatedMovies(moviePage) else movieRepository.getTopRatedMovies(moviePage)
                    TRENDING -> if (fragID == MOVIE) movieRepository.getTrendingMovies(moviePage) else movieRepository.getTrendingTvSeries(moviePage)
                    UPCOMING -> movieRepository.getUpcomingMovies(moviePage)
                    LATEST -> movieRepository.getOnTheAir(moviePage)
                    else -> movieRepository.getPopularMovies(moviePage)
                }

                if (moviesData.isSuccessful) {
                    moviesData.body()?.let { movieResponse ->
                        moviePage++
                        if (dataResponse == null) {
                            dataResponse = movieResponse
                        }else {
                            val oldArticles = dataResponse?.movies
                            val newArticles = movieResponse.movies
                            oldArticles?.addAll(newArticles)
                        }
                        movieListAdapter.differ.submitList(dataResponse?.movies ?: movieResponse.movies)
                    }
                    withContext(Dispatchers.Main) {
                        hideShimmerPlaceHolder()
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(moviesData.message())
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
                        Log.e(TAG, "getMoviesData: ${t.message}", )
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        showNoNetworkSnackBar(getString(R.string.conversion_error_msg))
                        Log.e(TAG, "getMoviesData: ${t.message}", )
                    }
                }
            }
        }
    }

    private fun showShimmerPlaceHolder() {
        binding.movieListRv.visibility = View.GONE
        binding.shimmerLayout.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun hideShimmerPlaceHolder() {
        binding.movieListRv.visibility = View.VISIBLE
        binding.shimmerLayout.apply {
            visibility = View.GONE
            stopShimmer()
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

    private fun initViews() {
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.toolbarTitle.text = getTitle()

        movieListAdapter = MovieListRecyclerAdapter()

        binding.movieListRv.apply {
            adapter = movieListAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addOnScrollListener(this@MovieListFragment.scrollListener)
        }

        movieListAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, movieID)
                putInt(FRAG_ID, fragID)
            }
            findNavController().navigate(R.id.action_movieListFragment_to_movieDetailFragment, bundle)
        }

    }

    private fun getTitle(): String {
        return when(movieCategory) {
            POPULAR -> if (fragID == MOVIE) "Popular Movies" else "Popular TvSeries"
            TOP_RATED -> if (fragID == MOVIE) "TopRated Movies" else "TopRated TvSeries"
            TRENDING -> if (fragID == MOVIE) "Trending Movies" else "Trending TvSeries"
            UPCOMING -> "Upcoming Movies"
            LATEST -> "Latest TvSeries"
            else -> ""
        }
    }

    // Paginating the recyclerView
    var isScrolling = false
    var isLoading = false
    var isLastPage = false

    private val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.movieListRv.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotAtLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                getMoviesData()
                isScrolling = false
            }
        }
    }
}