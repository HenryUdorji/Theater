package com.henryudorji.theater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.data.model.detail.Genre
import com.henryudorji.theater.databinding.GenreCustomLayoutBinding
import com.henryudorji.theater.databinding.MovieCustomLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso

//
// Created by hash on 5/3/2021.
//
class GenreRecyclerAdapter(): RecyclerView.Adapter<GenreRecyclerAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(val binding: GenreCustomLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = GenreCustomLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = differ.currentList[position]

        holder.binding.apply {
            genreName.text = genre.name
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}