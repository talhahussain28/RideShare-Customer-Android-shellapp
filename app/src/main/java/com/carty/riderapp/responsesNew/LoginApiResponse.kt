package com.carty.riderapp.responsesNew

data class LoginApiResponse(
    val error : String,
    val payload : Payloads,
    val status : Int
)

data class Payloads (
    val otp : Int,
    val customer_id : String
)