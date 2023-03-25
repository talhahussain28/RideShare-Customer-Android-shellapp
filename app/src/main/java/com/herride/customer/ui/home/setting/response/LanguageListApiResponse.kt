package com.herride.customer.ui.home.setting.response


import com.google.gson.annotations.SerializedName

data class LanguageListApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: List<Payload>,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("code")
        val code: String,
        @SerializedName("language_id")
        val languageId: String,
        @SerializedName("title")
        val title: String
    )
}