package com.carty.riderapp.ui.home.faq.response


import com.google.gson.annotations.SerializedName

data class FaqSubListApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: List<Payload>,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("help_id")
        val helpId: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("question")
        val question: String
    )
}