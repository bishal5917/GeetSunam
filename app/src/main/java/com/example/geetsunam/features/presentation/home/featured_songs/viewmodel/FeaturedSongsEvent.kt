package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

import com.example.geetsunam.utils.models.Song

sealed class FeaturedSongsEvent {
    data class GetFeaturedSongs(val token: String) : FeaturedSongsEvent()
    data class RefreshFeaturedSongs(val token: String) : FeaturedSongsEvent()
    data class SaveFeaturedSongs(val songs: List<Song?>?) : FeaturedSongsEvent()
}