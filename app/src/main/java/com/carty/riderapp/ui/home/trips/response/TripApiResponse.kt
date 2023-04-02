package com.carty.riderapp.ui.home.trips.response


import com.google.gson.annotations.SerializedName

data class TripApiResponse(
        @SerializedName("error")
    val error: Any,
        @SerializedName("payload")
    val payload: List<Payload>,
        @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("default_currency")
        val defaultCurrency: String,
        @SerializedName("finish_address")
        val finishAddress: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("profile_picture")
        val profilePicture: String,
        @SerializedName("start_address")
        val startAddress: String,
        @SerializedName("total")
        val total: Double,
        @SerializedName("trip_id")
        val tripId: String
    )
}