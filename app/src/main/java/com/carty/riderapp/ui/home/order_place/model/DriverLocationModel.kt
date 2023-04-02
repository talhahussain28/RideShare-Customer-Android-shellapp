package com.carty.riderapp.ui.home.order_place.model

import com.google.gson.annotations.SerializedName

data class DriverLocationModel(
        @SerializedName("driverId")
        var driverId: String = "",
        @SerializedName("latitude")
        val latitude: Double = 0.0,
        @SerializedName("longitude")
        val longitude: Double = 0.0,
        @SerializedName("bearing")
        val bearing: Float = 0f,
        @SerializedName("status")
        val status: String = ""
)