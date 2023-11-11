package com.example.geetsunam.features.presentation.single_artist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetArtistSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArtistSongViewModel @Inject constructor(
    private val getArtistSongsUsecase: GetArtistSongsUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(ArtistSongState.idle)
    val artistSongsState: LiveData<ArtistSongState> = _liveState

    fun onEvent(event: ArtistSongEvent) {
        when (event) {
            is ArtistSongEvent.GetArtistSongs -> {
                getArtistSongs(event.commonRequestModel)
            }

            else -> {}
        }
    }

    private fun getArtistSongs(commonRequestModel: CommonRequestModel) {
        getArtistSongsUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ArtistSongState.ArtistSongStatus.LOADING,
                            message = "Getting songs ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ArtistSongState.ArtistSongStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = ArtistSongState.ArtistSongStatus.FAILED,
                            message = result.message
                        )
                    )
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}