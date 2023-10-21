package com.example.geetsunam.utils

import com.example.geetsunam.utils.models.QueryRequestModel

object GetQuery {
    fun getQueryMap(queryRequestModel: QueryRequestModel): Map<String, Any> {
        val queryMap = mutableMapOf<String, Any>(
            "number" to queryRequestModel.number, "offset" to queryRequestModel.offset
        )
        if (queryRequestModel.diet != null) {
            queryMap["diet"] = queryRequestModel.diet
        }
        if (!queryRequestModel.query.isNullOrEmpty()) {
            queryMap["query"] = queryRequestModel.query
        }
        if (queryRequestModel.diet == null) {
            queryMap["diet"] = "gluten free"
        }
        if (queryRequestModel.type != null) {
            queryMap["type"] = queryRequestModel.type
        }
        if (queryRequestModel.type == null) {
            queryMap["type"] = "main course"
        }
        if (queryRequestModel.addRecipeInformation != null) {
            queryMap["addRecipeInformation"] = queryRequestModel.addRecipeInformation
        }
        return queryMap;
    }
}