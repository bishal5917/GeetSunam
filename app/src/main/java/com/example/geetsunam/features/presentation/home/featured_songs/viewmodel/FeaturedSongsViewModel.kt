package com.example.geetsunam.features.presentation.home.featured_songs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetFeaturedSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FeaturedSongsViewModel @Inject constructor(
    private val getFeaturedSongsUsecase: GetFeaturedSongsUsecase
) : ViewModel() {
    private val _featuredSongState = MutableLiveData(FeaturedSongsState.idle)
    val featuredSongState: LiveData<FeaturedSongsState> = _featuredSongState

    fun onEvent(event: FeaturedSongsEvent) {
        when (event) {
            is FeaturedSongsEvent.GetFeaturedSongs -> {
                getSongs(event.token)
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        getFeaturedSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.LOADING,
                            message = "Getting genres"
                        )
                    )
                }

                is Resource.Success -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _featuredSongState.postValue(
                        _featuredSongState.value?.copy(
                            status = FeaturedSongsState.SongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}