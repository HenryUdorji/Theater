package com.henryudorji.theater.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.R
import com.henryudorji.theater.data.model.review.Review
import com.henryudorji.theater.databinding.RvReviewLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso


class ReviewRvAdapter(): RecyclerView.Adapter<ReviewRvAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: RvReviewLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RvReviewLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = differ.currentList[position]

        holder.binding.apply {
            Picasso.get()
                .load(BASE_URL_IMAGE + review.authorDetails.avatarPath)
                .placeholder(R.drawable.profile_placeholder)
                .into(authorImage)
            authorName.text = review.authorDetails.name
            ratingText.text = review.authorDetails.rating.toString()
            reviewContent.apply {
                text = review.content
                setShowingLine(5)
                setShowMoreColor(resources.getColor(R.color.red, null))
                setShowLessTextColor(resources.getColor(R.color.red, null))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}