package com.example.geetsunam.features.presentation.new_song.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.New
import com.example.geetsunam.features.domain.usecases.GetLocalNewUsecase
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.features.domain.usecases.SaveNewUsecase
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
class NewSongViewModel @Inject constructor(
    private val getNewSongsUsecase: GetNewSongsUsecase,
    private val saveNewUsecase: SaveNewUsecase,
    private val getLocalNewUsecase: GetLocalNewUsecase
) : ViewModel() {
    private val _newSongsState = MutableLiveData(NewSongState.idle)
    val newSongsState: LiveData<NewSongState> = _newSongsState

    fun onEvent(event: NewSongEvent) {
        when (event) {
            is NewSongEvent.GetNewSongs -> {
                getSongs(event.token)
            }

            is NewSongEvent.RefreshNew -> {
                fetchSongsFromApi(event.token)
            }

            is NewSongEvent.SaveNew -> {
                saveNew(event.songs)
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        viewModelScope.launch {
            getLocalNewUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (new: New in it) {
                        val song = Song(
                            id = new.id,
                            isFavourite = new.isFavourite,
                            isFeatured = new.isFavourite,
                            source = new.source,
                            stream = new.stream,
                            title = new.songName,
                            coverArt = new.coverArt,
                            duration = new.duration,
                            artists = Artist(fullname = new.artistName)
                        )
                        songs.add(song)
                    }
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.SUCCESS,
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
        getNewSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.LOADING,
                            message = "Getting new songs"
                        )
                    )
                }

                is Resource.Success -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            songs = result.data?.data?.songs,
                        )
                    )
                }

                is Resource.Error -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveNew(songs: List<Song?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveNewUsecase.call(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}