package com.example.geetsunam.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.utils.models.Song

class SongDiffUtil(
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

            oldList[oldItemPosition].coverArt != newList[newItemPosition].coverArt -> {
                false
            }

            oldList[oldItemPosition].isFeatured != newList[newItemPosition].isFeatured -> {
                false
            }

            oldList[oldItemPosition].isFavourite != newList[newItemPosition].isFavourite -> {
                false
            }

            oldList[oldItemPosition].source != newList[newItemPosition].source -> {
                false
            }

            oldList[oldItemPosition].stream != newList[newItemPosition].stream -> {
                false
            }

            else -> true
        }
    }
}