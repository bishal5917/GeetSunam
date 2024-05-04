package com.example.geetsunam.features.presentation.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerControlView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// THIS ACTIVITY PLAYS MUSIC FILE USING EXOPLAYER
@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    //getting argument (song)
    private val args by navArgs<MusicPlayerActivityArgs>()

    private lateinit var binding: ActivityMusicPlayerBinding

    private lateinit var player: ExoPlayer

    private lateinit var playerControlView: PlayerControlView

    @Inject
    lateinit var musicViewModel: MusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.result = args.song
        initPlayer()

        binding.pvSong.player = player
        val mediaItem = MediaItem.fromUri(args.song.source!!)
        playerControlView = findViewById<PlayerControlView>(R.id.player_control_view)
        playerControlView.player = player
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun initPlayer() {
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}