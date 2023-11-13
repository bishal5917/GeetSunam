package com.example.geetsunam.utils

import com.example.geetsunam.utils.models.QueryRequestModel

object GetQuery {
    fun getQueryMap(queryRequestModel: QueryRequestModel): Map<String, Any> {
        val queryMap = mutableMapOf<String, Any>()
        if (!queryRequestModel.query.isNullOrEmpty()) {
            queryMap["query"] = queryRequestModel.query
        }
        return queryMap;
    }
}