package com.herride.customer.ui.signin_up.response


import com.google.gson.annotations.SerializedName

data class ResendOtpApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("otp")
        val otp: Int
    )
}