package com.example.geetsunam.features.presentation.music

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.core.configs.ApiConfig
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicState
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    //getting argument (id)
    private val args by navArgs<MusicActivityArgs>()

    private lateinit var binding: ActivityMusicBinding

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    //handler for seekBar
    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        binding.result = Song(
            duration = "--:--",
            coverArt = ApiConfig.defaultCoverArt,
            title = "...............",
            artists = Artist(
                fullname = "....."
            )
        )
        requestApi()
        setContentView(binding.root)
        // Initialize and set up the MediaPlayer
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build()
        )
    }

    private fun requestApi() {
        musicViewModel.onEvent(
            MusicEvent.GetMusic(
                splashViewModel.userIdFlow.value, args.id
            )
        )
        musicViewModel.musicState.observe(this) { response ->
            if (response.status == MusicState.MusicStatus.LOADING) {
            }
            if (response.status == MusicState.MusicStatus.SUCCESS) {
                //play music
                mediaPlayer?.setDataSource(response.music?.source)
                mediaPlayer?.prepareAsync() // Asynchronous preparation
                mediaPlayer?.setOnPreparedListener { player ->
                    // Start playing when the media is prepared
                    player.start()
                    binding.seekBar.progress = 0
                    binding.seekBar.max = mediaPlayer!!.duration
                    binding.seekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                            if (p2) {
                                mediaPlayer!!.seekTo(p1)
                            }
                        }

                        override fun onStartTrackingTouch(p0: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(p0: SeekBar?) {
                        }
                    })
                    Handler(Looper.getMainLooper()).postDelayed({
                        runnable = Runnable {
                            binding.seekBar.progress = mediaPlayer!!.currentPosition
                        }
                    }, 3000)

                    binding.result = response.music
                    binding.ibPlay.setImageResource(R.drawable.ic_pause)
                    binding.ibPlay.setOnClickListener {
                        if (mediaPlayer!!.isPlaying) {
                            mediaPlayer!!.pause()
                            binding.ibPlay.setImageResource(R.drawable.ic_play)
                        } else if (!mediaPlayer!!.isPlaying) {
                            mediaPlayer!!.start()
                            binding.ibPlay.setImageResource(R.drawable.ic_pause)
                        }
                    }
                }
            }
            if (response.status == MusicState.MusicStatus.FAILED) {
                CustomToast.showToast(context = this, "${response.message}")
            }
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}