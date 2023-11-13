package com.example.geetsunam.utils

import android.media.MediaPlayer
import android.widget.SeekBar

class PlayerUtil {
    fun setSeekbar(seekbar: SeekBar, mediaPlayer: MediaPlayer) {
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
    }

    fun calculateTime(time: Int): String {
        var timeLabel = ""
        val timeInSeconds = time / 1000
        val min = timeInSeconds / 60
        val sec = timeInSeconds % 60
        timeLabel = "$min"
        timeLabel += if (sec < 10) {
            ":0$sec"
        } else {
            ":$sec"
        }
        return timeLabel
    }
}