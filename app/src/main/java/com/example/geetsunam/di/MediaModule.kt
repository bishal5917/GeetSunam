package com.example.geetsunam.di

import android.content.Context
import dagger.Module
import dagger.Provides
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    //    private var mediaPlayer: MediaPlayer? = null
    private var exoplayer: ExoPlayer? = null

//    @Provides
//    fun providesMediaPlayer(): MediaPlayer {
//        mediaPlayer = MediaPlayer()
//        mediaPlayer?.setAudioAttributes(
//            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA).build()
//        )
//        return mediaPlayer as MediaPlayer
//    }

    @Provides
    @Singleton
    fun providesExoplayer(context: Context): ExoPlayer {
        exoplayer = ExoPlayer.Builder(context).build()
        exoplayer!!.playWhenReady = true
        return exoplayer as ExoPlayer
    }
}