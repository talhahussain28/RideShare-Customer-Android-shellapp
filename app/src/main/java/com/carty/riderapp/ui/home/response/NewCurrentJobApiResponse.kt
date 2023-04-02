package com.carty.riderapp.ui.home.response


import com.google.gson.annotations.SerializedName

data class NewCurrentJobApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("customer_id")
        val customerId: String,
        @SerializedName("default_currency")
        val defaultCurrency: String,
        @SerializedName("driver")
        val driver: Driver,
        @SerializedName("driver_id")
        val driverId: String,
        @SerializedName("finish_address")
        val finishAddress: String,
        @SerializedName("finish_latitude")
        val finishLatitude: Double,
        @SerializedName("finish_longitude")
        val finishLongitude: Double,
        @SerializedName("formatted_distance")
        val formattedDistance: String,
        @SerializedName("formatted_duration")
        val formattedDuration: String,
        @SerializedName("start_address")
        val startAddress: String,
        @SerializedName("start_latitude")
        val startLatitude: Double,
        @SerializedName("start_longitude")
        val startLongitude: Double,
        @SerializedName("status")
        val status: String,
        @SerializedName("total")
        val total: Double,
        @SerializedName("trip_id")
        val tripId: String,
        @SerializedName("vehicle_id")
        val vehicleId: String
    ) {
        data class Driver(
            @SerializedName("mobile")
            val mobile: String,
            @SerializedName("mobile_country_code")
            val mobileCountryCode: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("profile_picture")
            val profilePicture: String,
            @SerializedName("rating")
            val rating: String,
            @SerializedName("vehicle_model")
            val vehicleModel: String,
            @SerializedName("vehicle_name")
            val vehicleName: String,
            @SerializedName("vehicle_plate_number")
            val vehiclePlateNumber: String
        )
    }
}