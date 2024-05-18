package com.example.geetsunam.features.presentation.single_artist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityArtistBinding
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.single_artist.adapters.ArtistSongsAdapter
import com.example.geetsunam.features.presentation.single_artist.viewmodel.ArtistSongEvent
import com.example.geetsunam.features.presentation.single_artist.viewmodel.ArtistSongState
import com.example.geetsunam.features.presentation.single_artist.viewmodel.ArtistSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtistActivity : AppCompatActivity() {
    //getting argument (artist)
    private val args by navArgs<ArtistActivityArgs>()

    private lateinit var binding: ActivityArtistBinding

    private val artistSongsAdapter by lazy { ArtistSongsAdapter() }

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var artistSongViewModel: ArtistSongViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
        binding.artist = args.artist
        setContentView(binding.root)
        val toolbar = binding.tbArtistActivity
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        requestGenreSongs()
    }

    private fun setupRecyclerView() {
        binding.rvArtistSongs.adapter = artistSongsAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvArtistSongs.layoutManager = layoutManager
    }

    private fun requestGenreSongs() {
        artistSongViewModel.onEvent(
            ArtistSongEvent.GetArtistSongs(
                CommonRequestModel(
                    token = splashViewModel.splashState.value?.userEntity?.token ?: "",
                    artistId = args.artist.id,
                ),
            ),
        )
        artistSongViewModel.artistSongsState.observe(this) { response ->
            if (response.status == ArtistSongState.ArtistSongStatus.LOADING) {
                binding.rvArtistSongs.visibility = View.GONE
                binding.shlArtistSongs.visibility = View.VISIBLE
            }
            if (response.status == ArtistSongState.ArtistSongStatus.SUCCESS) {
                response.songs?.let {
                    artistSongsAdapter.setData(
                        response.songs.songs as List<Song>
                    )
                }
                binding.rvArtistSongs.visibility = View.VISIBLE
                binding.shlArtistSongs.visibility = View.GONE
                binding.tvArtistSongsQty.text = "${response.songs?.songs?.size.toString()} Songs"
                musicViewModel.onEvent(MusicEvent.SetMediaItems(response.songs?.songs!!, "artists"))
            }
            if (response.status == ArtistSongState.ArtistSongStatus.FAILED) {
                binding.rvArtistSongs.visibility = View.GONE
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