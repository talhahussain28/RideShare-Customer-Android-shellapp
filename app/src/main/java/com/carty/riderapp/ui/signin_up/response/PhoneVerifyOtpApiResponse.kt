package com.carty.riderapp.ui.signin_up.response


import com.google.gson.annotations.SerializedName

data class PhoneVerifyOtpApiResponse(
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
        @SerializedName("email")
        val email: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("mobile_country_code")
        val mobileCountryCode: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("preferences")
        val preferences: Preferences,
        @SerializedName("profile_picture")
        val profilePicture: String,
        @SerializedName("status")
        val status: String
    ) {
        data class Preferences(
            @SerializedName("accessible_id")
            val accessibleId: String,
            @SerializedName("mode_id")
            val modeId: String,
            @SerializedName("music_id")
            val musicId: String,
            @SerializedName("temperature")
            val temperature: String
        )
    }
}