package com.example.geetsunam.features.presentation.music.track.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.TrackPlayedSongUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TrackSongViewModel @Inject constructor(
    private val trackPlayedSongUsecase: TrackPlayedSongUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(TrackSongState.idle)
    val trackSongState: LiveData<TrackSongState> = _liveState

    fun onEvent(event: TrackSongEvent) {
        when (event) {
            is TrackSongEvent.TrackCurrentSong -> {
                trackSong(event.commonRequestModel)
            }

            else -> {}
        }
    }

    private fun trackSong(commonRequestModel: CommonRequestModel) {
        trackPlayedSongUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = TrackSongState.TrackSongStatus.TRACKING,
                            message = "Tracking current song..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = TrackSongState.TrackSongStatus.TRACKED,
                            message = "Success",
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = TrackSongState.TrackSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}