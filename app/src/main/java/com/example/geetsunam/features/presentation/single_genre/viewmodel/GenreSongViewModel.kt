package com.example.geetsunam.features.presentation.single_genre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetGenreSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GenreSongViewModel @Inject constructor(
    private val getGenreSongsUsecase: GetGenreSongsUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(GenreSongState.idle)
    val genreSongsState: LiveData<GenreSongState> = _liveState

    fun onEvent(event: GenreSongEvent) {
        when (event) {
            is GenreSongEvent.GetGenreSongs -> {
                getGenreSongs(event.commonRequestModel)
            }

            else -> {}
        }
    }

    private fun getGenreSongs(commonRequestModel: CommonRequestModel) {
        getGenreSongsUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = GenreSongState.GenreSongStatus.LOADING,
                            message = "Getting songs ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = GenreSongState.GenreSongStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = GenreSongState.GenreSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}