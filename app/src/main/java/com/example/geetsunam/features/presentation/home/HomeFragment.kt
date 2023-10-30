package com.example.geetsunam.features.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreEvent
import com.example.geetsunam.features.presentation.home.genres.viewmodel.GenreViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var genreViewModel: GenreViewModel

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        requestApi()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun requestApi() {
        genreViewModel.onEvent(GenreEvent.GetGenre(splashViewModel.userIdFlow.value.toString()))
    }
}