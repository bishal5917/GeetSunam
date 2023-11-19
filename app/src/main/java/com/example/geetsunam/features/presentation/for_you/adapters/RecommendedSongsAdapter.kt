package com.example.geetsunam.features.presentation.for_you.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.SongCardBinding
import com.example.geetsunam.utils.SongDiffUtil
import com.example.geetsunam.utils.models.Song

class RecommendedSongsAdapter : RecyclerView.Adapter<RecommendedSongsAdapter.MyViewHolder>() {

    private var songs = emptyList<Song>()

    class MyViewHolder(private val binding: SongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Song) {
            binding.result = result
            binding.from = "recommended"
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
        val recommendedDiffUtil = SongDiffUtil(songs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recommendedDiffUtil)
        songs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}