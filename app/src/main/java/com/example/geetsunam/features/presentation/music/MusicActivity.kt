package com.example.geetsunam.features.presentation.music

import android.app.Dialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavEvent
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavState
import com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel.ToggleFavViewModel
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongEvent
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongState
import com.example.geetsunam.features.presentation.music.track.viewmodel.TrackSongViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicState
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// THIS ACTIVITY PLAYS MUSIC FILE USING MEDIAPLAYER CLASS OF ANDROID
@AndroidEntryPoint
class MusicActivity : AppCompatActivity() {

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var toggleFavViewModel: ToggleFavViewModel

    @Inject
    lateinit var trackSongViewModel: TrackSongViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    //getting argument (song)
//    private val args by navArgs<MusicActivityArgs>()

    private lateinit var binding: ActivityMusicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set current song and play it
//        musicViewModel.onEvent(
//            MusicEvent.SetAndPlayCurrent(
//                args.song.id!!, binding, mediaPlayer
//            )
//        )
        setPlayModeDrawables()
        addToFavourite()
        trackSong()
//        observeSongTracking()
        binding.ibPlayMode.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.ChangePlayMode)
        }
        binding.ibPlayNext.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.PlayNextSong(binding, mediaPlayer))
        }
        binding.ibPlayPrevious.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.PlayPreviousSong(binding, mediaPlayer))
        }
        mediaPlayer.setOnCompletionListener {
            musicViewModel.onEvent(MusicEvent.PlayAnother(binding, mediaPlayer))
        }
        binding.ibShuffle.setOnClickListener {
            musicViewModel.onEvent(MusicEvent.Shuffle(binding, mediaPlayer))
        }
    }

    private fun setPlayModeDrawables() {
        musicViewModel.musicState.observe(this) { response ->
            if (response.playMode == MusicState.PlayMode.Serial) {
                binding.ibPlayMode.setImageResource(R.drawable.ic_repeat)
            }
            if (response.playMode == MusicState.PlayMode.Random) {
                binding.ibPlayMode.setImageResource(R.drawable.ic_shuffle)
            }
            if (response.playMode == MusicState.PlayMode.LoopCurrent) {
                binding.ibPlayMode.setImageResource(R.drawable.ic_repeat_one)
            }
        }
    }

    private fun addToFavourite() {
        binding.ibLike.setOnClickListener {
//            toggleFavViewModel.onEvent(
//                ToggleFavEvent.AddFavourite(
//                    CommonRequestModel(splashViewModel.userFlow.value?.token, args.song.id)
//                )
//            )
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

    private fun trackSong() {
        musicViewModel.musicState.observe(this) { response ->
            if (response.status == MusicState.MusicStatus.StartTracking) {
                //call api to track song played
                trackSongViewModel.onEvent(
                    TrackSongEvent.TrackCurrentSong(
                        CommonRequestModel(
                            token = splashViewModel.userFlow.value?.token.toString(),
                            songId = response.currentSong?.id
                        )
                    )
                )
            }
        }
    }

    private fun observeSongTracking() {
        //observing (FOR TESTING ONLY,COMMENT THIS IN PRODUCTION)
        trackSongViewModel.trackSongState.observe(this) { response ->
            if (response.status == TrackSongState.TrackSongStatus.TRACKING) {
                CustomToast.showToast(context = this, "${response.message}")
            }
            if (response.status == TrackSongState.TrackSongStatus.TRACKED) {
                CustomToast.showToast(context = this, "${response.message}")
            }
            if (response.status == TrackSongState.TrackSongStatus.FAILED) {
                CustomToast.showToast(context = this, "${response.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        musicViewModel.onEvent(MusicEvent.Reset)
    }
}