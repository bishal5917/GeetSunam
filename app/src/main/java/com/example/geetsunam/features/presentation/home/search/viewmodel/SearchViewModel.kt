package com.example.geetsunam.features.presentation.home.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geetsunam.features.domain.usecases.SearchUsecase
import com.example.geetsunam.utils.Resource
import com.example.geetsunam.utils.models.QueryRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsecase: SearchUsecase
) : ViewModel() {
    private val _liveState = MutableLiveData(SearchState.idle)
    val searchState: LiveData<SearchState> = _liveState

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Search -> {
                searchGlobally(event.queryRequestModel)
            }

            else -> {}
        }
    }

    private fun searchGlobally(queryRequestModel: QueryRequestModel) {
        searchUsecase.call(queryRequestModel).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SearchState.SearchStatus.LOADING,
                            message = "Searching,Please wait ..."
                        )
                    )
                }

                is Resource.Success -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SearchState.SearchStatus.SUCCESS,
                            message = "success",
                            searchData = result.data?.data?.search
                        )
                    )
                }

                is Resource.Error -> {
                    _liveState.postValue(
                        _liveState.value?.copy(
                            status = SearchState.SearchStatus.FAILED, message = result.message
                        )
                    )
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}