package com.example.geetsunam.features.presentation.home.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivitySearchBinding
import com.example.geetsunam.features.presentation.home.search.adapters.ArtistSearchAdapter
import com.example.geetsunam.features.presentation.home.search.adapters.SongSearchAdapter
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchEvent
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchState
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.single_genre.adapters.GenreSongsAdapter
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongEvent
import com.example.geetsunam.features.presentation.single_genre.viewmodel.GenreSongState
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.QueryRequestModel
import com.example.geetsunam.utils.models.Song
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

    private val songSearchAdapter by lazy { SongSearchAdapter() }
    private val artistSearchAdapter by lazy { ArtistSearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.tbSearchActivity
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerViews()
        //call search API Upon query field changes
        binding.svAllSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(value: String?): Boolean {
                if (value?.length!! < 2) {
                    return false
                }
                searchViewModel.onEvent(
                    SearchEvent.Search(
                        QueryRequestModel(
                            token = splashViewModel.userFlow.value?.token,
                            query = value,
                        ),
                    ),
                )
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.svAllSearch.clearFocus()
                return false
            }
        })
        observeSearch()
    }

    private fun setupRecyclerViews() {
        //for artists
        binding.rvSearchArtists.adapter = artistSearchAdapter
        val layoutManagerArtists = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchArtists.layoutManager = layoutManagerArtists
        //for songs
        binding.rvSearchSongs.adapter = songSearchAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvSearchSongs.layoutManager = layoutManager
    }

    private fun observeSearch() {
        searchViewModel.searchState.observe(this) { response ->
            if (response.status == SearchState.SearchStatus.LOADING) {
                binding.rvSearchArtists.visibility = View.GONE
                binding.shlSearchArtists.visibility = View.VISIBLE
                binding.rvSearchSongs.visibility = View.GONE
                binding.shlSearchSongs.visibility = View.VISIBLE
            }
            if (response.status == SearchState.SearchStatus.SUCCESS) {
                response.searchData?.let {
                    artistSearchAdapter.setData(
                        response.searchData.artists as List<Artist>
                    )
                }
                response.searchData?.let {
                    songSearchAdapter.setData(
                        response.searchData.songs as List<Song>
                    )
                }
                if (response.searchData?.songs?.size != 0) {
                    binding.tvSearchSongs.visibility = View.VISIBLE
                    binding.rvSearchSongs.visibility = View.VISIBLE
                    musicViewModel.onEvent(
                        MusicEvent.SetMediaItems(
                            response.searchData?.songs!!, "search"
                        )
                    )
                }
                if (response.searchData.artists?.size != 0) {
                    binding.tvSearchArtists.visibility = View.VISIBLE
                    binding.rvSearchArtists.visibility = View.VISIBLE
                }
                binding.shlSearchSongs.visibility = View.GONE
                binding.shlSearchArtists.visibility = View.GONE
            }
            if (response.status == SearchState.SearchStatus.FAILED) {
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