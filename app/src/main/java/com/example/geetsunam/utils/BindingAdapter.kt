package com.example.geetsunam.utils

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.geetsunam.R
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.features.presentation.home.HomeFragmentDirections
import com.example.geetsunam.features.presentation.liked_song.LikedSongFragmentDirections
import com.example.geetsunam.features.presentation.music.MusicActivity
import com.example.geetsunam.features.presentation.new_song.NewSongFragmentDirections
import com.example.geetsunam.features.presentation.single_genre.GenreActivity
import com.example.geetsunam.features.presentation.trending.TrendingFragmentDirections
import com.example.geetsunam.utils.models.Song

class BindingAdapter {

    companion object {
        @BindingAdapter("clickedFrom", "getSong", requireAll = true)
        @JvmStatic
        fun onMusicCardClickedListener(
            songCard: ConstraintLayout, from: String, song: Song
        ) {
            songCard.setOnClickListener {
                val songEntity = SongEntity(
                    id = song.id,
                    coverArt = song.coverArt,
                    artistName = song.artists?.fullname,
                    songName = song.title,
                    duration = song.duration,
                    source = song.source,
                    stream = song.stream,
                    isFavourite = song.isFavourite,
                )
                try {
                    if (from == "trending") {
                        val action =
                            TrendingFragmentDirections.actionTrendingFragmentToMusicActivity(
                                songEntity
                            )
                        songCard.findNavController().navigate(action)

                    } else if (from == "featured") {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToMusicActivity(songEntity)
                        songCard.findNavController().navigate(action)
                    } else if (from == "new_song") {
                        val action = NewSongFragmentDirections.actionNewSongFragmentToMusicActivity(
                            songEntity
                        )
                        songCard.findNavController().navigate(action)
                    } else if (from == "favourites") {
                        val action =
                            LikedSongFragmentDirections.actionLikedSongFragmentToMusicActivity(
                                songEntity
                            )
                        songCard.findNavController().navigate(action)
                    } else if (from == "genre_songs" || from == "artist_songs") {
                        val intent = Intent(songCard.context, MusicActivity::class.java)
                        intent.putExtra("song", songEntity)
                        songCard.context.startActivity(intent)
                    }
                } catch (ex: Exception) {
                    Log.d("setOnClickListener", "${ex.message}")
                }
            }
        }

        @BindingAdapter("onGenreClicked")
        @JvmStatic
        fun onGenreClicked(imageView: ImageView, genre: GenreResponseModel.Data.Genre) {
            imageView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToGenreActivity(genre)
                imageView.findNavController().navigate(action)
            }
        }

        @BindingAdapter("onArtistClicked")
        @JvmStatic
        fun onArtistClicked(imageView: ImageView, artist: ArtistResponseModel.Data.Artist) {
            imageView.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToArtistActivity(artist)
                imageView.findNavController().navigate(action)
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