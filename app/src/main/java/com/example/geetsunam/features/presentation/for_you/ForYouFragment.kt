package com.example.geetsunam.features.presentation.for_you

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.for_you.adapters.RecommendedSongsAdapter
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendEvent
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendState
import com.example.geetsunam.features.presentation.for_you.viewmodel.RecommendViewModel
import com.example.geetsunam.features.presentation.music.viewmodel.MusicEvent
import com.example.geetsunam.features.presentation.music.viewmodel.MusicViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.models.Song
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForYouFragment : Fragment() {
    private val recommendedSongsAdapter by lazy { RecommendedSongsAdapter() }

    private lateinit var gview: View

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var recommendViewModel: RecommendViewModel

    @Inject
    lateinit var musicViewModel: MusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_for_you, container, false)
        pullToRefresh()
        setupRecyclerView()
        requestRecommended()
        return gview
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlForYou)
        swipeToRefresh.setOnRefreshListener {
            recommendViewModel.onEvent(
                RecommendEvent.GetRecommendedSongs(
                    splashViewModel.userFlow.value?.token ?: ""
                )
            )
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        val songsRecView = gview.findViewById<RecyclerView>(R.id.rvForYou)
        songsRecView.adapter = recommendedSongsAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        songsRecView.layoutManager = layoutManager
    }

    private fun requestRecommended() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvForYou)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlForYou)

        recommendViewModel.songState.observe(viewLifecycleOwner) { response ->
            if (response.status == RecommendState.RecommendStatus.IDLE) {
                recommendViewModel.onEvent(
                    RecommendEvent.GetRecommendedSongs(
                        splashViewModel.userFlow.value?.token ?: ""
                    )
                )
            }
            if (response.status == RecommendState.RecommendStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == RecommendState.RecommendStatus.SUCCESS) {
                response.songs?.let {
                    recommendedSongsAdapter.setData(
                        response.songs as List<Song>
                    )
                }
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
                musicViewModel.onEvent(
                    MusicEvent.SetMediaItems(
                        response.songs, "recommended"
                    )
                )
            }
            if (response.status == RecommendState.RecommendStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }
}