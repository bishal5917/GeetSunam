package com.example.geetsunam.features.presentation.music

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.navigation.navArgs
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongEvent
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongViewModel
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavEvent
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavState
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicState
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongEvent
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.service.Actions
import com.example.geetsunam.service.SongTrackService
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.Network
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// THIS ACTIVITY PLAYS MUSIC FILE USING EXOPLAYER
@AndroidEntryPoint
class MusicPlayerActivity : AppCompatActivity() {

    //getting argument (song)
    private val args by navArgs<MusicPlayerActivityArgs>()

    private lateinit var binding: ActivityMusicPlayerBinding

    @Inject
    lateinit var musicViewModel: MusicViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var toggleFavViewModel: ToggleFavViewModel

    @Inject
    lateinit var offlineSongViewModel: OfflineSongViewModel

    @Inject
    lateinit var favSongViewModel: FavSongViewModel

    private lateinit var token: String

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = splashViewModel.splashState.value?.userEntity?.token ?: ""
        binding.result = args.song
        addToFavourite()
        setShuffleAndLoopMode()
        downloadSong()
        observeSongStatus()
        //set current song and play it
        musicViewModel.onEvent(
            MusicEvent.SetAndPlayCurrent(
                args.song.id!!, binding
            )
        )
        //Start song tracking service
        configureService(Actions.START)
    }

    private fun configureService(action: Actions) {
        Intent(this, SongTrackService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }
            startService(it)
        }
    }

    private fun downloadSong() {
        binding.ibDownload.setOnClickListener {
            if (Network.hasInternetConnection(this)) {
                CustomDialog().showSureDownloadDialog(this) {
                    musicViewModel.onEvent(MusicEvent.DownloadSong(this))
                }
            } else {
                CustomToast.showToast(this, Constants.noInternet)
            }
        }
    }

    private fun observeSongStatus() {
        val dialog = Dialog(this)
        musicViewModel.musicState.observe(this) { response ->
            if (response.status == MusicState.MusicStatus.Downloading) {
                //show loading dialog
                CustomDialog().showDownloadingDialog(dialog)
            }
            if (response.status == MusicState.MusicStatus.Downloaded) {
                CustomDialog().hideDownloadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
                offlineSongViewModel.onEvent(OfflineSongEvent.GetOfflineSongs)
            }
            if (response.status == MusicState.MusicStatus.Failed) {
                CustomDialog().hideDownloadingDialog(dialog)
                CustomToast.showToast(
                    context = this, "${
                        response.message
                    }"
                )
            }
            if (response.status == MusicState.MusicStatus.Changed) {
                //Start song tracking service for next song also
                configureService(Actions.START)
            }
        }
    }

    private fun setShuffleAndLoopMode() {
        binding.ibShuffle.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.ChangeShuffleMode(binding))
        }
        binding.ibRepeatMode.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.ChangeRepeatMode(binding))
        }
    }

    private fun addToFavourite() {
        binding.ibLike.setOnClickListener {
            toggleFavViewModel.onEvent(
                ToggleFavEvent.AddFavourite(
                    CommonRequestModel(token, args.song.id)
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
                favSongViewModel.onEvent(FavSongEvent.GetFavouriteSongs(token))
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

    override fun onDestroy() {
        musicViewModel.onEvent(MusicEvent.Reset)
        configureService(Actions.STOP)
        super.onDestroy()
    }
}