package com.example.geetsunam.features.presentation.music

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityMusicBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicActivity : AppCompatActivity() {

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    //getting argument (song)
    private val args by navArgs<MusicActivityArgs>()

    private lateinit var binding: ActivityMusicBinding

    //handler for seekBar
    lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize and set up the MediaPlayer
//        mediaPlayer = MediaPlayer()
//        mediaPlayer?.setAudioAttributes(
//            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA).build()
//        )
        playMusic()
    }

    private fun playMusic() {
        binding.result = args.song
        val response = args.song
        mediaPlayer?.setDataSource(response.source)
        mediaPlayer?.prepareAsync() // Asynchronous preparation
        mediaPlayer?.setOnPreparedListener { player ->
            // Start playing when the media is prepared
            player.start()
            binding.seekBar.progress = 0
            binding.seekBar.max = mediaPlayer!!.duration
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
//            Handler(Looper.getMainLooper()).postDelayed({
//                runnable = Runnable {
//                    binding.seekBar.progress = mediaPlayer!!.currentPosition
//                }
//            }, 3000)
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

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}