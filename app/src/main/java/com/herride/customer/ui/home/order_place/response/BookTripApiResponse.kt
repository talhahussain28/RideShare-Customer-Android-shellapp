package com.herride.customer.ui.home.order_place.response


import com.google.gson.annotations.SerializedName

data class BookTripApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("status")
        val status: String,
        @SerializedName("trip_id")
        val tripId: String
    )
}