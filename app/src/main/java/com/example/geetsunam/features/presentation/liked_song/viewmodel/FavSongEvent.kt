package com.example.geetsunam.features.presentation.liked_song.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class FavSongEvent {
    data class GetFavouriteSongs(val token: String) : FavSongEvent()
    data class RefreshFavourites(val token: String) : FavSongEvent()
    data class SaveFavourites(val songs: List<Song?>?) : FavSongEvent()
    object Reset : FavSongEvent()
}