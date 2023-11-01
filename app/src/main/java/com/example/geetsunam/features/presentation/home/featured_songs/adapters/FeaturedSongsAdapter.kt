package com.example.geetsunam.features.presentation.home.featured_songs.adapters

import com.example.geetsunam.features.presentation.home.featured_artists.adapters.FeaturedArtistsDiffUtil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.FavArtistsCardBinding
import com.example.geetsunam.databinding.FeaturedSongCardBinding
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.songs.SongResponseModel

class FeaturedSongsAdapter : RecyclerView.Adapter<FeaturedSongsAdapter.MyViewHolder>() {

    private var songs = emptyList<SongResponseModel.Data.Song>()

    class MyViewHolder(private val binding: FeaturedSongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SongResponseModel.Data.Song) {
            binding.result = result
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

    fun setData(newData: List<SongResponseModel.Data.Song>) {
        val recipesDiffUtil = FeaturedSongsDiffUtil(songs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        songs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}