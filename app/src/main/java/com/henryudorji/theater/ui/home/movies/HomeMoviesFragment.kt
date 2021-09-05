package com.henryudorji.theater.ui.home.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.FragmentHomeDetailBinding
import com.henryudorji.theater.ui.base.BaseFragment
import com.henryudorji.theater.ui.home.HomeViewModel
import com.henryudorji.theater.utils.*
import com.henryudorji.theater.utils.Constants.FRAG_ID
import com.henryudorji.theater.utils.Constants.MOVIE
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.UPCOMING
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeMoviesFragment: BaseFragment<FragmentHomeDetailBinding, HomeViewModel>() {
    private val TAG = "HomeMoviesFragment"
    override val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularAdapter: MovieRvAdapter
    private lateinit var upcomingAdapter: MovieRvAdapter
    private lateinit var topRatedAdapter: MovieRvAdapter

    private var page = 1

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeDetailBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            getPopularMovies(page)
            getUpcomingMovies(page)
            getTopRatedMovies(page)
        }

        initViews()
        observeMovieData()
    }


    private fun observeMovieData() {
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

    private fun hideShimmerPlaceHolder() = with(binding) {
        normalLayout.show()
        shimmerInclude.apply {
            shimmerLayout.hide()
            popularShimmerFrame.stopShimmer()
            upcomingMoviesShimmer.stopShimmer()
            topRatedMoviesShimmer.stopShimmer()
        }
    }

    private fun showShimmerPlaceHolder() = with(binding) {
        normalLayout.hide()
        shimmerInclude.apply {
            shimmerLayout.show()
            popularShimmerFrame.startShimmer()
            upcomingMoviesShimmer.startShimmer()
            topRatedMoviesShimmer.startShimmer()
        }
    }

    private fun initViews() {
        popularAdapter = MovieRvAdapter()
        upcomingAdapter = MovieRvAdapter()
        topRatedAdapter = MovieRvAdapter()

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