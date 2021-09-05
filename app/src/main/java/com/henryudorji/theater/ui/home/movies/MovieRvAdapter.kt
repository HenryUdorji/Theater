package com.henryudorji.theater.ui.home.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.movie.Movie
import com.henryudorji.theater.databinding.RvMovieLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso


class MovieRvAdapter(): RecyclerView.Adapter<MovieRvAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: RvMovieLayoutBinding): RecyclerView.ViewHolder(binding.root)

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
        val binding = RvMovieLayoutBinding.inflate(
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

            movieTitleText.text = movie.title

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