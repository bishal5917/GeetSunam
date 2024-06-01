package com.example.geetsunam.features.presentation.music.toggle_fav.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.R
import com.example.geetsunam.features.domain.usecases.AddFavouriteLocalUsecase
import com.example.geetsunam.features.domain.usecases.DeleteFavouriteLocalUsecase
import com.example.geetsunam.features.domain.usecases.ToggleFavouriteUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToggleFavViewModel @Inject constructor(
    private val toggleFavouriteUsecase: ToggleFavouriteUsecase,
    private val addFavouriteLocalUsecase: AddFavouriteLocalUsecase,
    private val deleteFavouriteLocalUsecase: DeleteFavouriteLocalUsecase
) : ViewModel() {
    private val _toggleFavState = MutableLiveData(ToggleFavState.idle)
    val toggleFavState: LiveData<ToggleFavState> = _toggleFavState

    fun onEvent(event: ToggleFavEvent) {
        when (event) {
            is ToggleFavEvent.AddFavourite -> {
                addFavourite(event.commonRequestModel)
            }

            else -> {}
        }
    }

    private fun addFavourite(commonRequestModel: CommonRequestModel) {
        toggleFavouriteUsecase.call(commonRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _toggleFavState.postValue(
                        _toggleFavState.value?.copy(
                            status = ToggleFavState.ToggleFavStatus.LOADING,
                            message = "Requesting,please wait..."
                        )
                    )
                }

                is Resource.Success -> {
                    val message: String
                    val drawableId: Int
                    val added: Int
                    val song = result.data?.data?.song
                    if (song?.isFavourite == true) {
                        message = "Added to your Liked Songs"
                        drawableId = R.drawable.ic_favorite_fill
                        added = 1;
                    } else {
                        message = "Removed from your Liked Songs"
                        drawableId = R.drawable.ic_favorite
                        added = 0
                    }
                    saveOrDeleteOffline(song ?: Song(), added)
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

    private fun saveOrDeleteOffline(song: Song, added: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (added == 1) {
                    addFavouriteLocalUsecase.call(song)
                } else {
                    deleteFavouriteLocalUsecase.call(song)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}