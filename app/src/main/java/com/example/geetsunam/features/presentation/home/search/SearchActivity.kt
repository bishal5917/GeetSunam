package com.example.geetsunam.features.presentation.home.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivitySearchBinding
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.tbSearchActivity
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