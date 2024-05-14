package com.example.geetsunam.features.presentation.trending.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.Trending
import com.example.geetsunam.features.domain.usecases.GetLocalTrendingUsecase
import com.example.geetsunam.features.domain.usecases.GetTrendingSongsUsecase
import com.example.geetsunam.features.domain.usecases.SaveTrendingUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.Artist
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
class TrendingViewModel @Inject constructor(
    private val getTrendingSongsUsecase: GetTrendingSongsUsecase,
    private val saveTrendingUsecase: SaveTrendingUsecase,
    private val getLocalTrendingUsecase: GetLocalTrendingUsecase
) : ViewModel() {
    private val _trendingSongState = MutableLiveData(TrendingState.idle)
    val trendingSongState: LiveData<TrendingState> = _trendingSongState

    fun onEvent(event: TrendingEvent) {
        when (event) {
            is TrendingEvent.GetTrendingSongs -> {
                getSongs(event.token)
            }

            is TrendingEvent.RefreshTrending -> {
                fetchSongsFromApi(event.token)
            }

            is TrendingEvent.SaveTrending -> {
                saveTrending(event.songs)
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        _trendingSongState.postValue(
            _trendingSongState.value?.copy(
                status = TrendingState.TrendingStatus.LOADING,
                message = "Getting trending songs"
            )
        )
        viewModelScope.launch {
            getLocalTrendingUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (trending: Trending in it) {
                        val song = Song(
                            id = trending.id,
                            isFavourite = trending.isFavourite,
                            isFeatured = trending.isFavourite,
                            source = trending.source,
                            stream = trending.stream,
                            title = trending.songName,
                            coverArt = trending.coverArt,
                            duration = trending.duration,
                            artists = Artist(fullname = trending.artistName)
                        )
                        songs.add(song)
                    }
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.SUCCESS,
                            message = "success",
                            fromApi = false,
                            songs = songs,
                        )
                    )
                } else {
                    fetchSongsFromApi(token)
                }
            }
        }
    }

    private fun fetchSongsFromApi(token: String) {
        getTrendingSongsUsecase.call(CommonRequestModel(token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.LOADING,
                            message = "Getting trending songs"
                        )
                    )
                }

                is Resource.Success -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            songs = result.data?.data?.songs,
                        )
                    )
                }

                is Resource.Error -> {
                    _trendingSongState.postValue(
                        _trendingSongState.value?.copy(
                            status = TrendingState.TrendingStatus.FAILED, message = result.message
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveTrending(songs: List<Song?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveTrendingUsecase.call(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}