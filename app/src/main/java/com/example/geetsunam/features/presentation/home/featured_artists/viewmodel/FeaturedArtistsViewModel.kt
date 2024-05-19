package com.example.geetsunam.features.presentation.home.featured_artists.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.FeaturedArtist
import com.example.geetsunam.features.domain.usecases.GetFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedArtistsUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedArtistsUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.Artist
import com.example.geetsunam.utils.models.CommonRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedArtistsViewModel @Inject constructor(
    private val getFeaturedArtistsUsecase: GetFeaturedArtistsUsecase,
    private val saveFeaturedArtistsUsecase: SaveFeaturedArtistsUsecase,
    private val getLocalFeaturedArtistsUsecase: GetLocalFeaturedArtistsUsecase
) : ViewModel() {
    private val _state = MutableLiveData(FeaturedArtistsState.idle)
    val featuredArtistsState: LiveData<FeaturedArtistsState> = _state

    fun onEvent(event: FeaturedArtistsEvent) {
        when (event) {
            is FeaturedArtistsEvent.GetFeaturedArtists -> {
                getArtists(event.token)
            }

            is FeaturedArtistsEvent.RefershArtists -> {
                fetchArtistsFromApi(event.token)
            }

            is FeaturedArtistsEvent.SaveArtists -> {
                saveArtists(event.artists)
            }

            else -> {}
        }
    }

    private fun getArtists(token: String) {
        _state.postValue(
            _state.value?.copy(
                status = FeaturedArtistsState.FeaturedArtistsStatus.LOADING,
                message = "Getting artists"
            )
        )
        viewModelScope.launch {
            getLocalFeaturedArtistsUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val artists: MutableList<Artist?> = ArrayList()
                    for (a: FeaturedArtist in it) {
                        val artist = Artist(
                            id = a.id,
                            fullname = a.fullname,
                            email = a.email,
                            isFeatured = a.isFeatured,
                            profileImage = a.profileImage
                        )
                        artists.add(artist)
                    }
                    _state.postValue(
                        _state.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.SUCCESS,
                            message = "success",
                            fromApi = false,
                            artists = artists
                        )
                    )
                } else {
                    fetchArtistsFromApi(token)
                }
            }
        }
    }

    private fun fetchArtistsFromApi(token: String) {
        getFeaturedArtistsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.postValue(
                        _state.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.LOADING,
                            message = "Getting featured artists"
                        )
                    )
                }

                is Resource.Success -> {
                    _state.postValue(
                        _state.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            artists = result.data?.data?.artists,
                        )
                    )
                }

                is Resource.Error -> {
                    _state.postValue(
                        _state.value?.copy(
                            status = FeaturedArtistsState.FeaturedArtistsStatus.FAILED,
                            message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveArtists(artists: List<Artist?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveFeaturedArtistsUsecase.call(artists)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}