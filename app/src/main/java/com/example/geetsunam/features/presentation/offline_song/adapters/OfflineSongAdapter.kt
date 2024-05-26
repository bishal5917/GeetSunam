package com.example.geetsunam.features.presentation.offline_song.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.OfflineSongCardBinding
import com.example.geetsunam.utils.SongDiffUtil
import com.example.geetsunam.utils.models.Song

class OfflineSongAdapter : RecyclerView.Adapter<OfflineSongAdapter.MyViewHolder>() {

    private var songs = emptyList<Song>()

    class MyViewHolder(private val binding: OfflineSongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Song) {
            binding.result = result
            binding.from = "offline"
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OfflineSongCardBinding.inflate(layoutInflater, parent, false)
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
        val songDiffUtil = SongDiffUtil(songs, newData)
        val result = DiffUtil.calculateDiff(songDiffUtil)
        songs = newData
        result.dispatchUpdatesTo(this)
    }
}