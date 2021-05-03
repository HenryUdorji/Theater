package com.henryudorji.theater.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

//
// Created by hash on 5/2/2021.
//
class ViewpagerAdapter(
    fragment: Fragment,
    private val fragments: MutableList<Fragment>
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}