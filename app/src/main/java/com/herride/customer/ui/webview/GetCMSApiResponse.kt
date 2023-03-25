package com.herride.customer.ui.webview


import com.google.gson.annotations.SerializedName

data class GetCMSApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: List<Payload>,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("cms_id")
        val cmsId: String,
        @SerializedName("code")
        val code: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String
    )
}