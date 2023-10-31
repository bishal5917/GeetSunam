package com.example.geetsunam.features.presentation.home.featured_songs.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.features.data.models.songs.SongResponseModel

class FeaturedSongsDiffUtil(
    private val oldList: List<SongResponseModel.Data.Song>,
    private val newList: List<SongResponseModel.Data.Song>
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

            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }

            oldList[oldItemPosition].duration != newList[newItemPosition].duration -> {
                false
            }

            else -> true
        }
    }
}