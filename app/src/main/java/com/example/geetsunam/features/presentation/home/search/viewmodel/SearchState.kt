package com.example.geetsunam.features.presentation.home.search.viewmodel

import com.example.geetsunam.features.data.models.search.SearchResponseModel

data class SearchState(
    val status: SearchStatus,
    val message: String? = null,
    val searchData: SearchResponseModel.Data.Search? = null
) {
    companion object {
        val idle = SearchState(SearchStatus.IDLE, message = "")
    }

    enum class SearchStatus {
        IDLE, LOADING, SUCCESS, FAILED
    }
}