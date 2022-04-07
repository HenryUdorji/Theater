package com.henryudorji.theater.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.video.Video
import com.henryudorji.theater.databinding.RvTrailerLayoutBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TrailerVideoRvAdapter(val lifecycle: Lifecycle) : RecyclerView.Adapter<TrailerVideoRvAdapter.TrailerVideoViewHolder>() {

    inner class TrailerVideoViewHolder(val binding: RvTrailerLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video?) {
            video?.let {
                //Play video of movie or series
                binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = it.key
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerVideoViewHolder {
        val binding = RvTrailerLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        lifecycle.addObserver(binding.youtubePlayerView)
        return TrailerVideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailerVideoViewHolder, position: Int) {
        val video = differ.currentList[position]

        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}