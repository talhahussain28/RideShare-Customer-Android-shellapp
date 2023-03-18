package com.herride.customer.ui.home.response


import com.google.gson.annotations.SerializedName

data class ReceiptApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("base_fare")
        val baseFare: Double,
        @SerializedName("distance_fare")
        val distanceFare: Double,
        @SerializedName("duration_fare")
        val durationFare: Double,
        @SerializedName("formatted_distance")
        val formattedDistance: String,
        @SerializedName("formatted_duration")
        val formattedDuration: String,
        @SerializedName("total")
        val total: Double,
        @SerializedName("trip_date")
        val tripDate: String,
        @SerializedName("trip_id")
        val tripId: String,
        @SerializedName("status")
        val status: String
    )
}