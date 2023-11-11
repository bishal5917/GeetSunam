package com.example.geetsunam.features.presentation.single_genre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityGenreBinding
import com.example.geetsunam.features.presentation.single_genre.adapters.GenreSongsAdapter
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongEvent
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongState
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenreActivity : AppCompatActivity() {

    //getting argument (genre)
    private val args by navArgs<GenreActivityArgs>()

    private lateinit var binding: ActivityGenreBinding

    private val genreSongsAdapter by lazy { GenreSongsAdapter() }

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var genreSongViewModel: GenreSongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenreBinding.inflate(layoutInflater)
        binding.genre = args.genre
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.tbGenreActivity)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        requestGenreSongs()
    }

    private fun setupRecyclerView() {
        binding.rvGenreSongs.adapter = genreSongsAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvGenreSongs.layoutManager = layoutManager
    }

    private fun requestGenreSongs() {
        genreSongViewModel.onEvent(
            GenreSongEvent.GetGenreSongs(
                CommonRequestModel(
                    token = splashViewModel.userFlow.value?.token,
                    genreId = args.genre.id,
                ),
            ),
        )
        genreSongViewModel.genreSongsState.observe(this) { response ->
            if (response.status == GenreSongState.GenreSongStatus.LOADING) {
                binding.rvGenreSongs.visibility = View.GONE
                binding.shlGenreSongs.visibility = View.VISIBLE
            }
            if (response.status == GenreSongState.GenreSongStatus.SUCCESS) {
                response.songs?.let {
                    genreSongsAdapter.setData(
                        response.songs.songs as List<Song>
                    )
                }
                binding.rvGenreSongs.visibility = View.VISIBLE
                binding.shlGenreSongs.visibility = View.GONE
                binding.tvGenreSongsQty.text = "${response.songs?.songs?.size.toString()} Songs"
            }
            if (response.status == GenreSongState.GenreSongStatus.FAILED) {
                binding.rvGenreSongs.visibility = View.GONE
                CustomToast.showToast(context = this, "${response.message}")
            }
        }
    }

    //going back to previous screen when back arrow is pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}