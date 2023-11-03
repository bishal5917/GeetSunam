package com.example.geetsunam.features.presentation.new_song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator.AdapterChanges
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.new_song.adapters.NewsongsAdapter
import com.example.geetsunam.features.presentation.new_song.viewmodel.NewSongEvent
import com.example.geetsunam.features.presentation.new_song.viewmodel.NewSongState
import com.example.geetsunam.features.presentation.new_song.viewmodel.NewSongViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.features.presentation.trending.adapters.TrendingAdapter
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingEvent
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingState
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.Song
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewSongFragment : Fragment() {
    private val newSongAdapter by lazy { NewsongsAdapter() }

    private lateinit var gview: View

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var newSongViewModel: NewSongViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_new_song, container, false)
        pullToRefresh()
        setupRecyclerView()
        requestTrending()
        return gview
    }

    private fun setupRecyclerView() {
        val newSongRecView = gview.findViewById<RecyclerView>(R.id.rvNewSong)
        newSongRecView.adapter = newSongAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        newSongRecView.layoutManager = layoutManager
    }

    private fun requestTrending() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvNewSong)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlNewSong)

        newSongViewModel.newSongsState.observe(viewLifecycleOwner) { response ->
            if (response.status == NewSongState.NewSongStatus.IDLE) {
                newSongViewModel.onEvent(NewSongEvent.GetNewSongs(splashViewModel.userIdFlow.value))
            }
            if (response.status == NewSongState.NewSongStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == NewSongState.NewSongStatus.SUCCESS) {
                response.songs?.let {
                    newSongAdapter.setData(
                        response.songs.songs as List<Song>
                    )
                }
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
            }
            if (response.status == NewSongState.NewSongStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlNewSong)
        swipeToRefresh.setOnRefreshListener {
            newSongViewModel.onEvent(NewSongEvent.GetNewSongs(splashViewModel.userIdFlow.value))
            swipeToRefresh.isRefreshing = false
        }
    }
}