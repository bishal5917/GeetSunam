package com.example.geetsunam.features.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.R
import com.example.geetsunam.features.data.models.genres.GenreResponseModel
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

    private lateinit var gview: View

    @Inject
    lateinit var genreViewModel: GenreViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gview = inflater.inflate(R.layout.fragment_home, container, false)

        requestApi()
        setupRecyclerView()
//        pullToRefresh()
        return gview
    }

    private fun setupRecyclerView() {
        val recyclerView = gview.findViewById<RecyclerView>(R.id.rvGenre)
        recyclerView.adapter = genreAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private fun requestApi() {
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
}