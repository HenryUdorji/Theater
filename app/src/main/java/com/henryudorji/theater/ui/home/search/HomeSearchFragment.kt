package com.henryudorji.theater.ui.home.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.R
import com.henryudorji.theater.databinding.FragmentHomeSearchBinding
import com.henryudorji.theater.ui.base.BaseFragment
import com.henryudorji.theater.ui.home.HomeViewModel
import com.henryudorji.theater.ui.home.MovieRvAdapter
import com.henryudorji.theater.ui.list.MovieListRvAdapter
import com.henryudorji.theater.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeSearchFragment: BaseFragment<FragmentHomeSearchBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()
    private lateinit var searchAdapter: MovieListRvAdapter
    private val page: Int = 1

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchRv()

        binding.searchEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString().trim().lowercase()
                if (searchQuery.isNotEmpty()) {
                    viewModel.searchMovies(page, searchQuery)
                    observeSearch()
                }
            }

        })
    }

    private fun setupSearchRv() = with(binding) {
        searchAdapter = MovieListRvAdapter()

        searchRv.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(activity, 3).also {
                hasFixedSize()
            }
        }
        searchAdapter.setOnItemClickListener { movieID ->
            val bundle = Bundle().apply {
                putInt(Constants.MOVIE_ID, movieID)
                putInt(Constants.FRAG_ID, Constants.MOVIE)
            }
            findNavController().navigate(R.id.action_homeFragment_to_movieDetailFragment, bundle)
        }
    }

    private fun observeSearch() {
        viewModel.searchMoviesLiveData.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Loading -> showShimmerPlaceHolder()
                is Resource.Error -> state.message?.let {
                    Snackbar.make(binding.root, it, Snackbar
                        .LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    hideShimmerPlaceHolder()
                    searchAdapter.differ.submitList(state.data?.movies)
                }
            }
        }
    }

    private fun hideShimmerPlaceHolder() = with(binding) {
        searchRv.show()
        shimmerInclude.apply {
            shimmerLayout.hide()
            popularShimmerFrame.stopShimmer()
        }
    }

    private fun showShimmerPlaceHolder() = with(binding) {
        searchRv.hide()
        shimmerInclude.apply {
            shimmerLayout.show()
            popularShimmerFrame.startShimmer()
        }
    }
}