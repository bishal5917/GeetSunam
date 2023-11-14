package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.utils.models.Song

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
                        totalSongs = event.playlist?.size
                    )
                )
            }

            is MusicEvent.SetAndRetainPlaylist -> {
                val newPlaylist = ArrayList<Song>()
                newPlaylist.addAll(_musicState.value?.currentPlaylist as ArrayList<Song>)
                newPlaylist.addAll(event.playlist as ArrayList<Song>)
                _musicState.postValue(
                    _musicState.value?.copy(
                        playlistName = event.playlistName,
                        currentPlaylist = newPlaylist,
                        totalSongs = newPlaylist.size
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

            is MusicEvent.Reset -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        playlistName = null, currentPlaylist = null, totalSongs = null
                    )
                )
            }

            else -> {}
        }
    }

    private fun setSong(songId: String) {
        val song = _musicState.value?.currentPlaylist?.find { song ->
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
        val currSong = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == _musicState.value?.currentSong?.id
        }
        val currIdx = _musicState.value?.currentPlaylist?.indexOf(currSong)
        val song = if (isNext) {
            if (currIdx == _musicState.value?.totalSongs!! - 1) {
                return
            }
            _musicState.value?.currentPlaylist?.elementAt(currIdx!! + 1)
        } else {
            if (currIdx == 0) {
                return
            }
            _musicState.value?.currentPlaylist?.elementAt(currIdx!! - 1)
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