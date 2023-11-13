package com.example.geetsunam.utils.models

data class QueryRequestModel(
    val query: String? = null,
    val token: String? = null,
    val page: Int? = null,
    val limit: Int? = null,
)