package com.example.geetsunam.features.presentation.for_you.viewmodel

import androidx.lifecycle.ViewModel
import com.example.geetsunam.features.domain.usecases.GetRecommendedSongsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val getRecommendedSongsUsecase: GetRecommendedSongsUsecase
) : ViewModel() {

}