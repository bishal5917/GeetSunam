package com.example.geetsunam.features.presentation.single_artist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityArtistBinding

class ArtistActivity : AppCompatActivity() {
    //getting argument (artist)
    private val args by navArgs<ArtistActivityArgs>()

    private lateinit var binding: ActivityArtistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        binding.artist = args.artist
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.tbArtistActivity)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //going back to previous screen when back arrow is pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}