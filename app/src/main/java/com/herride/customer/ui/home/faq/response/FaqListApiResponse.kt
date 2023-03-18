package com.herride.customer.ui.home.faq.response


import com.google.gson.annotations.SerializedName

data class FaqListApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: List<Payload>,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("help_category_id")
        val helpCategoryId: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String
    )
}