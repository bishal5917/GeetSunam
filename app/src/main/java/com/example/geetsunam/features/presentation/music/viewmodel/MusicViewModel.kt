package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.SetPlaylist -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        playlistName = event.playlistName, currentPlaylist = event.playlist
                    )
                )
            }

            is MusicEvent.SetCurrentSongId -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        currentSongId = event.songId
                    )
                )
            }
        }
    }
}