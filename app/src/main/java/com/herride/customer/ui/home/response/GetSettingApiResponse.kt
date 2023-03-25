package com.herride.customer.ui.home.response


import com.google.gson.annotations.SerializedName

data class GetSettingApiResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("call_us")
        val callUs: String,
        @SerializedName("company_address")
        val companyAddress: String,
        @SerializedName("company_percentage")
        val companyPercentage: Int,
        @SerializedName("default_currency")
        val defaultCurrency: String,
        @SerializedName("fb_url")
        val fbUrl: String,
        @SerializedName("instagram_url")
        val instagramUrl: String,
        @SerializedName("is_payment_live")
        val isPaymentLive: Boolean,
        @SerializedName("linkedin_url")
        val linkedinUrl: String,
        @SerializedName("support_email")
        val supportEmail: String,
        @SerializedName("tax_percentage")
        val taxPercentage: Int,
        @SerializedName("twitter_url")
        val twitterUrl: String,
        @SerializedName("youtube_url")
        val youtubeUrl: String
    )
}