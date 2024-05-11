package com.example.geetsunam.features.presentation.music.viewmodel

import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MusicViewModel @Inject constructor(private val player: ExoPlayer) : ViewModel() {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.SetMediaItems -> {
                //time to set mediaItems
                setMediaItems(event.playlist, event.playlistName)
            }

            is MusicEvent.SetAndPlayCurrent -> {
                setPlayModeDrawables(event.binding)
                setCurrentSongById(event.songId)
                playSong(event.binding)
            }

            is MusicEvent.ChangeRepeatMode -> {
                if (player.repeatMode == Player.REPEAT_MODE_OFF) {
                    player.repeatMode = Player.REPEAT_MODE_ONE
                    event.binding.ibRepeatMode.setImageResource(R.drawable.ic_repeat_one)
                } else if (player.repeatMode == Player.REPEAT_MODE_ONE) {
                    player.repeatMode = Player.REPEAT_MODE_OFF
                    event.binding.ibRepeatMode.setImageResource(R.drawable.ic_repeat)
                }
            }

            is MusicEvent.ChangeShuffleMode -> {
                if (player.shuffleModeEnabled) {
                    player.shuffleModeEnabled = false
                    event.binding.ibShuffle.setImageResource(R.drawable.ic_shuffle_off)
                } else {
                    player.shuffleModeEnabled = true
                    event.binding.ibShuffle.setImageResource(R.drawable.ic_shuffle)
                }
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

            is MusicEvent.Reset -> {
                releasePlayer()
                _musicState.postValue(
                    _musicState.value?.copy(
                        status = MusicState.MusicStatus.IDLE
                    )
                )
            }

            else -> {}
        }
    }

    private fun setMediaItems(playlist: List<Song?>?, playlistName: String) {
        val items: ArrayList<MediaItem> = ArrayList()
        //Now loop through songs and add all
        if (playlist != null) {
            for (song: Song? in playlist) {
                items.add(MediaItem.fromUri(song?.source!!))
            }
        }
        //Now setting the mediaItems
        player.setMediaItems(items, false)
        _musicState.postValue(
            _musicState.value?.copy(
                playlistName = playlistName,
                currentPlaylist = playlist,
                totalSongs = playlist?.size,
                medias = items
            )
        )
    }

    private fun setCurrentSongById(songId: String) {
        val song = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == songId
        }
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

    private fun setSongByIdx(idx: Int) {
        val song = _musicState.value?.currentPlaylist?.get(idx)
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

    private fun setPlayModeDrawables(binding: ActivityMusicPlayerBinding) {
        //Checking for shuffle
        if (player.shuffleModeEnabled) {
            binding.ibShuffle.setImageResource(R.drawable.ic_shuffle)
        } else {
            binding.ibShuffle.setImageResource(R.drawable.ic_shuffle_off)
        }
        //Checking for repeat mode
        if (player.repeatMode == Player.REPEAT_MODE_OFF) {
            binding.ibRepeatMode.setImageResource(R.drawable.ic_repeat)
        } else if (player.repeatMode == Player.REPEAT_MODE_ONE) {
            binding.ibRepeatMode.setImageResource(R.drawable.ic_repeat_one)
        }
    }

    @OptIn(UnstableApi::class)
    private fun playSong(binding: ActivityMusicPlayerBinding) {
        //find current song index
        val currSong = _musicState.value?.currentPlaylist?.find { song ->
            song?.id == _musicState.value?.currentSong?.id
        }
        val currIdx = _musicState.value?.currentPlaylist?.indexOf(currSong)
        binding.pvSong.player = player
        player.prepare()
        player.seekTo(currIdx!!, 0)
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {
                when (state) {
                    Player.STATE_IDLE -> {
                        // The player is idle, meaning it holds only limited resources.The player must be prepared before it will play the media.
                    }

                    Player.STATE_READY -> {
                        player.play()
                        val currentPosition = player.currentMediaItemIndex
                        setSongByIdx(currentPosition)
                        binding.result = _musicState.value?.currentSong
                        // The player is able to immediately play from its current position. The player will be playing if getPlayWhenReady() is true, and paused otherwise.
                    }

                    Player.STATE_BUFFERING -> {
                        val currentPosition = player.currentMediaItemIndex
                        setSongByIdx(currentPosition)
                        binding.result = _musicState.value?.currentSong
                        // The player is not able to immediately play the media, but is doing work toward being able to do so. This state typically occurs when the player needs to buffer more data before playback can start.
                    }

                    Player.STATE_ENDED -> {
                        // The player has finished playing the media.
                        val nextMediaIdx = player.nextMediaItemIndex
                        setSongByIdx(nextMediaIdx)
                        binding.result = _musicState.value?.currentSong
                    }

                    else -> {
                        // Other things
                    }
                }
            }
        })
//        binding.result = songEntity
//        mediaPlayer.setDataSource(songEntity?.source)
//        mediaPlayer.prepareAsync() // Asynchronous preparation
//        _musicState.value = _musicState.value?.copy(status = MusicState.MusicStatus.PREPARING)
//        mediaPlayer.setOnPreparedListener { player ->
//            // Start playing when the media is prepared
//            player.start()
//            _musicState.value = _musicState.value?.copy(status = MusicState.MusicStatus.PLAYING)
//            binding.seekBar.progress = 0
//            binding.seekBar.max = mediaPlayer.duration
//            binding.ibPlay.setImageResource(R.drawable.ic_pause)
//            PlayerUtil().setSeekbar(binding.seekBar, mediaPlayer)
//            binding.ibPlay.setOnClickListener {
//                if (_musicState.value?.status != MusicState.MusicStatus.PREPARING) {
//                    if (mediaPlayer.isPlaying) {
//                        mediaPlayer.pause()
//                        _musicState.value =
//                            _musicState.value?.copy(status = MusicState.MusicStatus.PAUSED)
//                        binding.ibPlay.setImageResource(R.drawable.ic_play)
//                    } else if (!mediaPlayer.isPlaying) {
//                        mediaPlayer.start()
//                        _musicState.value =
//                            _musicState.value?.copy(status = MusicState.MusicStatus.PLAYING)
//                        binding.ibPlay.setImageResource(R.drawable.ic_pause)
//                    }
//                }
//            }
//            //setting handlers
//            val handler = object : Handler() {
//                override fun handleMessage(msg: Message) {
//                    val currentPos = msg.what
//                    binding.seekBar.progress = currentPos
//                    val elapsedTime = PlayerUtil().calculateTime(currentPos)
//                    binding.tvDurationStart.text = elapsedTime
//                    if (elapsedTime == "0:30") {
//                        _musicState.postValue(
//                            _musicState.value?.copy(
//                                status = MusicState.MusicStatus.StartTracking
//                            )
//                        )
//                    }
//                    super.handleMessage(msg)
//                }
//            }
//            Thread(Runnable {
//                while (true) {
//                    try {
//                        val msg = Message()
//                        msg.what = mediaPlayer.currentPosition
//                        handler.sendMessage(msg)
//                        Thread.sleep(1000)
//                    } catch (ex: Exception) {
//                        Log.d(LogTag.PLAYER, "$ex")
//                    }
//                }
//            }).start()
//
//        }
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
//            playSong(binding, mediaPlayer)
        }
        if (canPlayPrevious) {
            resetMedia(binding, mediaPlayer)
//            playSong(binding, mediaPlayer)
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

    private fun releasePlayer() {
        player.stop()
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}