package com.example.geetsunam.features.presentation.offline_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
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
import com.example.geetsunam.features.presentation.offline_song.adapters.OfflineSongAdapter
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongEvent
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongState
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.Network
import com.example.geetsunam.utils.models.Song
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import javax.inject.Inject

@AndroidEntryPoint
class OfflineSongFragment : Fragment() {
    private lateinit var gview: View

    @Inject
    lateinit var musicViewModel: MusicViewModel

    @Inject
    lateinit var offlineSongViewModel: OfflineSongViewModel

    private val offlineSongsAdapter by lazy {
        OfflineSongAdapter(
            requireContext(), offlineSongViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_offline_song, container, false)
        setupRecyclerView()
        pullToRefresh()
        getOfflineSongs()
        return gview
    }

    private fun setupRecyclerView() {
        val songsRecView = gview.findViewById<RecyclerView>(R.id.rvOfflineSongs)
        songsRecView.adapter = offlineSongsAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        songsRecView.layoutManager = layoutManager
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlOfflineSong)
        swipeToRefresh.setOnRefreshListener {
            offlineSongViewModel.onEvent(OfflineSongEvent.GetOfflineSongs)
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun getOfflineSongs() {
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvOfflineSongs)
        val noSongsTv = gview.findViewById<TextView>(R.id.tvNoSongs)
        val pb = gview.findViewById<ProgressBar>(R.id.pbOfflineSong)
        offlineSongViewModel.state.observe(viewLifecycleOwner) { response ->
            if (response.status == OfflineSongState.OfflineSongStatus.IDLE) {
                offlineSongViewModel.onEvent(OfflineSongEvent.GetOfflineSongs)
            }
            if (response.status == OfflineSongState.OfflineSongStatus.LOADING) {
                recyclerView.visibility = View.GONE
                noSongsTv.visibility = View.GONE
                pb.visibility = View.VISIBLE
            }
            if (response.status == OfflineSongState.OfflineSongStatus.SUCCESS) {
                response.songs?.let {
                    offlineSongsAdapter.setData(
                        response.songs as List<Song>
                    )
                }
                recyclerView.visibility = View.VISIBLE
                noSongsTv.visibility = View.GONE
                pb.visibility = View.GONE
                musicViewModel.onEvent(MusicEvent.SetMediaItems(response.songs, "offline"))
            }
            if (response.status == OfflineSongState.OfflineSongStatus.EMPTY) {
                recyclerView.visibility = View.GONE
                noSongsTv.visibility = View.VISIBLE
                pb.visibility = View.GONE
            }
            if (response.status == OfflineSongState.OfflineSongStatus.FAILED) {
                recyclerView.visibility = View.GONE
                noSongsTv.visibility = View.VISIBLE
                pb.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
            if (response.status == OfflineSongState.OfflineSongStatus.DELETED) {
                offlineSongViewModel.onEvent(OfflineSongEvent.GetOfflineSongs)
            }
        }
    }

    override fun onResume() {
        val songs: List<Song?>? = offlineSongViewModel.state.value?.songs
        if (songs != null) {
            musicViewModel.onEvent(MusicEvent.SetMediaItems(songs, "offline"))
        }
        super.onResume()
    }
}