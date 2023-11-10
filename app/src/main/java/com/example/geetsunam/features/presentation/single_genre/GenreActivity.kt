package com.example.geetsunam.features.presentation.single_genre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.navArgs
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityGenreBinding

class GenreActivity : AppCompatActivity() {

    //getting argument (genre)
    private val args by navArgs<GenreActivityArgs>()

    private lateinit var binding: ActivityGenreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        binding.genre = args.genre
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.tbGenreActivity)
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