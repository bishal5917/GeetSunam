package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geetsunam.R
import com.example.geetsunam.features.domain.entities.SongEntity

class MusicViewModel : ViewModel() {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.SetPlaylist -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        playlistName = event.playlistName,
                        currentPlaylist = event.playlist,
                        totalSongs = event.playlist.songs?.size
                    )
                )
            }

            is MusicEvent.SetCurrentSong -> {
                setSong(event.songId)
            }

            is MusicEvent.PlayNextSong -> {
                playNextSong(isNext = true)
            }

            is MusicEvent.PlayPreviousSong -> {
                playNextSong(isNext = false)
            }

            else -> {}
        }
    }

    private fun setSong(songId: String) {
        val song = _musicState.value?.currentPlaylist?.songs?.find { song ->
            song?.id == songId
        }
        _musicState.postValue(
            _musicState.value?.copy(
                currentSong = SongEntity(
                    id = song?.id,
                    coverArt = song?.coverArt,
                    artistName = song?.artists?.fullname,
                    songName = song?.title,
                    duration = song?.duration,
                    source = song?.source,
                    stream = song?.stream,
                    isFavourite = song?.isFavourite,
                )
            )
        )
    }

    private fun playNextSong(isNext: Boolean) {
        //find current song index
        val currSong = _musicState.value?.currentPlaylist?.songs?.find { song ->
            song?.id == _musicState.value?.currentSong?.id
        }
        val currIdx = _musicState.value?.currentPlaylist?.songs?.indexOf(currSong)
        val song = if (isNext) {
            if (currIdx == _musicState.value?.totalSongs!! - 1) {
                return
            }
            _musicState.value?.currentPlaylist?.songs?.elementAt(currIdx!! + 1)
        } else {
            if (currIdx == 0) {
                return
            }
            _musicState.value?.currentPlaylist?.songs?.elementAt(currIdx!! - 1)
        }
        _musicState.postValue(
            _musicState.value?.copy(
                currentSong = SongEntity(
                    id = song?.id,
                    coverArt = song?.coverArt,
                    artistName = song?.artists?.fullname,
                    songName = song?.title,
                    duration = song?.duration,
                    source = song?.source,
                    stream = song?.stream,
                    isFavourite = song?.isFavourite,
                )
            )
        )
    }
}