package com.example.geetsunam.features.presentation.liked_song.viewmodel


sealed class FavSongEvent {
    data class GetFavouriteSongs(val token: String) : FavSongEvent()
    object Reset : FavSongEvent()
}