package com.carty.riderapp.ui.signin_up.response


import com.google.gson.annotations.SerializedName

data class EmailMObileExistApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("otp")
        val otp: Int,
        @SerializedName("verify_mobile_id")
        val verifyMobileId: String
    )
}