package com.example.geetsunam.features.presentation.home.genres.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.FeaturedGenre
import com.example.geetsunam.features.domain.usecases.GetGenresUsecase
import com.example.geetsunam.features.domain.usecases.GetLocalFeaturedGenresUsecase
import com.example.geetsunam.features.domain.usecases.SaveFeaturedGenreUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.CommonRequestModel
import com.example.geetsunam.utils.models.Genre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val getGenresUsecase: GetGenresUsecase,
    private val saveFeaturedGenreUsecase: SaveFeaturedGenreUsecase,
    private val getLocalFeaturedGenresUsecase: GetLocalFeaturedGenresUsecase
) : ViewModel() {
    private val _genreState = MutableLiveData(GenreState.idle)
    val genreState: LiveData<GenreState> = _genreState

    fun onEvent(event: GenreEvent) {
        when (event) {
            is GenreEvent.GetGenre -> {
                getGenres(event.token)
            }

            is GenreEvent.RefreshGenres -> {
                fetchGenresFromApi(event.token)
            }

            is GenreEvent.SaveGenres -> {
                saveGenres(event.genres)
            }

            else -> {}
        }
    }

    private fun getGenres(token: String) {
        _genreState.postValue(
            _genreState.value?.copy(
                status = GenreState.GenreStatus.LOADING, message = "Getting genres"
            )
        )
        viewModelScope.launch {
            getLocalFeaturedGenresUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val genres: MutableList<Genre?> = ArrayList()
                    for (g: FeaturedGenre in it) {
                        val genre = Genre(
                            id = g.id, name = g.name, image = g.image
                        )
                        genres.add(genre)
                    }
                    _genreState.postValue(
                        _genreState.value?.copy(
                            status = GenreState.GenreStatus.SUCCESS,
                            message = "success",
                            fromApi = false,
                            genres = genres
                        )
                    )
                } else {
                    fetchGenresFromApi(token)
                }
            }
        }
    }

    private fun fetchGenresFromApi(token: String) {
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
                            fromApi = true,
                            genres = result.data?.data?.genres,
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

    private fun saveGenres(genres: List<Genre?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveFeaturedGenreUsecase.call(genres)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}