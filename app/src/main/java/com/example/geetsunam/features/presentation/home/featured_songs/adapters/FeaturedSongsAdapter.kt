package com.example.geetsunam.features.presentation.home.featured_songs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.FeaturedSongCardBinding
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.utils.models.Song
import javax.inject.Inject

class FeaturedSongsAdapter : RecyclerView.Adapter<FeaturedSongsAdapter.MyViewHolder>() {

    private var songs = emptyList<Song>()

    class MyViewHolder(private val binding: FeaturedSongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @Inject
        lateinit var musicViewModel: MusicViewModel
        fun bind(result: Song) {
            binding.result = result
            binding.from = "featured"
//            binding.musicViewModel = musicViewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeaturedSongCardBinding.inflate(layoutInflater, parent, false)
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
        val recipesDiffUtil = FeaturedSongsDiffUtil(songs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        songs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}