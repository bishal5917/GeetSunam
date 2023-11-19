package com.example.geetsunam.features.presentation.trending

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
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
class TrendingFragment : Fragment() {

    private val trendingAdapter by lazy { TrendingAdapter() }

    private lateinit var gview: View

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var trendingViewModel: TrendingViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_trending, container, false)
        pullToRefresh()
        setupRecyclerView()
        requestTrending()
        return gview
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlTrending)
        swipeToRefresh.setOnRefreshListener {
            trendingViewModel.onEvent(
                TrendingEvent.GetTrendingSongs(
                    splashViewModel.userFlow.value?.token ?: ""
                )
            )
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        val songsRecView = gview.findViewById<RecyclerView>(R.id.rvTrendingSongs)
        songsRecView.adapter = trendingAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        songsRecView.layoutManager = layoutManager
    }

    private fun requestTrending() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvTrendingSongs)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlTrending)

        trendingViewModel.trendingSongState.observe(viewLifecycleOwner) { response ->
            if (response.status == TrendingState.TrendingStatus.IDLE) {
                trendingViewModel.onEvent(
                    TrendingEvent.GetTrendingSongs(
                        splashViewModel.userFlow.value?.token ?: ""
                    )
                )
            }
            if (response.status == TrendingState.TrendingStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == TrendingState.TrendingStatus.SUCCESS) {
                response.songs?.let {
                    trendingAdapter.setData(
                        response.songs.songs as List<Song>
                    )
                }
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
                musicViewModel.onEvent(MusicEvent.SetPlaylist(response.songs?.songs!!, "trending"))
            }
            if (response.status == TrendingState.TrendingStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }
}