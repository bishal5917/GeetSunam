package com.example.geetsunam.features.presentation.home.genres.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val getGenresUsecase: GetGenresUsecase
) : ViewModel() {
    private val _genreState = MutableLiveData(GenreState.idle)
    val genreState: LiveData<GenreState> = _genreState

    fun onEvent(event: GenreEvent) {
        when (event) {
            is GenreEvent.GetGenre -> {
                getGenre(event.token)
            }
            else -> {}
        }
    }

    private fun getGenre(token: String) {
        getGenresUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _genreState.postValue(
                        _genreState.value?.copy(
                            status = GenreState.GenreStatus.LOADING, message = "Getting genres"
                        )
                    )
                }

                is Resource.Success -> {
                    _genreState.postValue(
                        _genreState.value?.copy(
                            status = GenreState.GenreStatus.SUCCESS,
                            message = "success",
                            genres = result.data?.data,
                        )
                    )
                }

                is Resource.Error -> {
                    _genreState.postValue(
                        _genreState.value?.copy(
                            status = GenreState.GenreStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}