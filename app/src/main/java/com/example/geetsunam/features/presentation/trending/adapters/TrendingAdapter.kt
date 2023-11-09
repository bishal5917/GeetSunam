package com.example.geetsunam.features.presentation.trending.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.SongCardBinding
import com.example.geetsunam.features.presentation.home.featured_songs.adapters.FeaturedSongsDiffUtil
import com.example.geetsunam.utils.models.Song

class TrendingAdapter : RecyclerView.Adapter<TrendingAdapter.MyViewHolder>() {

    private var songs = emptyList<Song>()

    class MyViewHolder(private val binding: SongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Song) {
            binding.result = result
            binding.from = "trending"
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SongCardBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = songs[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun setData(newData: List<Song>) {
        val trendingDiffUtil = FeaturedSongsDiffUtil(songs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(trendingDiffUtil)
        songs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}