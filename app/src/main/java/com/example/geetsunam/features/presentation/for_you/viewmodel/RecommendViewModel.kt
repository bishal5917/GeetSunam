package com.example.geetsunam.features.presentation.for_you.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.database.entities.Recommended
import com.example.geetsunam.database.entities.Trending
import com.example.geetsunam.features.domain.usecases.GetLocalRecommendedUsecase
import com.example.geetsunam.features.domain.usecases.GetRecommendedSongsUsecase
import com.example.geetsunam.features.domain.usecases.SaveRecommendedUsecase
import com.example.geetsunam.features.presentation.trending.viewmodel.TrendingState
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
class RecommendViewModel @Inject constructor(
    private val getRecommendedSongsUsecase: GetRecommendedSongsUsecase,
    private val saveRecommendedUsecase: SaveRecommendedUsecase,
    private val getLocalRecommendedUsecase: GetLocalRecommendedUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(RecommendState.idle)
    val songState: LiveData<RecommendState> = _liveState

    fun onEvent(event: RecommendEvent) {
        when (event) {
            is RecommendEvent.GetRecommendedSongs -> {
                getSongs(event.token)
            }

            is RecommendEvent.RefreshRecommended -> {
                getSongs(event.token)
//                fetchSongsFromApi(event.token)
            }

            is RecommendEvent.SaveRecommended -> {
                saveRecommended(event.songs)
            }

            is RecommendEvent.Reset -> {
                _liveState.postValue(
                    _liveState.value?.copy(
                        status = RecommendState.RecommendStatus.IDLE,
                        message = "IDLE",
                    )
                )
            }

            else -> {}
        }
    }

    private fun getSongs(token: String) {
        _liveState.postValue(
            _liveState.value?.copy(
                status = RecommendState.RecommendStatus.LOADING,
                message = "Getting recommended songs"
            )
        )
        viewModelScope.launch {
            getLocalRecommendedUsecase.call().collect() {
                if (it.isNotEmpty()) {
                    val songs: MutableList<Song?> = ArrayList()
                    for (s: Recommended in it) {
                        val song = Song(
                            id = s.id,
                            isFavourite = s.isFavourite,
                            isFeatured = s.isFavourite,
                            source = s.source,
                            stream = s.stream,
                            title = s.songName,
                            coverArt = s.coverArt,
                            duration = s.duration,
                            artists = Artist(fullname = s.artistName)
                        )
                        songs.add(song)
                    }
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.SUCCESS,
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
        getRecommendedSongsUsecase.call(CommonRequestModel(token = token)).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.LOADING,
                            message = "Getting songs ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.SUCCESS,
                            message = "success",
                            fromApi = true,
                            songs = result.data?.data
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = RecommendState.RecommendStatus.FAILED, message = result.message
                        )
                    )
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun saveRecommended(songs: List<Song?>?) {
        // Create a coroutine scope with IO dispatcher for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Execute the suspend function within the coroutine scope
                saveRecommendedUsecase.call(songs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}