package com.example.geetsunam.features.presentation.music.viewmodel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.PlayerUtil
import com.example.geetsunam.utils.models.Song
import kotlin.random.Random

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

            is MusicEvent.SetAndPlayCurrent -> {
                setCurrentSong(event.songId)
                playSong(event.binding, event.mediaPlayer)
            }

            is MusicEvent.PlayNextSong -> {
                playNextSong(isNext = true, event.binding, event.mediaPlayer)
            }

            is MusicEvent.PlayPreviousSong -> {
                playNextSong(isNext = false, event.binding, event.mediaPlayer)
            }

            is MusicEvent.ChangePlayMode -> {
                if (_musicState.value?.playMode == MusicState.PlayMode.Serial) {
                    _musicState.postValue(
                        _musicState.value?.copy(playMode = MusicState.PlayMode.LoopCurrent)
                    )
                }
                if (_musicState.value?.playMode == MusicState.PlayMode.LoopCurrent) {
                    _musicState.postValue(
                        _musicState.value?.copy(playMode = MusicState.PlayMode.Random)
                    )
                }
                if (_musicState.value?.playMode == MusicState.PlayMode.Random) {
                    _musicState.postValue(
                        _musicState.value?.copy(playMode = MusicState.PlayMode.Serial)
                    )
                }
            }

            is MusicEvent.PlayAnother -> {
                if (_musicState.value?.playMode == MusicState.PlayMode.Serial) {
                    setNextSong()
                    resetMedia(event.binding, event.mediaPlayer)
                    playSong(event.binding, event.mediaPlayer)
                }
                if (_musicState.value?.playMode == MusicState.PlayMode.LoopCurrent) {
                    event.mediaPlayer.seekTo(0)
                    event.mediaPlayer.start()
                }
                if (_musicState.value?.playMode == MusicState.PlayMode.Random) {
                    setRandomSong()
                    resetMedia(event.binding, event.mediaPlayer)
                    playSong(event.binding, event.mediaPlayer)
                }
            }

            is MusicEvent.Shuffle -> {
                setRandomSong()
                resetMedia(event.binding, event.mediaPlayer)
                playSong(event.binding, event.mediaPlayer)
            }

            is MusicEvent.Reset -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        status = MusicState.MusicStatus.IDLE
                    )
                )
            }

            else -> {}
        }
    }

    private fun setCurrentSong(songId: String) {
        val song = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == songId
        }
        Log.d(LogTag.PLAYER, "$song")
        _musicState.value = _musicState.value?.copy(
            status = MusicState.MusicStatus.IDLE, currentSong = SongEntity(
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
    }

    private fun playSong(binding: ActivityMusicBinding, mediaPlayer: MediaPlayer) {
        val songEntity = _musicState.value?.currentSong
        binding.result = songEntity
        mediaPlayer.setDataSource(songEntity?.source)
        mediaPlayer.prepareAsync() // Asynchronous preparation
        _musicState.value = _musicState.value?.copy(status = MusicState.MusicStatus.PREPARING)
        mediaPlayer.setOnPreparedListener { player ->
            // Start playing when the media is prepared
            player.start()
            _musicState.value = _musicState.value?.copy(status = MusicState.MusicStatus.PLAYING)
            binding.seekBar.progress = 0
            binding.seekBar.max = mediaPlayer.duration
            binding.ibPlay.setImageResource(R.drawable.ic_pause)
            PlayerUtil().setSeekbar(binding.seekBar, mediaPlayer)
            binding.ibPlay.setOnClickListener {
                if (_musicState.value?.status != MusicState.MusicStatus.PREPARING) {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        _musicState.value =
                            _musicState.value?.copy(status = MusicState.MusicStatus.PAUSED)
                        binding.ibPlay.setImageResource(R.drawable.ic_play)
                    } else if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                        _musicState.value =
                            _musicState.value?.copy(status = MusicState.MusicStatus.PLAYING)
                        binding.ibPlay.setImageResource(R.drawable.ic_pause)
                    }
                }
            }
            //setting handlers
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val currentPos = msg.what
                    binding.seekBar.progress = currentPos
                    val elapsedTime = PlayerUtil().calculateTime(currentPos)
                    binding.tvDurationStart.text = elapsedTime
                    if (elapsedTime == "0:30") {
                        _musicState.postValue(
                            _musicState.value?.copy(
                                status = MusicState.MusicStatus.StartTracking
                            )
                        )
                    }
                    super.handleMessage(msg)
                }
            }
            Thread(Runnable {
                while (true) {
                    try {
                        val msg = Message()
                        msg.what = mediaPlayer.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1000)
                    } catch (ex: Exception) {
                        Log.d(LogTag.PLAYER, "$ex")
                    }
                }
            }).start()

        }
    }

    private fun playNextSong(
        isNext: Boolean, binding: ActivityMusicBinding, mediaPlayer: MediaPlayer
    ) {
        var canPlayNext = false
        var canPlayPrevious = false
        //find current song index
        val currSong = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == _musicState.value?.currentSong?.id
        }
        val currIdx = _musicState.value?.currentPlaylist?.indexOf(currSong)
        val song = if (isNext) {
            if (currIdx == _musicState.value?.totalSongs!! - 1) {
                return
            } else {
                canPlayNext = true
                _musicState.value?.currentPlaylist?.elementAt(currIdx!! + 1)
            }
        } else {
            if (currIdx == 0) {
                return
            } else {
                canPlayPrevious = true
                _musicState.value?.currentPlaylist?.elementAt(currIdx!! - 1)
            }
        }
        _musicState.value = _musicState.value?.copy(
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
        if (canPlayNext) {
            resetMedia(binding, mediaPlayer)
            playSong(binding, mediaPlayer)
        }
        if (canPlayPrevious) {
            resetMedia(binding, mediaPlayer)
            playSong(binding, mediaPlayer)
        }
    }

    private fun setNextSong() {
        //find current song index
        val currSong = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == _musicState.value?.currentSong?.id
        }
        val currIdx = _musicState.value?.currentPlaylist?.indexOf(currSong)
        val song = if (currIdx == _musicState.value?.totalSongs!! - 1) {
            return
        } else {
            _musicState.value?.currentPlaylist?.elementAt(currIdx!! + 1)
        }
        _musicState.value = _musicState.value?.copy(
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
    }

    private fun setRandomSong() {
        //find random index between 0 and size of playlist-1(BOTH INCLUDED)
        val randomIdx = Random.nextInt(0, _musicState.value?.currentPlaylist?.size!! - 1)
        val song = _musicState.value?.currentPlaylist?.elementAt(randomIdx)
        _musicState.value = _musicState.value?.copy(
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
    }

    private fun resetMedia(binding: ActivityMusicBinding, mediaPlayer: MediaPlayer) {
        mediaPlayer.reset()
        binding.seekBar.progress = 0
        binding.ibPlay.setImageResource(R.drawable.ic_play)
    }
}