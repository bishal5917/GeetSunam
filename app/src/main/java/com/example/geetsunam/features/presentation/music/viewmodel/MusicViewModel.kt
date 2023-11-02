package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetSingleSongUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val getSingleSongUsecase: GetSingleSongUsecase) :
    ViewModel() {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.GetMusic -> {
                getSong(event.token, event.songId)
            }

            else -> {}
        }
    }

    private fun getSong(token: String, id: String) {
        getSingleSongUsecase.call(CommonRequestModel(token, id)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _musicState.postValue(
                        _musicState.value?.copy(
                            status = MusicState.MusicStatus.LOADING, message = "Getting music"
                        )
                    )
                }

                is Resource.Success -> {
                    _musicState.postValue(
                        _musicState.value?.copy(
                            status = MusicState.MusicStatus.SUCCESS,
                            message = "success",
                            music = result.data?.data?.song,
                        )
                    )
                }

                is Resource.Error -> {
                    _musicState.postValue(
                        _musicState.value?.copy(
                            status = MusicState.MusicStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}