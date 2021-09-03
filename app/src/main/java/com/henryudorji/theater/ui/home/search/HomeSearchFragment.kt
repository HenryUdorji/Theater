package com.henryudorji.theater.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.henryudorji.theater.databinding.FragmentHomeSearchBinding
import com.henryudorji.theater.ui.base.BaseFragment
import com.henryudorji.theater.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeSearchFragment: BaseFragment<FragmentHomeSearchBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}