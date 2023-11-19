package com.example.geetsunam.features.presentation.music

import android.app.Dialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.features.domain.entities.SongEntity
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavEvent
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavState
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.PlayerUtil
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
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
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.reset()
            musicViewModel.onEvent(MusicEvent.PlayNextSong)
            binding.seekBar.progress = 0
            songEntity = musicViewModel.musicState.value?.currentSong!!
            binding.ibPlay.setImageResource(R.drawable.ic_play)
            playMusic()
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
        mediaPlayer.reset()
        binding.seekBar.progress = 0
        songEntity = musicViewModel.musicState.value?.currentSong!!
        binding.ibPlay.setImageResource(R.drawable.ic_play)
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
            binding.ibPlay.setImageResource(R.drawable.ic_pause)
            PlayerUtil().setSeekbar(binding.seekBar, mediaPlayer)
            binding.ibPlay.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    binding.ibPlay.setImageResource(R.drawable.ic_play)

                } else if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    binding.ibPlay.setImageResource(R.drawable.ic_pause)
                }
            }
            //setting handlers
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val currentPos = msg.what
                    if (currentPos == 30000) {
                        //call api to track song played
                    }
                    binding.seekBar.progress = currentPos
                    val elapsedTime = PlayerUtil().calculateTime(currentPos)
                    binding.tvDurationStart.text = elapsedTime
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
                        Log.d(LogTag.PLAYER, "${ex.message}")
                    }
                }

            }).start()
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}