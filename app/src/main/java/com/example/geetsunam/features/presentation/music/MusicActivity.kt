package com.example.geetsunam.features.presentation.music

import android.app.Dialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavEvent
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavState
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicState
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : AppCompatActivity() {

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var toggleFavViewModel: ToggleFavViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    //getting argument (song)
    private val args by navArgs<MusicActivityArgs>()

    private lateinit var binding: ActivityMusicBinding

    private lateinit var songEntity: SongEntity

    //handler for seekBar
    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set current song
        musicViewModel.onEvent(MusicEvent.SetCurrentSong(args.song.id!!))

        songEntity = args.song
        playMusic()
        addToFavourite()
        binding.ibPlayNext.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.PlayNextSong)
            resetAndPlay()
        }
        binding.ibPlayPrevious.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.PlayPreviousSong)
            resetAndPlay()
        }
    }

    private fun addToFavourite() {
        val favBtn = binding.ibLike
        favBtn.setOnClickListener {
            toggleFavViewModel.onEvent(
                ToggleFavEvent.AddFavourite(
                    CommonRequestModel(splashViewModel.userFlow.value?.token, args.song.id)
                )
            )
        }
        //observing
        val dialog = Dialog(this)
        toggleFavViewModel.toggleFavState.observe(this) { response ->
            if (response.status == ToggleFavState.ToggleFavStatus.LOADING) {
                //show loading dialog
                CustomDialog().showLoadingDialog(dialog)
            }
            if (response.status == ToggleFavState.ToggleFavStatus.SUCCESS) {
                CustomDialog().hideLoadingDialog(dialog)
                binding.ibLike.setImageResource(response.drawableId!!)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
            if (response.status == ToggleFavState.ToggleFavStatus.FAILED) {
                CustomDialog().hideLoadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
        }
    }

    private fun resetAndPlay() {
        songEntity = musicViewModel.musicState.value?.currentSong!!
        mediaPlayer.reset()
        binding.ibPlay.setImageResource(R.drawable.ic_play)
        binding.seekBar.progress = 0
        playMusic()
    }

    private fun playMusic() {
        binding.result = songEntity
        mediaPlayer.setDataSource(songEntity.source)
        mediaPlayer.prepareAsync() // Asynchronous preparation
        mediaPlayer.setOnPreparedListener { player ->
            // Start playing when the media is prepared
            player.start()
            binding.seekBar.progress = 0
            binding.seekBar.max = mediaPlayer.duration
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) {
                        mediaPlayer.seekTo(p1)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            })
//            Handler(Looper.getMainLooper()).postDelayed({
//                runnable = Runnable {
//                    binding.seekBar.progress = mediaPlayer!!.currentPosition
//                }
//            }, 3000)
            binding.ibPlay.setImageResource(R.drawable.ic_pause)
            binding.ibPlay.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    binding.ibPlay.setImageResource(R.drawable.ic_play)

                } else if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    binding.ibPlay.setImageResource(R.drawable.ic_pause)
                }
            }
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}