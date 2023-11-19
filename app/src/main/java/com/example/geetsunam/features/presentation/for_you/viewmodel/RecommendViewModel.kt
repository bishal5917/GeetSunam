package com.example.geetsunam.features.presentation.for_you.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetRecommendedSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val getRecommendedSongsUsecase: GetRecommendedSongsUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(RecommendState.idle)
    val songState: LiveData<RecommendState> = _liveState

    fun onEvent(event: RecommendEvent) {
        when (event) {
            is RecommendEvent.GetRecommendedSongs -> {
                getRecommendedSongs(event.token)
            }

            else -> {}
        }
    }

    private fun getRecommendedSongs(token: String) {
        getRecommendedSongsUsecase.call(CommonRequestModel(token = token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.LOADING,
                            message = "Getting songs ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.FAILED,
                            message = result.message
                        )
                    )
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}