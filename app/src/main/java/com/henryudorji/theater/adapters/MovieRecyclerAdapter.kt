package com.henryudorji.theater.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.R
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.databinding.MovieCustomLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.henryudorji.theater.utils.Constants.MOVIE_LIST_FRAG
import com.henryudorji.theater.utils.Constants.OTHER_FRAG
import com.squareup.picasso.Picasso

//
// Created by hash on 5/3/2021.
//
class MovieRecyclerAdapter(private val fromWhichFrag: Int): RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieCustomLayoutBinding): RecyclerView.ViewHolder(binding.root)

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
        val binding = MovieCustomLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]

        holder.binding.apply {
            when(fromWhichFrag) {
                MOVIE_LIST_FRAG -> root.layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.dimen._170sdp)
                OTHER_FRAG -> root.layoutParams =
                        ViewGroup.LayoutParams(R.dimen._120sdp, R.dimen._170sdp)
            }
            Picasso.get().load(BASE_URL_IMAGE + movie.posterPath).into(movieImage)
            movieTitleText.text = movie.originalTitle
            ratingText.text = movie.voteAverage.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((id: Int) -> Unit) ? = null

    fun setOnItemClickListener(listener: (id: Int) -> Unit) {
        onItemClickListener = listener
    }
}