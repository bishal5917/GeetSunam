package com.example.geetsunam.features.presentation.offline_song.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.features.domain.usecases.DeleteOfflineSongUsecase
import com.example.geetsunam.features.domain.usecases.GetOfflineSongsUsecase
import com.example.geetsunam.utils.FileUtil
import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineSongViewModel @Inject constructor(
    private val getOfflineSongsUsecase: GetOfflineSongsUsecase,
    private val deleteOfflineSongUsecase: DeleteOfflineSongUsecase
) : ViewModel() {
    private val _state = MutableLiveData(OfflineSongState.idle)
    val state: LiveData<OfflineSongState> = _state

    fun onEvent(event: OfflineSongEvent) {
        when (event) {
            is OfflineSongEvent.GetOfflineSongs -> {
                getSongs()
            }

            is OfflineSongEvent.DeleteOfflineSong -> {
                deleteMusic(event.id, event.filePath)
            }

            else -> {}
        }
    }

    private fun getSongs() {
        _state.postValue(
            _state.value?.copy(
                status = OfflineSongState.OfflineSongStatus.LOADING, message = "Getting songs..."
            )
        )
        viewModelScope.launch {
            getOfflineSongsUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (s: OfflineSong in it) {
                        val song = Song(
                            id = s.id,
                            isFavourite = s.isFavourite,
                            isFeatured = s.isFavourite,
                            source = s.source,
                            stream = s.stream,
                            title = s.songName,
                            coverArt = s.coverArt,
                            duration = s.duration,
                            artists = Artist(fullname = s.artistName),
                            filePath = s.filePath
                        )
                        songs.add(song)
                    }
                    _state.postValue(
                        _state.value?.copy(
                            status = OfflineSongState.OfflineSongStatus.SUCCESS,
                            message = "success",
                            songs = songs,
                        )
                    )
                } else {
                    _state.postValue(
                        _state.value?.copy(
                            status = OfflineSongState.OfflineSongStatus.EMPTY,
                            message = "No downloads"
                        )
                    )
                }
            }
        }
    }

    private fun deleteMusic(id: String, path: String) {
        _state.value?.copy(
            status = OfflineSongState.OfflineSongStatus.DELETING,
            message = "Deleting a file...",
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                deleteOfflineSongUsecase.call(id)
                FileUtil().deleteFile(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _state.value?.copy(
            status = OfflineSongState.OfflineSongStatus.DELETED,
            message = "file deleted.",
        )
    }
}