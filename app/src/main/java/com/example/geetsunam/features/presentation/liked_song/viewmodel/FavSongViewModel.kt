package com.example.geetsunam.features.presentation.liked_song.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.Favourite
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFavouriteUsecase
import com.example.geetsunam.features.domain.usecases.SaveFavouriteUsecase
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
class FavSongViewModel @Inject constructor(
    private val getFavouriteSongsUsecase: GetFavouriteSongsUsecase,
    private val saveFavouriteUsecase: SaveFavouriteUsecase,
    private val getLocalFavouriteUsecase: GetLocalFavouriteUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(FavSongState.idle)
    val favSongsState: LiveData<FavSongState> = _liveState

    fun onEvent(event: FavSongEvent) {
        when (event) {
            is FavSongEvent.GetFavouriteSongs -> {
                getSongs(event.token)
            }

            is FavSongEvent.RefreshFavourites -> {
                fetchSongsFromApi(event.token)
            }

            is FavSongEvent.SaveFavourites -> {
                saveFavourites(event.songs)
            }

            is FavSongEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = FavSongState.FavSongStatus.IDLE, message = "IDLE"
                    )
                )
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        viewModelScope.launch {
            getLocalFavouriteUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (fav: Favourite in it) {
                        val song = Song(
                            id = fav.id,
                            isFavourite = fav.isFavourite,
                            isFeatured = fav.isFavourite,
                            source = fav.source,
                            stream = fav.stream,
                            title = fav.songName,
                            coverArt = fav.coverArt,
                            duration = fav.duration,
                            artists = Artist(fullname = fav.artistName)
                        )
                        songs.add(song)
                    }
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.SUCCESS,
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
        getFavouriteSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.LOADING,
                            message = "Getting your favourites..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            songs = result.data?.data?.songs,
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveFavourites(songs: List<Song?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveFavouriteUsecase.call(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}