package com.example.geetsunam.features.presentation.liked_song.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetFavouriteSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavSongViewModel @Inject constructor(
    private val getFavouriteSongsUsecase: GetFavouriteSongsUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(FavSongState.idle)
    val favSongsState: LiveData<FavSongState> = _liveState

    fun onEvent(event: FavSongEvent) {
        when (event) {
            is FavSongEvent.GetFavouriteSongs -> {
                getFavouriteSongs(event.token)
            }

            else -> {}
        }
    }

    private fun getFavouriteSongs(token: String) {
        getFavouriteSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.LOADING,
                            message = "Getting your favourites..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = FavSongState.FavSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}