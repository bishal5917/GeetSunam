package com.example.geetsunam.features.presentation.music.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.geetsunam.R
import com.example.geetsunam.database.entities.OfflineSong
import com.example.geetsunam.database.entities.Trending
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.downloader.MusicDownloader
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.features.domain.usecases.SaveSongOfflineUsecase
import com.example.geetsunam.utils.LogUtil
import com.example.geetsunam.utils.PlayerUtil
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val player: ExoPlayer,
    private val application: Application,
    private val saveSongOfflineUsecase: SaveSongOfflineUsecase
) : AndroidViewModel(application) {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    //DownloadManager and BroadcastReceiver
    private val downloadManager =
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private var downloadId: Long = -1L
    private var receiver: BroadcastReceiver? = null

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

            is MusicEvent.DownloadSong -> {
                if (player.isPlaying) {
                    player.pause()
                }
                downloadSong(event.context)
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
                if (playlistName == "offline") {
                    items.add(MediaItem.fromUri(song?.filePath!!))
                } else {
                    items.add(MediaItem.fromUri(song?.source!!))
                }
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
                    }

                    Player.STATE_READY -> {
                    }

                    Player.STATE_BUFFERING -> {
                    }

                    Player.STATE_ENDED -> {
                    }

                    else -> {
                        // Other things
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO || reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK) {
                    val currentPosition = player.currentMediaItemIndex
                    setSongByIdx(currentPosition)
                    binding.result = _musicState.value?.currentSong
                }
                super.onMediaItemTransition(mediaItem, reason)
            }

            override fun onPlayerError(error: PlaybackException) {
                if (error.cause is IOException) {
                    // Handle the case where the file was not found
                    _musicState.value = _musicState.value?.copy(
                        status = MusicState.MusicStatus.Failed, message = "File not found"
                    )
                } else {
                    // Handle other types of exceptions
                    _musicState.value = _musicState.value?.copy(
                        status = MusicState.MusicStatus.Failed, message = error.message ?: ""
                    )
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

    // ********** DOWNLOADING SONG **********
    private fun downloadSong(context: Context) {
        val downloader = MusicDownloader(context)
        val song = _musicState.value?.currentSong?.songName
        val artist = _musicState.value?.currentSong?.artistName
        val fileName = "$artist - $song"
        downloadId = downloader.downloadFile(
            _musicState.value?.currentSong?.source ?: "", fileName
        )
        _musicState.value = _musicState.value?.copy(
            status = MusicState.MusicStatus.Downloading
        )
        registerReceiver()
    }

    private fun registerReceiver() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                    if (id == downloadId) {
                        val query = DownloadManager.Query().setFilterById(downloadId)
                        val cursor = downloadManager.query(query)
                        if (cursor.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            if (columnIndex != -1) {
                                when (cursor.getInt(columnIndex)) {
                                    DownloadManager.STATUS_SUCCESSFUL -> {
                                        //Saving song to db
                                        val fileUriIndex =
                                            cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                                        val fileUri = cursor.getString(fileUriIndex)
                                        val filePath = Uri.parse(fileUri).path
//                                        val fileData =
//                                            PlayerUtil().getFileAsByteArray(filePath ?: "")
                                        if (filePath != null) {
                                            saveSongOffline(filePath)
                                        } else {
                                            LogUtil.log("Filepath is null!!!")
                                        }
                                        //updating the livedata
                                        _musicState.value = _musicState.value?.copy(
                                            status = MusicState.MusicStatus.Downloaded,
                                            message = "Song downloaded successfully"
                                        )
                                    }

                                    DownloadManager.STATUS_FAILED -> {
                                        _musicState.value = _musicState.value?.copy(
                                            status = MusicState.MusicStatus.Failed,
                                            message = "Download failed"
                                        )
                                    }
                                }
                            }
                        }
                        cursor.close()
                    }
//                    if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
//                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
//                        if (id != -1L) {
//                            _musicState.value = _musicState.value?.copy(
//                                status = MusicState.MusicStatus.Downloaded,
//                                message = "Song downloaded."
//                            )
//                            LogUtil.log("$id DOWNLOADED !!!")
//                        }
//                    }
                } catch (ex: Exception) {
                    _musicState.value = _musicState.value?.copy(
                        status = MusicState.MusicStatus.Failed, message = "Download failed"
                    )
                }
            }
        }
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        getApplication<Application>().registerReceiver(receiver, filter)
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

    private fun saveSongOffline(filePath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val song = _musicState.value?.currentSong
                val offlineSong = OfflineSong(
                    id = song?.id!!,
                    coverArt = song.coverArt,
                    artistName = song.artistName,
                    songName = song.songName,
                    duration = song.duration,
                    source = song.source,
                    stream = song.stream,
                    isFavourite = song.isFavourite,
                    filePath = filePath,
                    fileData = null
                )
                saveSongOfflineUsecase.call(offlineSong)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        player.release()
        receiver?.let {
            getApplication<Application>().unregisterReceiver(it)
        }
        super.onCleared()
    }
}