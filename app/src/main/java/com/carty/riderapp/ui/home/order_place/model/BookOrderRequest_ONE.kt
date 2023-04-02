package com.carty.riderapp.ui.home.order_place.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookOrderRequest_ONE(
        @SerializedName("code")
        var code: String = "",
        @SerializedName("customer_id")
        var customer_id: String = "",
        @SerializedName("card_id")
        var card_id: String = "",
        @SerializedName("vehicle_id")
        var vehicle_id: String = "",
        @SerializedName("start_address")
        var start_address: String = "",
        @SerializedName("start_latitude")
        var start_latitude: String = "0",
        @SerializedName("start_longitude")
        var start_longitude: String = "0",
        @SerializedName("finish_address")
        var finish_address: String = "",
        @SerializedName("finish_latitude")
        var finish_latitude: String = "0",
        @SerializedName("finish_longitude")
        var finish_longitude: String = "0",
        @SerializedName("total_distance")
        var total_distance: String = "0",
        @SerializedName("total_duration")
        var total_duration: String = "0",
        @SerializedName("formatted_distance")
        var formatted_distance: String = "",
        @SerializedName("formatted_duration")
        var formatted_duration: String = "",
        @SerializedName("total")
        var total: String = "0",
        @SerializedName("preferences")
        var mPreferences: preferences = preferences(),
        @SerializedName("fare_info")
        var fare_info: FareInfo = FareInfo()
) {
    @Keep
    data class preferences(
            @SerializedName("accessible_id")
            var accessible_id: String = "",
            @SerializedName("mode_id")
            var mode_id: String = "",
            @SerializedName("music_id")
            var music_id: String = "",
            @SerializedName("temperature")
            var temperature: String = "0"
    )

    data class FareInfo(
            @SerializedName("base_fare")
            var baseFare: String = "0",
            @SerializedName("distance_fare")
            var distanceFare: String = "0",
            @SerializedName("duration_fare")
            var durationFare: String = "0"
    )

}

////////////////////////////////////////////////////////////////////////////////////////


//import com.google.gson.annotations.SerializedName
//import androidx.annotation.Keep
//
//@Keep
//data class BookOrderRequest(
//        @SerializedName("customer_id")
//        var customerId: String = "",
//        @SerializedName("drop_of_info")
//        var dropOfInfo: DropOfInfo = DropOfInfo(),
//        @SerializedName("bank_info")
//        var bankInfo: BankInfo = BankInfo(),
//        @SerializedName("order_photo")
//        var order_photo: ArrayList<String> = arrayListOf<String>(),
//        @SerializedName("payment_type_id")
//        var paymentTypeId: String = "",
//        @SerializedName("pick_up_date")
//        var pickUpDate: String = "",
//        @SerializedName("pick_up_info")
//        var pickUpInfo: PickUpInfo = PickUpInfo(),
//        @SerializedName("tax_info")
//        var taxInfo: List<TaxInfo> = listOf(),
//        @SerializedName("pick_up_time")
//        var pickUpTime: String = "",
//        @SerializedName("drop_off_date")
//        var dropOffDate: String = "",
//        @SerializedName("drop_off_time")
//        var dropOffTime: String = "",
//        @SerializedName("service_category_id")
//        var serviceCategoryId: String = "",
//        @SerializedName("vehicle_id")
//        var vehicleId: String = "",
//        @SerializedName("coupon_id")
//        var couponId: String = "",
//        @SerializedName("coupon_code")
//        var couponCode: String = "",
//        @SerializedName("discount_type")
//        var discountType: String = "",
//        @SerializedName("discount_value")
//        var discountValue: Number = 0,
//        @SerializedName("discount")
//        var discount: Number = 0,
//        @SerializedName("total_payable")
//        var totalPayable: Number = 0,
//        @SerializedName("sub_total")
//        var subTotal: Number = 0,
//        @SerializedName("quantity")
//        var quantity: Number = 1,
//        @SerializedName("paymentMethodNonce")
//        var paymentMethodNonce: String = "",
//        @SerializedName("package_info")
//        var packageInfo: List<PackageInfo> = listOf(),
//        @SerializedName("distance_info")
//        var distanceInfo: DistanceInfo = DistanceInfo()
//)
//{
//
//
//    @Keep
//    data class DistanceInfo(
//            @SerializedName("distance_value")
//            var distanceValue: String = "",
//            @SerializedName("duration_value")
//            var durationValue: String = "",
//            @SerializedName("duration")
//            var duration: String = "",
//            @SerializedName("distance")
//            var distance: String = ""
//    )
//
//    @Keep
//    data class PackageInfo(
//            @SerializedName("attribute_info")
//            var attributeInfo: List<AttributeInfo> = listOf()
//    ) {
//        @Keep
//        data class AttributeInfo(
//                @SerializedName("attribute_id")
//                var attributeId: String = "",
//                @SerializedName("attribute_option_id")
//                var attributeOptionId: String = "",
//                @SerializedName("attribute_option_value")
//                var attributeOptionValue: String = "",
//                @SerializedName("attribute_value")
//                var attributeValue: Double = 0.0,
//                var attributeName: String
//        )
//    }
//
//    @Keep
//    data class BankInfo(
//            @SerializedName("bank_name")
//            var bankName: String = "",
//            @SerializedName("bank_accout_number")
//            var bankAccoutNumber: Number = 0,
//            @SerializedName("bank_ifsc_code")
//            var bankIfscCode: String = ""
//    )
//
//    @Keep
//    data class DropOfInfo(
//            @SerializedName("drop_of_address")
//            var dropOfAddress: String = "",
//            @SerializedName("drop_of_instructions")
//            var dropOfInstructions: String = "",
//            @SerializedName("drop_of_latitude")
//            var dropOfLatitude: Double = 0.0,
//            @SerializedName("drop_of_longitude")
//            var dropOfLongitude: Double = 0.0,
//            @SerializedName("mobile")
//            var mobile: Number = 0,
//            @SerializedName("mobile_country_code")
//            var mobileCountryCode: String = "0",
//            @SerializedName("name")
//            var name: String = ""
//    )
//
//    @Keep
//    data class PickUpInfo(
//            @SerializedName("mobile")
//            var mobile: Number = 0,
//            @SerializedName("mobile_country_code")
//            var mobileCountryCode: String = "0",
//            @SerializedName("name")
//            var name: String = "",
//            @SerializedName("pick_up_address")
//            var pickUpAddress: String = "",
//            @SerializedName("pick_up_instructions")
//            var pickUpInstructions: String = "",
//            @SerializedName("pick_up_latitude")
//            var pickUpLatitude: Double = 0.0,
//            @SerializedName("pick_up_longitude")
//            var pickUpLongitude: Double = 0.0
//    )
//
//    @Keep
//    data class TaxInfo(
//            @SerializedName("title")
//            var title: String = "",
//            @SerializedName("percentage")
//            var percentage: Number = 0,
//            @SerializedName("tax_id")
//            var taxId: String = "",
//            @SerializedName("taxValue")
//            var taxValue: Number = 0
//    )
//}


////////////////////////////////////////////////////////////////////////////////////////
//package com.acargodemo.customer.view.fragment.review_order.filter
//
//
//import com.google.gson.annotations.SerializedName
//import androidx.annotation.Keep
//
//@Keep
//data class BookOrderRequest(
//    @SerializedName("attribute_info")
//    var attributeInfo: List<AttributeInfo> = listOf(),
//    @SerializedName("customer_id")
//    var customerId: String = "",
//    @SerializedName("drop_of_info")
//    var dropOfInfo: DropOfInfo = DropOfInfo(),
//    @SerializedName("bank_info")
//    var bankInfo: BankInfo = BankInfo(),
//    @SerializedName("order_photo")
//    var order_photo: ArrayList<String> = arrayListOf<String>(),
//    @SerializedName("payment_type_id")
//    var paymentTypeId: String = "",
//    @SerializedName("pick_up_date")
//    var pickUpDate: String = "",
//    @SerializedName("pick_up_info")
//    var pickUpInfo: PickUpInfo = PickUpInfo(),
//    @SerializedName("tax_info")
//    var taxInfo: List<TaxInfo> = listOf(),
//    @SerializedName("pick_up_time")
//    var pickUpTime: String = "",
//    @SerializedName("drop_off_date")
//    var dropOffDate: String = "",
//    @SerializedName("drop_off_time")
//    var dropOffTime: String = "",
//    @SerializedName("service_category_id")
//    var serviceCategoryId: String = "",
//    @SerializedName("vehicle_id")
//    var vehicleId: String = "",
//    @SerializedName("coupon_id")
//    var couponId: String = "",
//    @SerializedName("coupon_code")
//    var couponCode: String = "",
//    @SerializedName("discount_type")
//    var discountType: String = "",
//    @SerializedName("discount_value")
//    var discountValue: Number = 0,
//    @SerializedName("discount")
//    var discount: Number = 0,
//    @SerializedName("total_payable")
//    var totalPayable: Number = 0,
//    @SerializedName("sub_total")
//    var subTotal: Number = 0,
//    @SerializedName("quantity")
//    var quantity: Number = 1,
//    @SerializedName("paymentMethodNonce")
//    var paymentMethodNonce: String = ""
//) {
//    @Keep
//    data class AttributeInfo(
//        @SerializedName("attribute_id")
//        var attributeId: String = "",
//        @SerializedName("attribute_option_id")
//        var attributeOptionId: String = "",
//        @SerializedName("attribute_option_value")
//        var attributeOptionValue: String = "",
//        @SerializedName("attribute_value")
//        var attributeValue: Double = 0.0,
//        var attributeName: String
//    )
//
//    @Keep
//    data class BankInfo(
//        @SerializedName("bank_name")
//        var bankName: String = "",
//        @SerializedName("bank_accout_number")
//        var bankAccoutNumber: Number = 0,
//        @SerializedName("bank_ifsc_code")
//        var bankIfscCode: String = ""
//    )
//
//    @Keep
//    data class DropOfInfo(
//        @SerializedName("drop_of_address")
//        var dropOfAddress: String = "",
//        @SerializedName("drop_of_instructions")
//        var dropOfInstructions: String = "",
//        @SerializedName("drop_of_latitude")
//        var dropOfLatitude: Double = 0.0,
//        @SerializedName("drop_of_longitude")
//        var dropOfLongitude: Double = 0.0,
//        @SerializedName("mobile")
////        var mobile: String = "",
//        var mobile: Number = 0,
//        @SerializedName("mobile_country_code")
//        var mobileCountryCode: String = "",
//        @SerializedName("name")
//        var name: String = ""
//    )
//
//    @Keep
//    data class PickUpInfo(
//        @SerializedName("mobile")
////        var mobile: String = "",
//        var mobile: Number = 0,
//        @SerializedName("mobile_country_code")
//        var mobileCountryCode: String = "",
//        @SerializedName("name")
//        var name: String = "",
//        @SerializedName("pick_up_address")
//        var pickUpAddress: String = "",
//        @SerializedName("pick_up_instructions")
//        var pickUpInstructions: String = "",
//        @SerializedName("pick_up_latitude")
//        var pickUpLatitude: Double = 0.0,
//        @SerializedName("pick_up_longitude")
//        var pickUpLongitude: Double = 0.0
//    )
//
//    @Keep
//    data class TaxInfo(
//        @SerializedName("title")
//        var title: String = "",
//        @SerializedName("percentage")
//        var percentage: Number = 0,
//        @SerializedName("tax_id")
//        var taxId: String = "",
//        @SerializedName("taxValue")
//        var taxValue: Number = 0
//    )
//}