package com.henryudorji.theater.ui.home.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.adapters.MovieRecyclerAdapter
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.databinding.FragmentHomeDetailBinding
import com.henryudorji.theater.ui.base.BaseFragment
import com.henryudorji.theater.ui.home.HomeFragmentDirections
import com.henryudorji.theater.utils.*
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.UPCOMING
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class HomeMoviesFragment: BaseFragment<FragmentHomeDetailBinding, MoviesViewModel>() {
    private val TAG = "HomeMoviesFragment"
    override val viewModel: MoviesViewModel by activityViewModels()
    private lateinit var popularAdapter: MovieRecyclerAdapter
    private lateinit var upcomingAdapter: MovieRecyclerAdapter
    private lateinit var topRatedAdapter: MovieRecyclerAdapter

    private var moviePage = 1

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeDetailBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeMovieData()
        ///observeNetworkConnection()
    }

    private fun observeMovieData() {
        viewModel.nowPlayingLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> showShimmerPlaceHolder()
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    state.data?.let { movieResponse ->
                        setupViewFlipper(movieResponse.movies)
                    }
                }
            }
        }

        viewModel.upcomingMoviesLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        upcomingAdapter.differ.submitList(it.movies.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> showShimmerPlaceHolder()
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
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> showShimmerPlaceHolder()
            }
        }

        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    hideShimmerPlaceHolder()
                    state.data?.let {
                        popularAdapter.differ.submitList(it.movies.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showShimmerPlaceHolder()
                }
            }
        }
    }

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

    private fun hideShimmerPlaceHolder() = with(binding) {
        normalLayout.show()
        shimmerInclude.apply {
            shimmerLayout.hide()
            popularShimmerFrame.stopShimmer()
            upcomingMoviesShimmer.stopShimmer()
            topRatedMoviesShimmer.stopShimmer()
            trendingMoviesShimmer.stopShimmer()
            swipeShimmer.stopShimmer()
        }
    }

    private fun showShimmerPlaceHolder() = with(binding) {
        normalLayout.hide()
        shimmerInclude.apply {
            shimmerLayout.show()
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
                addItemDecoration(SpacesItemDecorator(Constants.SPACE))
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
                addItemDecoration(SpacesItemDecorator(Constants.SPACE))
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
                addItemDecoration(SpacesItemDecorator(Constants.SPACE))
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