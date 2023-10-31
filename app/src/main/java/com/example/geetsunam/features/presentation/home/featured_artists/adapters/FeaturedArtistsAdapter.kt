package com.example.geetsunam.features.presentation.home.featured_artists.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.FavArtistsCardBinding
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel

class FeaturedArtistsAdapter : RecyclerView.Adapter<FeaturedArtistsAdapter.MyViewHolder>() {

    private var artists = emptyList<ArtistResponseModel.Data.Artist>()

    class MyViewHolder(private val binding: FavArtistsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ArtistResponseModel.Data.Artist) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavArtistsCardBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = artists[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    fun setData(newData: List<ArtistResponseModel.Data.Artist>) {
        val recipesDiffUtil = FeaturedArtistsDiffUtil(artists, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        artists = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}