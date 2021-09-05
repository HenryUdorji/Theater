package com.henryudorji.theater.ui.details.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.henryudorji.theater.data.model.cast.Cast
import com.henryudorji.theater.databinding.RvCastLayoutBinding
import com.henryudorji.theater.utils.Constants.BASE_URL_IMAGE
import com.squareup.picasso.Picasso


class CastRvAdapter(): RecyclerView.Adapter<CastRvAdapter.CastViewHolder>() {

    inner class CastViewHolder(val binding: RvCastLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = RvCastLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(BASE_URL_IMAGE + cast.profilePath).into(castImage)
            castName.text = cast.name
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}