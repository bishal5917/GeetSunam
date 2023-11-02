package com.example.geetsunam.features.presentation.home.featured_songs.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.utils.models.Song

class FeaturedSongsDiffUtil(
    private val oldList: List<Song>,
    private val newList: List<Song>
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