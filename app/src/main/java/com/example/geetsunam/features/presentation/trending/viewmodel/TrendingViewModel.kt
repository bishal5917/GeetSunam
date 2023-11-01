package com.example.geetsunam.features.presentation.trending.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingSongsUsecase: GetTrendingSongsUsecase
) : ViewModel() {
    private val _trendingSongState = MutableLiveData(TrendingState.idle)
    val trendingSongState: LiveData<TrendingState> = _trendingSongState

    fun onEvent(event: TrendingEvent) {
        when (event) {
            is TrendingEvent.GetTrendingSongs -> {
                getSongs(event.token)
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        getTrendingSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.LOADING,
                            message = "Getting genres"
                        )
                    )
                }

                is Resource.Success -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}