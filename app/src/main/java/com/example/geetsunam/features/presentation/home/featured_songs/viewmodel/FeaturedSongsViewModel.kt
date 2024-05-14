package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.database.entities.Featured
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedSongsUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedSongsUsecase
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongEvent
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongState
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedSongsViewModel @Inject constructor(
    private val getFeaturedSongsUsecase: GetFeaturedSongsUsecase,
    private val saveFeaturedSongsUsecase: SaveFeaturedSongsUsecase,
    private val getLocalFeaturedSongsUsecase: GetLocalFeaturedSongsUsecase
) : ViewModel() {
    private val _featuredSongState = MutableLiveData(FeaturedSongsState.idle)
    val featuredSongState: LiveData<FeaturedSongsState> = _featuredSongState

    fun onEvent(event: FeaturedSongsEvent) {
        when (event) {
            is FeaturedSongsEvent.GetFeaturedSongs -> {
                getSongs(event.token)
            }

            is FeaturedSongsEvent.RefreshFeaturedSongs -> {
                fetchSongsFromApi(event.token)
            }

            is FeaturedSongsEvent.SaveFeaturedSongs -> {
                saveFeatured(event.songs)
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        _featuredSongState.postValue(
            _featuredSongState.value?.copy(
                status = FeaturedSongsState.SongStatus.LOADING,
                message = "Getting songs",
            )
        )
        viewModelScope.launch {
            getLocalFeaturedSongsUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (s: Featured in it) {
                        val song = Song(
                            id = s.id,
                            isFavourite = s.isFavourite,
                            isFeatured = s.isFavourite,
                            source = s.source,
                            stream = s.stream,
                            title = s.songName,
                            coverArt = s.coverArt,
                            duration = s.duration,
                            artists = Artist(fullname = s.artistName)
                        )
                        songs.add(song)
                    }
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.SUCCESS,
                            message = "success",
                            fromApi = false,
                            songs = songs,
                        )
                    )
                } else {
                    fetchSongsFromApi(token)
                }
            }
        }
    }

    private fun fetchSongsFromApi(token: String) {
        getFeaturedSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.LOADING,
                            message = "Getting songs"
                        )
                    )
                }

                is Resource.Success -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            songs = result.data?.data?.songs,
                        )
                    )
                }

                is Resource.Error -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveFeatured(songs: List<Song?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveFeaturedSongsUsecase.call(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}