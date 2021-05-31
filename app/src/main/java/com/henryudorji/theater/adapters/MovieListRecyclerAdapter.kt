package com.henryudorji.theater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.databinding.MovieCustomLayoutBinding
import com.henryudorji.theater.databinding.MovieCustomLayoutListBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso

//
// Created by hash on 5/3/2021.
//
class MovieListRecyclerAdapter(): RecyclerView.Adapter<MovieListRecyclerAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieCustomLayoutListBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieCustomLayoutListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(BASE_URL_IMAGE + movie.posterPath).into(movieImage)
            movieTitleText.text = movie.originalTitle
            ratingText.text = movie.voteAverage.toString()

            this.root.setOnClickListener {
                onItemClickListener?.let { it(movie.id) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((movieId: Int) -> Unit) ? = null

    fun setOnItemClickListener(listener: (movieId: Int) -> Unit) {
        onItemClickListener = listener
    }
}