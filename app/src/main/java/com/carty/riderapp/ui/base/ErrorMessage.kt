package com.carty.riderapp.ui.base


import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("error")
    val error: Error,
    @SerializedName("payload")
    val payload: Any,
    @SerializedName("status")
    val status: Int
) {
    data class Error(
        @SerializedName("error_type")
        val errorType: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("statusCode")
        val statusCode: Int
    )
}