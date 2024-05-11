package com.example.geetsunam.features.presentation.music.viewmodel

import android.media.MediaPlayer
import androidx.media3.exoplayer.ExoPlayer
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.utils.models.Song

sealed class MusicEvent {
    data class SetMediaItems(val playlist: List<Song?>?, val playlistName: String) : MusicEvent()

    data class SetAndPlayCurrent(
        val songId: String, val binding: ActivityMusicPlayerBinding
    ) : MusicEvent()

    data class PlayNextSong(val binding: ActivityMusicBinding, val mediaPlayer: MediaPlayer) :
        MusicEvent()

    data class PlayAnother(val binding: ActivityMusicBinding, val mediaPlayer: MediaPlayer) :
        MusicEvent()

    data class PlayPreviousSong(val binding: ActivityMusicBinding, val mediaPlayer: MediaPlayer) :
        MusicEvent()

    data class Shuffle(val binding: ActivityMusicBinding, val mediaPlayer: MediaPlayer) :
        MusicEvent()

    object ChangePlayMode : MusicEvent()

    object Reset : MusicEvent()
}