package com.carty.riderapp.responsesNew

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookRide(
    /* @SerializedName("code")
     var code: String = "",*/
    @SerializedName("customer_id")
    var customer_id: String = "",
    @SerializedName("card_id")
    var card_id: String = "",
    /*@SerializedName("vehicle_id")
    var vehicle_id: String = "",*/
    @SerializedName("start_address")
    var start_address: String = "",
    @SerializedName("start_latitude")
    var start_latitude: Number = 0,
    @SerializedName("start_longitude")
    var start_longitude: Number = 0,
    @SerializedName("finish_address")
    var finish_address: String = "",
    @SerializedName("finish_latitude")
    var finish_latitude: Number = 0,
    @SerializedName("finish_longitude")
    var finish_longitude: Number = 0
)