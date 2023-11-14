package com.example.geetsunam.features.presentation.home.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.ArtistCardBinding
import com.example.geetsunam.features.presentation.home.featured_artists.adapters.FeaturedArtistsDiffUtil
import com.example.geetsunam.utils.models.Artist

class ArtistSearchAdapter : RecyclerView.Adapter<ArtistSearchAdapter.MyViewHolder>() {

    private var artists = emptyList<Artist>()

    class MyViewHolder(private val binding: ArtistCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Artist) {
            binding.result = result
            binding.from = "search"
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ArtistCardBinding.inflate(layoutInflater, parent, false)
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

    fun setData(newData: List<Artist>) {
        val artistDiffUtil = FeaturedArtistsDiffUtil(artists, newData)
        val diffUtilResult = DiffUtil.calculateDiff(artistDiffUtil)
        artists = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}