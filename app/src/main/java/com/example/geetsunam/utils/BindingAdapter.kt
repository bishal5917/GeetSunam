package com.example.geetsunam.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.geetsunam.R
import com.example.geetsunam.features.data.models.songs.SongResponseModel
import com.example.geetsunam.features.presentation.home.HomeFragmentDirections
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.new_song.NewSongFragmentDirections
import com.example.geetsunam.features.presentation.trending.TrendingFragmentDirections
import javax.inject.Inject

class BindingAdapter {

    companion object {
        @BindingAdapter("clickedFrom", "getSongId", requireAll = true)
        @JvmStatic
        fun onMusicCardClickedListener(
            songCard: ConstraintLayout, from: String, songId: String
        ) {
            songCard.setOnClickListener {
                try {
                    if (from == "trending") {
                        val action =
                            TrendingFragmentDirections.actionTrendingFragmentToMusicActivity(songId)
                        songCard.findNavController().navigate(action)
                    } else if (from == "featured") {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToMusicActivity(songId)
                        songCard.findNavController().navigate(action)
                    } else if (from == "new_song") {
                        val action =
                            NewSongFragmentDirections.actionNewSongFragmentToMusicActivity(songId)
                        songCard.findNavController().navigate(action)
                    }
                } catch (ex: Exception) {
                    Log.d("setOnClickListener", "${ex.message}")
                }
            }

        }

        @BindingAdapter("setText")
        @JvmStatic
        fun setText(textView: TextView, value: String) {
            textView.text = value
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun setNetworkImage(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                placeholder(R.drawable.placeholder)
                error(R.drawable.error)
            }
        }
    }
}