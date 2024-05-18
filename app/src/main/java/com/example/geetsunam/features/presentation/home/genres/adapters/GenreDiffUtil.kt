package com.example.geetsunam.features.presentation.home.genres.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.utils.models.Genre

class GenreDiffUtil(
    private val oldList: List<Genre>,
    private val newList: List<Genre>
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

            oldList[oldItemPosition].image != newList[newItemPosition].image -> {
                false
            }

            oldList[oldItemPosition].name != newList[newItemPosition].name -> {
                false
            }

            else -> true
        }
    }
}