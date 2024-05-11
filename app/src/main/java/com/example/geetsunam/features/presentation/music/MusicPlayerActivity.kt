package com.example.geetsunam.features.presentation.music

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.navigation.navArgs
import com.example.geetsunam.databinding.ActivityMusicPlayerBinding
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavEvent
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavState
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
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

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.result = args.song
        addToFavourite()
        setShuffleAndLoopMode()
        //set current song and play it
        musicViewModel.onEvent(
            MusicEvent.SetAndPlayCurrent(
                args.song.id!!, binding
            )
        )
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

    override fun onDestroy() {
        musicViewModel.onEvent(MusicEvent.Reset)
        super.onDestroy()
    }
}