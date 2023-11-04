package com.example.geetsunam.di

import android.media.AudioAttributes
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    private var mediaPlayer: MediaPlayer? = null

    @Provides
    fun providesMediaPlayer(): MediaPlayer {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build()
        )
        return mediaPlayer as MediaPlayer
    }
}