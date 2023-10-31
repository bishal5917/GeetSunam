package com.example.geetsunam.features.presentation.home.genres.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.geetsunam.features.data.models.genres.GenreResponseModel

class GenreDiffUtil(
    private val oldList: List<GenreResponseModel.Data.Genre>,
    private val newList: List<GenreResponseModel.Data.Genre>
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