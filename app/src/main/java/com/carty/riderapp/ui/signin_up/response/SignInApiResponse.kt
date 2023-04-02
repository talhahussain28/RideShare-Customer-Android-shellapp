package com.carty.riderapp.ui.signin_up.response


import com.google.gson.annotations.SerializedName

data class SignInApiResponse(
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
        @SerializedName("otp")
        val otp: Int
    )
}