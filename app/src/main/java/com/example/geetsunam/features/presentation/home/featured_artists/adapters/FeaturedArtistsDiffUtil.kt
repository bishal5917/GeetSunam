package com.example.geetsunam.features.presentation.home.featured_artists.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.utils.models.Artist

class FeaturedArtistsDiffUtil(
    private val oldList: List<Artist>,
    private val newList: List<Artist>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }

            oldList[oldItemPosition].profileImage != newList[newItemPosition].profileImage -> {
                false
            }

            oldList[oldItemPosition].fullname != newList[newItemPosition].fullname -> {
                false
            }

            else -> true
        }
    }
}