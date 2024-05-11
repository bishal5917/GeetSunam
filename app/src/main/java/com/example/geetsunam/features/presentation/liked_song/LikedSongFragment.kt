package com.example.geetsunam.features.presentation.liked_song

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.liked_song.adapters.FavSongsAdapter
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongEvent
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongState
import com.example.geetsunam.features.presentation.liked_song.viewmodel.FavSongViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.Song
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LikedSongFragment : Fragment() {
    private val favSongsAdapter by lazy { FavSongsAdapter() }

    private lateinit var gview: View

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var favSongViewModel: FavSongViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_liked_song, container, false)
        pullToRefresh()
        setupRecyclerView()
        requestTrending()
        return gview
    }

    private fun setupRecyclerView() {
        val songsRecView = gview.findViewById<RecyclerView>(R.id.rvLikedSongs)
        songsRecView.adapter = favSongsAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        songsRecView.layoutManager = layoutManager
    }

    private fun requestTrending() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvLikedSongs)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlLikedSong)
        val likedSongsQty = gview.findViewById<TextView>(R.id.tvLikedSongsQty)
        favSongViewModel.favSongsState.observe(viewLifecycleOwner) { response ->
            if (response.status == FavSongState.FavSongStatus.IDLE) {
                favSongViewModel.onEvent(
                    FavSongEvent.GetFavouriteSongs(
                        splashViewModel.userFlow.value?.token ?: ""
                    )
                )
            }
            if (response.status == FavSongState.FavSongStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == FavSongState.FavSongStatus.SUCCESS) {
                response.songs?.let {
                    favSongsAdapter.setData(
                        response.songs.songs as List<Song>
                    )
                }
                likedSongsQty.text = "${response.songs?.songs?.size} Songs"
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
                musicViewModel.onEvent(MusicEvent.SetMediaItems(response.songs?.songs!!, "liked"))

            }
            if (response.status == FavSongState.FavSongStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlLikedSong)
        swipeToRefresh.setOnRefreshListener {
            favSongViewModel.onEvent(
                FavSongEvent.GetFavouriteSongs(
                    splashViewModel.userFlow.value?.token ?: ""
                )
            )
            swipeToRefresh.isRefreshing = false
        }
    }
}