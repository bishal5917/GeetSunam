package com.example.geetsunam.utils.models

data class QueryRequestModel(
    val number: String = "8",
    val offset: Int = 0,
    val diet: String? = null,
    val type: String? = null,
    val addRecipeInformation: Boolean? = null,
    val query: String? = null,
)