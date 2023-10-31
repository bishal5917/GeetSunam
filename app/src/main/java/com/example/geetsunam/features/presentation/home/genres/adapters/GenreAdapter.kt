package com.example.geetsunam.features.presentation.home.genres.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.GenreCardBinding
import com.example.geetsunam.features.data.models.genres.GenreResponseModel

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.MyViewHolder>() {

    private var genre = emptyList<GenreResponseModel.Data.Genre>()

    class MyViewHolder(private val binding: GenreCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GenreResponseModel.Data.Genre) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GenreCardBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = genre[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return genre.size
    }

    fun setData(newData: List<GenreResponseModel.Data.Genre>) {
        val genreDiffUtil = GenreDiffUtil(genre, newData)
        val diffUtilResult = DiffUtil.calculateDiff(genreDiffUtil)
        genre = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}