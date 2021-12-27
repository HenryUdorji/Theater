package com.henryudorji.theater.ui.home.tvseries

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
import com.henryudorji.theater.utils.Constants.ON_THE_AIR
import com.henryudorji.theater.utils.Constants.MOVIE_CATEGORY
import com.henryudorji.theater.utils.Constants.MOVIE_ID
import com.henryudorji.theater.utils.Constants.POPULAR
import com.henryudorji.theater.utils.Constants.TOP_RATED
import com.henryudorji.theater.utils.Constants.TV_SERIES
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeTvSeriesFragment: BaseFragment<FragmentHomeDetailBinding, HomeViewModel>() {

    private val TAG = "HomeTvSeriesFragment"
    override val viewModel: HomeViewModel by activityViewModels()
    private lateinit var popularAdapter: TvSeriesRvAdapter
    private lateinit var onTheAirAdapter: TvSeriesRvAdapter
    private lateinit var topRatedAdapter: TvSeriesRvAdapter

    private var page = 1

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            getPopularTvSeries(page)
            getOnTheAirTvSeries(page)
            getTopRatedTvSeries(page)
        }

        initViews()
        observeMovieData()
    }

    private fun observeMovieData() {
        viewModel.onTheAirLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        onTheAirAdapter.differ.submitList(it.tvSeries.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> showShimmerPlaceHolder()
            }
        }

        viewModel.topRatedTvSeriesLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    state.data?.let {
                        topRatedAdapter.differ.submitList(it.tvSeries.shuffled().subList(0, 10))
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, state.message!!, Snackbar.LENGTH_LONG).show()
                }
                is Resource.Loading -> showShimmerPlaceHolder()
            }
        }

        viewModel.popularTvSeriesLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Success -> {
                    hideShimmerPlaceHolder()
                    state.data?.let {
                        popularAdapter.differ.submitList(it.tvSeries.shuffled().subList(0, 10))
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
        binding.upcomingMoviesText.text = getString(R.string.on_the_air)

        popularAdapter = TvSeriesRvAdapter()
        onTheAirAdapter = TvSeriesRvAdapter()
        topRatedAdapter = TvSeriesRvAdapter()

        binding.popularRv.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        popularAdapter.setOnItemClickListener { tvSeriesID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, tvSeriesID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        //Upcoming is same as OnTheAir
        binding.upcomingRv.apply {
            adapter = onTheAirAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        onTheAirAdapter.setOnItemClickListener { tvSeriesID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, tvSeriesID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }

        binding.topRatedRv.apply {
            adapter = topRatedAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false).also {
                hasFixedSize()
            }
        }
        topRatedAdapter.setOnItemClickListener { tvSeriesID ->
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, tvSeriesID)
                putInt(FRAG_ID, TV_SERIES)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }


        binding.showAllPopularText.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString(MOVIE_CATEGORY, POPULAR)
//                putInt(FRAG_ID, TV_SERIES)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
            Snackbar.make(binding.root, "Feature coming soon", Snackbar.LENGTH_SHORT).show()
        }

        //Latest is the same as Upcoming
        binding.showAllUpcomingMoviesText.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString(MOVIE_CATEGORY, ON_THE_AIR)
//                putInt(FRAG_ID, TV_SERIES)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
            Snackbar.make(binding.root, "Feature coming soon", Snackbar.LENGTH_SHORT).show()
        }

        binding.showAllTopRatedMoviesText.setOnClickListener {
//            val bundle = Bundle().apply {
//                putString(MOVIE_CATEGORY, TOP_RATED)
//                putInt(FRAG_ID, TV_SERIES)
//            }
//            findNavController().navigate(R.id.action_homeFragment_to_movieListFragment, bundle)
            Snackbar.make(binding.root, "Feature coming soon", Snackbar.LENGTH_SHORT).show()
        }
    }
}