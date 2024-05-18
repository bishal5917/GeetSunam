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
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingEvent
import com.example.geetsunam.utils.Constants
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.Network
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
            if (Network.hasInternetConnection(context)) {
                recommendViewModel.onEvent(
                    RecommendEvent.RefreshRecommended(
                        splashViewModel.splashState.value?.userEntity?.token ?: ""
                    )
                )
            } else {
                CustomToast.showToast(context = requireContext(), Constants.noInternet)
            }
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
                        splashViewModel.splashState.value?.userEntity?.token ?: ""
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
                if (response.fromApi == true) {
                    //resave into roomdb if fetched from API
                    recommendViewModel.onEvent(RecommendEvent.SaveRecommended(response.songs))
                }
            }
            if (response.status == RecommendState.RecommendStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }

    override fun onResume() {
        val songs: List<Song?>? = recommendViewModel.songState.value?.songs;
        if (songs != null) {
            musicViewModel.onEvent(MusicEvent.SetMediaItems(songs, "recommended"))
        }
        super.onResume()
    }
}