package com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.R
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ToggleFavViewModel @Inject constructor(
    private val toggleFavouriteUsecase: ToggleFavouriteUsecase
) : ViewModel() {
    private val _toggleFavState = MutableLiveData(ToggleFavState.idle)
    val toggleFavState: LiveData<ToggleFavState> = _toggleFavState

    fun onEvent(event: ToggleFavEvent) {
        when (event) {
            is ToggleFavEvent.AddFavourite -> {
                addFavourite(event.commonRequestModel)
            }
        }
    }

    private fun addFavourite(commonRequestModel: CommonRequestModel) {
        toggleFavouriteUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _toggleFavState.postValue(
                        _toggleFavState.value?.copy(
                            status = ToggleFavState.ToggleFavStatus.LOADING, message =
                            "Requesting,please wait..."
                        )
                    )
                }

                is Resource.Success -> {
                    var message: String
                    var drawableId: Int
                    if (result.data?.data?.song?.isFavourite == true) {
                        message = "Added to your Liked Songs"
                        drawableId = R.drawable.ic_favorite_fill
                    } else {
                        message = "Removed from your Liked Songs"
                        drawableId = R.drawable.ic_favorite
                    }
                    _toggleFavState.postValue(
                        _toggleFavState.value?.copy(
                            status = ToggleFavState.ToggleFavStatus.SUCCESS,
                            message = message,
                            drawableId = drawableId,
                        )
                    )
                }

                is Resource.Error -> {
                    _toggleFavState.postValue(
                        _toggleFavState.value?.copy(
                            status = ToggleFavState.ToggleFavStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}