package com.example.geetsunam.features.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geetsunam.R
import com.example.geetsunam.features.data.models.artist.ArtistResponseModel
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
import com.example.geetsunam.features.presentation.home.featured_artists.adapters.FeaturedArtistsAdapter
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsEvent
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsState
import com.example.geetsunam.features.presentation.home.featured_artists.viewmodel.FeaturedArtistsViewModel
import com.example.geetsunam.features.presentation.home.genres.adapters.GenreAdapter
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreEvent
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreState
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomToast
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val genreAdapter by lazy { GenreAdapter() }
    private val featuredArtistsAdapter by lazy { FeaturedArtistsAdapter() }

    private lateinit var gview: View

    @Inject
    lateinit var genreViewModel: GenreViewModel

    @Inject
    lateinit var featuredArtistsViewModel: FeaturedArtistsViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_home, container, false)

        pullToRefresh()
        requestGenres()
        requestFeaturedArtists()
        setupRecyclerView()
        return gview
    }

    private fun pullToRefresh() {
        val swipeToRefresh = gview.findViewById<SwipeRefreshLayout>(R.id.srlHome)
        swipeToRefresh.setOnRefreshListener {
            genreViewModel.onEvent(GenreEvent.GetGenre(splashViewModel.userIdFlow.value))
            featuredArtistsViewModel.onEvent(
                FeaturedArtistsEvent.GetFeaturedArtists(splashViewModel.userIdFlow.value)
            )
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        //for genres
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvGenre)
        recyclerView.adapter = genreAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        //for featured artists
        val artistRecView = gview.findViewById<RecyclerView>(R.id.rvFeaturedArtists)
        artistRecView.adapter = featuredArtistsAdapter
        val artistRecViewLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        artistRecView.layoutManager = artistRecViewLayoutManager
    }

    private fun requestGenres() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvGenre)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlGenre)

        genreViewModel.genreState.observe(viewLifecycleOwner) { response ->
            if (response.status == GenreState.GenreStatus.IDLE) {
                genreViewModel.onEvent(GenreEvent.GetGenre(splashViewModel.userIdFlow.value))
            }
            if (response.status == GenreState.GenreStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == GenreState.GenreStatus.SUCCESS) {
                response.genres?.let { genreAdapter.setData(response.genres.genres as List<GenreResponseModel.Data.Genre>) }
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
            }
            if (response.status == GenreState.GenreStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }

    private fun requestFeaturedArtists() {
        //find recyclerview and shimmerview
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvFeaturedArtists)
        val shimmerView = gview.findViewById<ShimmerFrameLayout>(R.id.shlFavArtists)

        featuredArtistsViewModel.featuredArtistsState.observe(viewLifecycleOwner) { response ->
            if (response.status == FeaturedArtistsState.FeaturedArtistsStatus.IDLE) {
                featuredArtistsViewModel.onEvent(
                    FeaturedArtistsEvent.GetFeaturedArtists(splashViewModel.userIdFlow.value)
                )
            }
            if (response.status == FeaturedArtistsState.FeaturedArtistsStatus.LOADING) {
                recyclerView.visibility = View.GONE
                shimmerView.visibility = View.VISIBLE
            }
            if (response.status == FeaturedArtistsState.FeaturedArtistsStatus.SUCCESS) {
                response.artists?.let {
                    featuredArtistsAdapter.setData(
                        response.artists.artists as List<ArtistResponseModel.Data.Artist>
                    )
                }
                recyclerView.visibility = View.VISIBLE
                shimmerView.visibility = View.GONE
            }
            if (response.status == FeaturedArtistsState.FeaturedArtistsStatus.FAILED) {
                recyclerView.visibility = View.GONE
                CustomToast.showToast(context = requireContext(), "${response.message}")
            }
        }
    }
}