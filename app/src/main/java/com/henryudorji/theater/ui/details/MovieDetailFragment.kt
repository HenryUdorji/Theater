package com.henryudorji.theater.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.ui.details.adapters.CastRvAdapter
import com.henryudorji.theater.ui.details.adapters.GenreRvAdapter
import com.henryudorji.theater.ui.home.MovieRvAdapter
import com.henryudorji.theater.ui.details.adapters.ReviewRvAdapter
import com.henryudorji.theater.data.model.detail.Genre
import com.henryudorji.theater.data.model.detail.MovieDetailResponse
import com.henryudorji.theater.data.model.detail.TvSeriesDetailResponse
import com.henryudorji.theater.databinding.FragmentMovieDetailBinding
import com.henryudorji.theater.ui.base.BaseFragment
import com.henryudorji.theater.ui.details.adapters.TrailerVideoRvAdapter
import com.henryudorji.theater.ui.home.tvseries.TvSeriesRvAdapter
import com.henryudorji.theater.utils.*
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.SPACE
import com.henryudorji.theater.utils.Constants.TV_SERIES
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailFragment: BaseFragment<FragmentMovieDetailBinding, DetailsViewModel>() {
    private val TAG = "MovieDetailFragment"
    private lateinit var genreRvAdapter: GenreRvAdapter
    private lateinit var castRvAdapter: CastRvAdapter
    private lateinit var movieRecommendationRvAdapter: MovieRvAdapter
    private lateinit var tvSeriesRecommendationRvAdapter: TvSeriesRvAdapter
    private lateinit var reviewRvAdapter: ReviewRvAdapter
    private lateinit var trailerVideoRvAdapter: TrailerVideoRvAdapter
    private val args: MovieDetailFragmentArgs by navArgs()
    private var movieID: Int = 0
    private var fragID: Int = 1

    override val viewModel: DetailsViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMovieDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieID = args.movieid
        fragID = args.fragid

        if (fragID == MOVIE) {
            viewModel.getMovieDetail(movieID)
        } else {
            viewModel.getTvSeriesDetail(tvSeriesID = movieID)
        }

        initViews()
        observeData()
    }

    private fun observeData() {
        observeInitialDetails()
        observeTrailerVideo()
        observeReview()
        observeCast()
        observeRecommendation()
    }

    private fun observeRecommendation() = with(binding) {
        when(fragID) {
            MOVIE -> {
                viewModel.moviesRecommendationLiveData.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is Resource.Success -> {
                            val recommendation = state.data?.movies
                            if (recommendation?.isNotEmpty() == true) {
                                if (recommendation.size > 10) {
                                    movieRecommendationRvAdapter.differ
                                        .submitList(state.data?.movies?.subList(0, 10))
                                }else movieRecommendationRvAdapter.differ.submitList(recommendation)
                            }else {
                                recommendedRv.hide()
                                recommendedText.hide()
                            }
                        }
                        is Resource.Error -> {
                            state.message?.let {
                                Snackbar.make(root,
                                    it, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Loading -> Unit
                    }
                }
            }
            TV_SERIES -> {
                viewModel.tvSeriesRecommendationLiveData.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is Resource.Success -> {
                            val recommendation = state.data?.tvSeries
                            if (recommendation?.isNotEmpty() == true) {
                                if (recommendation.size > 10) {
                                    tvSeriesRecommendationRvAdapter.differ
                                        .submitList(state.data?.tvSeries?.subList(0, 10))
                                }else tvSeriesRecommendationRvAdapter.differ.submitList(recommendation)
                            }
                        }
                        is Resource.Error -> {
                            state.message?.let {
                                Snackbar.make(root,
                                    it, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        is Resource.Loading -> Unit
                    }
                }
            }
        }
    }

    private fun observeCast() = with(binding) {
        viewModel.castLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    val cast = state.data?.cast
                    if (cast?.isNotEmpty() == true) {
                        castRvAdapter.differ.submitList(cast)
                    }
                }
                is Resource.Error -> {
                    state.message?.let {
                        Snackbar.make(root,
                            it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun observeReview() = with(binding) {
        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    val reviews = state.data?.reviews
                    if (reviews?.isNotEmpty() == true) {
                        if (reviews.size > 10) {
                            reviewRvAdapter.differ.submitList(reviews.subList(0, 10))
                        }else reviewRvAdapter.differ.submitList(reviews)
                    }else reviewRvAdapter.differ.submitList(mutableListOf())

                    val reviewSize = AppUtils.coolNumberFormat(reviews?.size?.toLong()!!)
                    totalReviewText.text = "$reviewSize reviews"
                }
                is Resource.Error -> {
                    state.message?.let {
                        Snackbar.make(root,
                            it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun observeTrailerVideo() = with(binding) {
        viewModel.trailerLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    trailerVideoRvAdapter.differ.submitList(state.data?.videos)
                }
                is Resource.Error -> {
                    state.message?.let {
                        Snackbar.make(root,
                            it, Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun observeInitialDetails() = with(binding) {
        when(fragID) {
            MOVIE -> {
                viewModel.movieDetailLiveData.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is Resource.Success -> {
                            if (state.data?.video == false) {
                                videoRv.hide()
                                watch.hide()
                            }
                            normalLayout.show()
                            progressBar.hide()
                            showInitialDetail(state.data, null)
                        }
                        is Resource.Error -> {
                            state.message?.let {
                                Snackbar.make(root,
                                    it, Snackbar.LENGTH_SHORT).show()
                                progressBar.hide()
                            }
                        }
                        is Resource.Loading -> {
                            progressBar.show()
                            normalLayout.hide()
                        }
                    }
                }
            }
            TV_SERIES -> {
                viewModel.tvSeriesDetailLiveData.observe(viewLifecycleOwner) { state ->
                    when(state) {
                        is Resource.Success -> {
                            normalLayout.show()
                            progressBar.hide()
                            showInitialDetail(null, state.data)
                        }
                        is Resource.Error -> {
                            state.message?.let {
                                Snackbar.make(root,
                                    it, Snackbar.LENGTH_SHORT).show()
                                progressBar.hide()
                            }
                        }
                        is Resource.Loading -> {
                            progressBar.show()
                            normalLayout.hide()
                        }
                    }
                }
            }
        }
    }

    private fun showInitialDetail(movieData: MovieDetailResponse?, tvData: TvSeriesDetailResponse?) {
        var genreList: List<Genre> = mutableListOf()
        if (movieData == null) {
            Picasso.get().load(BASE_URL_IMAGE + tvData?.posterPath).into(binding.barImage)
            tvData?.let {
                binding.movieTitle.text = it.name
                binding.ratingText.text = it.voteAverage.toString()
                binding.runtimeText.text = "${it.episodeRunTime ?: "..."} minutes"
                genreList = it.genres
                binding.movieOverviewText.text = it.overview
                binding.budgetText.text = "Seasons: ${it.numberOfSeasons ?: "..."}"
                binding.revenueText.text = "Episodes: ${it.numberOfEpisodes ?: "..."}"
            }
        } else {
            Picasso.get().load(BASE_URL_IMAGE + movieData.posterPath).into(binding.barImage)
            binding.movieTitle.text = movieData.title
            binding.ratingText.text = movieData.voteAverage.toString()
            binding.runtimeText.text = "${movieData.runtime ?: "..."} minutes"
            genreList = movieData.genres
            binding.movieOverviewText.text = movieData.overview

            val budget = AppUtils.coolNumberFormat(movieData.budget?.toLong())
            val revenue = AppUtils.coolNumberFormat(movieData.revenue?.toLong())
            binding.budgetText.text = "Budget: $$budget"
            binding.revenueText.text = "Revenue: $$revenue"
        }
        genreRvAdapter.differ.submitList(genreList)
    }

    private fun initViews() = with(binding) {
        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if (fragID == MOVIE) movieRecommendationRvAdapter = MovieRvAdapter() else tvSeriesRecommendationRvAdapter = TvSeriesRvAdapter()

        genreRvAdapter = GenreRvAdapter()
        reviewRvAdapter = ReviewRvAdapter()
        castRvAdapter = CastRvAdapter()
        trailerVideoRvAdapter = TrailerVideoRvAdapter(lifecycle)

        genreRv.apply {
            adapter = genreRvAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        recommendedRv.apply {
            adapter = if (fragID == MOVIE) movieRecommendationRvAdapter else tvSeriesRecommendationRvAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        if (fragID == MOVIE) {
            movieRecommendationRvAdapter.setOnItemClickListener { movieID ->
                val bundle = Bundle().apply {
                    putInt(MOVIE_ID, movieID)
                    putInt(FRAG_ID, MOVIE)
                }
                findNavController().navigate(R.id.action_movieDetailFragment_self, bundle)
            }
        }else {
            tvSeriesRecommendationRvAdapter.setOnItemClickListener { tvSeriesID ->
                val bundle = Bundle().apply {
                    putInt(MOVIE_ID, tvSeriesID)
                    putInt(FRAG_ID, TV_SERIES)
                }
                findNavController().navigate(R.id.action_movieDetailFragment_self, bundle)
            }
        }

        reviewRv.apply {
            adapter = reviewRvAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(SpacesItemDecorator(SPACE))
        }

        castRv.apply {
            adapter = castRvAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        videoRv.apply {
            adapter = trailerVideoRvAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacesItemDecorator(SPACE))
        }

        showAllReviewsText.setOnClickListener {
            //@todo review
            Snackbar.make(binding.root, "Feature coming soon", Snackbar.LENGTH_SHORT).show()
        }
    }
}