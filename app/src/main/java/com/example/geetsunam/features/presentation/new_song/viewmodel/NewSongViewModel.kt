package com.example.geetsunam.features.presentation.new_song.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetNewSongsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewSongViewModel @Inject constructor(
    private val getNewSongsUsecase: GetNewSongsUsecase
) : ViewModel() {
    private val _newSongsState = MutableLiveData(NewSongState.idle)
    val newSongsState: LiveData<NewSongState> = _newSongsState

    fun onEvent(event: NewSongEvent) {
        when (event) {
            is NewSongEvent.GetNewSongs -> {
                getNewSongs(event.token)
            }

            else -> {}
        }
    }

    private fun getNewSongs(token: String) {
        getNewSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.LOADING,
                            message = "Getting new songs"
                        )
                    )
                }

                is Resource.Success -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.SUCCESS,
                            message = "success",
                            songs = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _newSongsState.postValue(
                        _newSongsState.value?.copy(
                            status = NewSongState.NewSongStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}