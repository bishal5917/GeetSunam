package com.example.geetsunam.features.presentation.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.example.geetsunam.R

class MusicActivity : AppCompatActivity() {

    //getting argument (id)
    private val args by navArgs<MusicActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
    }
}