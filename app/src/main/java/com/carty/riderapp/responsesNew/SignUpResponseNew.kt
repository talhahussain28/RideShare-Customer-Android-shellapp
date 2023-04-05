package com.carty.riderapp.responsesNew

data class SignUpResponseNew(
    val error: String,
    val payload: Payload,
    val status: Int
)

data class Payload(

    val preferences: Preferences,
    val customer_id: String,
    val name: String,
    val email: String,
    val mobile_country_code: Int,
    val mobile: String,
    val profile_picture: String,
    val gender: String,
    val state_id: String,
    val city_id: String,
    val status: String,
    val is_push: Boolean,
    val is_email: Boolean,
    val state_name: String,
    val city_name: String
)

data class Preferences(

    val mode_id: String,
    val music_id: String,
    val accessible_id: String,
    val temperature: String
)