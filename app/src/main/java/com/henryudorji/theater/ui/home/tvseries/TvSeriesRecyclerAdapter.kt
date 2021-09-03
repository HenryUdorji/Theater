package com.henryudorji.theater.ui.home.tvseries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.tv.TvSeries
import com.henryudorji.theater.databinding.MovieCustomLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso


class TvSeriesRecyclerAdapter(): RecyclerView.Adapter<TvSeriesRecyclerAdapter.TvSeriesViewHolder>() {

    inner class TvSeriesViewHolder(val binding: MovieCustomLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<TvSeries>() {
        override fun areItemsTheSame(oldItem: TvSeries, newItem: TvSeries): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvSeries, newItem: TvSeries): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvSeriesViewHolder {
        val binding = MovieCustomLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TvSeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvSeriesViewHolder, position: Int) {
        val tvSeries = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(BASE_URL_IMAGE + tvSeries.posterPath).into(movieImage)

            movieTitleText.text = tvSeries.name

            ratingText.text = tvSeries.voteAverage.toString()

            this.root.setOnClickListener {
                onItemClickListener?.let { it(tvSeries.id) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((tvSeriesId: Int) -> Unit) ? = null

    fun setOnItemClickListener(listener: (tvSeriesId: Int) -> Unit) {
        onItemClickListener = listener
    }
}