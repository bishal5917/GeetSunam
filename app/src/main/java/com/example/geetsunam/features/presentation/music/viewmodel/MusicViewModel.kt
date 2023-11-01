package com.example.geetsunam.features.presentation.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {
    private val _musicState = MutableLiveData(MusicState.idle)
    val musicState: LiveData<MusicState> = _musicState

    fun onEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.PlayMusic -> {
                _musicState.postValue(
                    _musicState.value?.copy(
                        status = MusicState.MusicStatus.SUCCESS,
                        message = "playing music",
                        music = event.music,
                    )
                )
            }

            else -> {}
        }
    }

//    private fun getGenre(token: String) {
//        getGenresUsecase.call(CommonRequestModel(token)).onEach { result ->
//            when (result) {
//                is Resource.Loading -> {
//                    _genreState.postValue(
//                        _genreState.value?.copy(
//                            status = GenreState.GenreStatus.LOADING, message = "Getting genres"
//                        )
//                    )
//                }
//
//                is Resource.Success -> {
//                    _genreState.postValue(
//                        _genreState.value?.copy(
//                            status = GenreState.GenreStatus.SUCCESS,
//                            message = "success",
//                            genres = result.data?.data,
//                        )
//                    )
//                }
//
//                is Resource.Error -> {
//                    _genreState.postValue(
//                        _genreState.value?.copy(
//                            status = GenreState.GenreStatus.FAILED, message = result.message
//                        )
//                    )
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
}