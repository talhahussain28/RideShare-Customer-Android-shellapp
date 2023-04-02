package com.carty.riderapp.ui.home.model

import com.google.gson.annotations.SerializedName

data class TripStatusTracking(
        @SerializedName("driver_id")
        var driver_id: String = "",
        @SerializedName("status")
        var status: String = ""
)