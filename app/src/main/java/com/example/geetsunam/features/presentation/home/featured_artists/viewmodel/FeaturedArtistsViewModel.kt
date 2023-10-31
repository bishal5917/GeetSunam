package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeaturedArtistsViewModel @Inject constructor(
    private val getFeaturedArtistsUsecase: GetFeaturedArtistsUsecase
) : ViewModel() {
    private val _featuredArtistsState = MutableLiveData(FeaturedArtistsState.idle)
    val featuredArtistsState: LiveData<FeaturedArtistsState> = _featuredArtistsState

    fun onEvent(event: FeaturedArtistsEvent) {
        when (event) {
            is FeaturedArtistsEvent.GetFeaturedArtists -> {
                getFeaturedArtists(event.token)
            }
            else -> {}
        }
    }

    private fun getFeaturedArtists(token: String) {
        getFeaturedArtistsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _featuredArtistsState.postValue(
                        _featuredArtistsState.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.LOADING, message
                            = "Getting featured artists"
                        )
                    )
                }

                is Resource.Success -> {
                    _featuredArtistsState.postValue(
                        _featuredArtistsState.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.SUCCESS,
                            message = "success",
                            artists = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _featuredArtistsState.postValue(
                        _featuredArtistsState.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.FAILED,
                            message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}