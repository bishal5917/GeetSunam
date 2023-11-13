package com.example.geetsunam.features.presentation.home.search.viewmodel

import com.example.geetsunam.utils.models.QueryRequestModel

sealed class SearchEvent {
    data class Search(val queryRequestModel: QueryRequestModel) : SearchEvent()
}