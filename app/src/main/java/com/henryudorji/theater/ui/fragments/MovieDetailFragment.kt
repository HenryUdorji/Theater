package com.henryudorji.theater.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.CastRecyclerAdapter
import com.henryudorji.theater.adapters.GenreRecyclerAdapter
import com.henryudorji.theater.adapters.MovieRecyclerAdapter
import com.henryudorji.theater.adapters.ReviewRecyclerAdapter
import com.henryudorji.theater.data.model.detail.Genre
import com.henryudorji.theater.data.model.detail.MovieDetailResponse
import com.henryudorji.theater.data.model.detail.tvseries.TvSeriesDetailResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.databinding.FragmentMovieDetailBinding
import com.henryudorji.theater.ui.main.MainActivity
import com.henryudorji.theater.utils.ConnectionManager
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.MOVIE
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


//
// Created by hash on 5/2/2021.
//
class MovieDetailFragment: Fragment(R.layout.fragment_movie_detail) {
    private val TAG = "MovieDetailFragment"
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var movieRepository: MovieRepository
    private lateinit var genreRecyclerAdapter: GenreRecyclerAdapter
    private lateinit var castRecyclerAdapter: CastRecyclerAdapter
    private lateinit var recommendationRecyclerAdapter: MovieRecyclerAdapter
    private lateinit var reviewRecyclerAdapter: ReviewRecyclerAdapter
    private val args: MovieDetailFragmentArgs by navArgs()
    private var movieID: Int = 0
    private var fragID: Int = 1
    private var page = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieDetailBinding.bind(view)

        movieID = args.movieid
        fragID = args.fragid
        movieRepository = (activity as MainActivity).movieRepository

        initViews()
        getDetails()
    }

    private fun getDetails() = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) {
            showShimmerPlaceHolder()
        }

        try {
            if (ConnectionManager.hasInternetConnection(requireContext())) {
                //Check if the user is coming from Movies Frag or TvSeries Frag
                if (fragID == MOVIE) {
                    val movieResponse = movieRepository.getMovieDetails(movieID)
                    if (movieResponse.isSuccessful) {
                        movieResponse.body()?.let { data ->
                            if (data.video) {
                                getVideo()
                            }else {
                                withContext(Dispatchers.Main) {
                                    binding.watch.visibility = View.GONE
                                    binding.roundRectView.visibility = View.GONE
                                }
                            }
                            withContext(Dispatchers.Main) {
                                showInitialDetail(data, null)
                            }
                            getCast()
                            getRecommendation()
                            getReview()
                        }

                        withContext(Dispatchers.Main) {
                            hideShimmerPlaceHolder()
                        }
                    }else {
                        withContext(Dispatchers.Main) {
                            showNoNetworkSnackBar(movieResponse.message())
                        }
                    }
                } else {
                    val tvSeriesResponse = movieRepository.getTvSeriesDetails(movieID)
                    if (tvSeriesResponse.isSuccessful) {
                        tvSeriesResponse.body()?.let { data ->
                            withContext(Dispatchers.Main) {
                                showInitialDetail(null, data)
                            }
                            getCast()
                            getRecommendation()
                            getReview()
                        }

                        withContext(Dispatchers.Main) {
                            hideShimmerPlaceHolder()
                        }
                    }else {
                        withContext(Dispatchers.Main) {
                            showNoNetworkSnackBar(tvSeriesResponse.message())
                        }
                    }

                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(getString(R.string.connect_unavailable_msg))
                }
            }
        }catch (t: Throwable) {
            when (t) {
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

    private fun showInitialDetail(movieData: MovieDetailResponse?, tvData: TvSeriesDetailResponse?) {
        val genreList: List<Genre>
        if (movieData == null) {
            Picasso.get().load(BASE_URL_IMAGE + tvData?.posterPath).into(binding.barImage)
            binding.movieTitle.text = tvData?.name
            binding.ratingText.text = tvData?.voteAverage.toString()
            binding.runtimeText.text = "${tvData?.episodeRunTime.toString()} minutes"
            genreList = tvData?.genres!!
            binding.movieOverviewText.text = tvData?.overview
        }else {
            Picasso.get().load(BASE_URL_IMAGE + movieData.posterPath).into(binding.barImage)
            binding.movieTitle.text = movieData.title
            binding.ratingText.text = movieData.voteAverage.toString()
            binding.runtimeText.text = "${movieData.runtime} minutes"
            genreList = movieData.genres
            binding.movieOverviewText.text = movieData.overview
        }
        genreRecyclerAdapter.differ.submitList(genreList)
    }

    private fun getReview() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val review = if (fragID == MOVIE) {
                movieRepository.getMovieReview(page, movieID)
            }else {
                movieRepository.getTvSeriesReview(page, movieID)
            }

            if (review.isSuccessful) {
                review.body()?.let { data ->
                    reviewRecyclerAdapter.differ.submitList(data.reviews.subList(0, 10))
                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(review.message())
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

    private fun getRecommendation() = CoroutineScope(Dispatchers.IO).launch{
        try {
            val recommendation = if (fragID == MOVIE) {
                movieRepository.getMovieRecommendation(movieID)
            }else {
                movieRepository.getTvSeriesRecommendation(movieID)
            }

            if (recommendation.isSuccessful) {
                recommendation.body()?.let { data ->
                    recommendationRecyclerAdapter.differ.submitList(data.movies)
                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(recommendation.message())
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

    private fun getCast() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val cast = if (fragID == MOVIE) {
                movieRepository.getMovieCast(page, movieID)
            }else {
                movieRepository.getTvSeriesCast(page, movieID)
            }

            if (cast.isSuccessful) {
                cast.body()?.let { data ->
                    castRecyclerAdapter.differ.submitList(data.cast.subList(0, 10))
                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(cast.message())
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

    private fun getVideo() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val video = if (fragID == MOVIE) {
                movieRepository.getMovieVideo(movieID)
            }else {
                movieRepository.getTvSeriesVideo(movieID)
            }

            if (video.isSuccessful) {
                video.body()?.let { data ->
                    //Play video of movie or series
                    withContext(Dispatchers.Main) {
                        lifecycle.addObserver(binding.youtubePlayerView)

                        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                val videoId = data.videos[0].key
                                youTubePlayer.loadVideo(videoId, 0f)
                            }
                        })

                    }
                }
            }else {
                withContext(Dispatchers.Main) {
                    showNoNetworkSnackBar(video.message())
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

    private fun showShimmerPlaceHolder() {
        binding.normalLayout.visibility = View.GONE
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.detailShimmer.startShimmer()
    }

    private fun hideShimmerPlaceHolder() {
        binding.normalLayout.visibility = View.VISIBLE
        binding.shimmerLayout.visibility = View.GONE
        binding.detailShimmer.stopShimmer()
    }

    private fun showNoNetworkSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry)) {
                    if (ConnectionManager.hasInternetConnection(requireContext())) {
                        getDetails()
                    }else {
                        showNoNetworkSnackBar(message)
                    }
                }.show()

    }

    private fun initViews() {
        if (fragID == MOVIE) {
            binding.seasonText.visibility = View.GONE
            binding.episodeText.visibility = View.GONE
            binding.budgetText.visibility = View.VISIBLE
            binding.revenueText.visibility = View.VISIBLE
        }else {
            binding.budgetText.visibility = View.GONE
            binding.revenueText.visibility = View.GONE
            binding.seasonText.visibility = View.VISIBLE
            binding.episodeText.visibility = View.VISIBLE
        }

        genreRecyclerAdapter = GenreRecyclerAdapter()
        recommendationRecyclerAdapter = MovieRecyclerAdapter()
        reviewRecyclerAdapter = ReviewRecyclerAdapter()
        castRecyclerAdapter = CastRecyclerAdapter()

        binding.genreRv.apply {
            adapter = genreRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recommendedRv.apply {
            adapter = recommendationRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.reviewRv.apply {
            adapter = reviewRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.castRv.apply {
            adapter = castRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.showAllCastText.setOnClickListener {
            //@todo cast
        }

        binding.showAllReviewsText.setOnClickListener {
            //@todo review
        }
    }
}