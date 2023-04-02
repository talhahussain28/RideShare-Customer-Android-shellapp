package com.carty.riderapp.preference
//package com.servicely.customer.repository.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.carty.riderapp.ui.home.setting.response.LanguageListApiResponse

/**
 * This class will help to read/write data into preference.
 */
class SessionManager(context: Context) {

    val sharedPref: SharedPreference = SharedPreference(context)

    // All the pref keys
    object PrefKeys {
        const val IS_LOGIN: String = "IS_LOGIN"
        const val IS_LOGOUTED: String = "IS_LOGOUTED"
        const val LANGUAGE_CODE: String = "LANGUAGE_CODE"
        const val LANGUAGE_ID: String = "LANGUAGE_ID"
       // const val customer_id: String = "customer_id"
        const val userName: String = "userName"
        const val PASSWORD: String = "Password"
        const val email: String = "email"
        const val USER_EMAIL: String = "user_email"
        const val USER_ID: String = "USER_ID"
        const val USER_PROFILE_PIC: String = "user_profile_pic"
        const val SERVICE_CATEGORY_ID: String = "service_category_id"
        const val ADDRESS_ID: String = "ADDRESS_ID"
        const val JOB_ID: String = "JOB_ID"
        const val OFFLINE_DATA: String = "OFFLINE_DATA"
        const val access_token: String =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkNVUzE1ODU2NDg2NzNVVEg1NDg2MSIsIm5hbWUiOiJNZWV0IEFnaGVyYSIsImlhdCI6MTU4NTY0ODg2NiwiZXhwIjoxNTg1NjQ5NDY2fQ.iFfYD8KmUIHg3sBdl1ZnQ38r8sghTgJJ_aJi4_eHZJY"
        const val access_token_master: String = "access_token_master"
        const val companyAddress: String = "companyAddress"
        const val companyPhone: String = "companyPhone"
        const val companyName: String = "companyName"
        const val supportEmail: String = "supportEmail"
        const val currencyCode: String = "currencyCode"
        const val mobile: String = "mobile"
        const val mobile_country_code: String = "mobile_country_code"
        const val profile_photo: String = "profile_photo"
        const val gender: String = "gender"
        const val country_name: String = "country_name"
        const val status: String = "status"
        const val otp: String = "otp"
        const val addressFor: String = "addressFor"
        const val isAddressUpdate: String = "isAddressUpdate"
        const val selectedAddress: String = "selectedAddress"
        const val selectedAddressLat: String = "selectedAddressLat"
        const val selectedAddressLong: String = "selectedAddressLong"
        const val selectedsubCategory: String = "selectedsubCategory"
        const val orderProductImageUrl: String = "orderProductImageUrl"
        const val selectedPaymentType: String = "selectedPaymentType"
        const val vehicleIamge: String = "vehicleIamge"
        const val selectedServiceName: String = "selectedServiceName"
        const val countryId: String = "countryId"
        const val token: String = "token"
        const val refreshToken: String = "refreshToken"
        const val customerSupportEmail: String = "customerSupportEmail"
        const val customerSupportMobile: String = "customerSupportMobile"
        const val driverSupportMobile: String = "driverSupportMobile"
        const val driverSupportEmail: String = "driverSupportEmail"
        const val defaultCurrency: String = "defaultCurrency"
        const val nearestDriverRadious: String = "nearestDriverRadious"
        const val deviceId: String = "deviceId"
        const val deviceName: String = "deviceName"
        const val deviceToken: String = "deviceToken"
        const val isNewData: String = "isNewData"
        const val braintree_customer_token: String = "braintree_customer_token"
        const val paypal_customer_id: String = "paypal_customer_id"
        const val bank_info_id: String = "bank_info_id"
        const val bank_holder_name: String = "bank_holder_name"
        const val bank_AC_NO: String = "bank_AC_NO"
        const val paypalTransactionNonce: String = "paypalTransactionNonce"
        const val estimatedDiatance: String = "estimatedDiatance"
        const val estimatedDuration: String = "estimatedDuration"
        const val isWheelChair: String = "isWheelChair"

        const val USER_REMENBER_EMAIL: String = "user_remember_email"
        const val USER_REMENBER_PASSWORD: String = "user_remember_password"
        const val mode_id: String = "mode_id"
        const val music_id: String = "music_id"
        const val accessible_id: String = "accessible_id"
        const val temperature: String = "temperature"
        const val TERMS_CONDITIONS: String = "TERMS_CONDITIONS"
        const val PRIVACY_POLICY: String = "PRIVACY_POLICY"
        const val call_us: String = "call_us"
        const val support_email: String = "support_email"
        const val availability_status: String = "availability_status"
        const val one_signal_player_id: String = "one_signal_player_id"
        const val fcm_token: String = "fcm_token"
    }


    var fcm_token: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.fcm_token)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.fcm_token, value)
        }

    var one_signal_player_id: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.one_signal_player_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.one_signal_player_id, value)
        }

    var availability_status: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.availability_status)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.availability_status, value)
        }

    var support_email: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.support_email)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.support_email, value)
        }

    var call_us: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.call_us)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.call_us, value)
        }

    var PRIVACY_POLICY: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.PRIVACY_POLICY)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.PRIVACY_POLICY, value)
        }
    var TERMS_CONDITIONS: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.TERMS_CONDITIONS)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.TERMS_CONDITIONS, value)
        }
    var temperature: String = "0"
        get() {
            return sharedPref.getValueString(PrefKeys.temperature)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.temperature, value)
        }
    var accessible_id: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.accessible_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.accessible_id, value)
        }

    var music_id: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.music_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.music_id, value)
        }
    var mode_id: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.mode_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.mode_id, value)
        }
    var USER_REMENBER_PASSWORD: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.USER_REMENBER_PASSWORD)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.USER_REMENBER_PASSWORD, value)
        }

    var USER_REMENBER_EMAIL: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.USER_REMENBER_EMAIL)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.USER_REMENBER_EMAIL, value)
        }

    var estimatedDuration: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.estimatedDuration)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.estimatedDuration, value)
        }

    var estimatedDiatance: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.estimatedDiatance)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.estimatedDiatance, value)
        }

    var paypalTransactionNonce: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.paypalTransactionNonce)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.paypalTransactionNonce, value)
        }

    var bank_AC_NO: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.bank_AC_NO)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.bank_AC_NO, value)
        }

    var bank_holder_name: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.bank_holder_name)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.bank_holder_name, value)
        }

    var bank_info_id: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.bank_info_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.bank_info_id, value)
        }

    var paypal_customer_id: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.paypal_customer_id)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.paypal_customer_id, value)
        }

    var braintree_customer_token: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.braintree_customer_token)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.braintree_customer_token, value)
        }

    var isNewData: Boolean = false
        get() {
            return sharedPref.getValueBoolien(PrefKeys.isNewData,false)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.isNewData, value)
        }

    var deviceToken: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.deviceToken)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.deviceToken, value)
        }

    var deviceName: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.deviceName)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.deviceName, value)
        }

    var deviceId: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.deviceId)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.deviceId, value)
        }

    var nearestDriverRadious: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.nearestDriverRadious)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.nearestDriverRadious, value)
        }

    var defaultCurrency: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.defaultCurrency)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.defaultCurrency, value)
        }

    var driverSupportEmail: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.driverSupportEmail)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.driverSupportEmail, value)
        }

    var driverSupportMobile: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.driverSupportMobile)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.driverSupportMobile, value)
        }

    var customerSupportMobile: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.customerSupportMobile)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.customerSupportMobile, value)
        }

    var customerSupportEmail: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.customerSupportEmail)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.customerSupportEmail, value)
        }

    var refreshToken: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.refreshToken)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.refreshToken, value)
        }


    var token: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.token)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.token, value)
        }

    var countryId: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.countryId)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.countryId, value)
        }

    var selectedServiceName: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.selectedServiceName)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedServiceName, value)
        }

    var vehicleIamge: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.vehicleIamge)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.vehicleIamge, value)
        }

    var selectedPaymentType: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.selectedPaymentType)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedPaymentType, value)
        }

    var orderProductImageUrl: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.orderProductImageUrl)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.orderProductImageUrl, value)
        }
    var selectedsubCategory: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.selectedsubCategory)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedsubCategory, value)
        }

    var selectedAddressLong: String = "0.0"
        get() {
            return sharedPref.getValueString(PrefKeys.selectedAddressLong)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedAddressLong, value)
        }
    var selectedAddressLat: String = "0.0"
        get() {
            return sharedPref.getValueString(PrefKeys.selectedAddressLat)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedAddressLat, value)
        }

    var selectedAddress: String = "Address"
        get() {
            return sharedPref.getValueString(PrefKeys.selectedAddress)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.selectedAddress, value)
        }
    var isAddressUpdate: Boolean = false
        get() {
            return sharedPref.getValueBoolien(PrefKeys.isAddressUpdate, false)
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.isAddressUpdate, value)
        }
    var addressFor: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.addressFor)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.addressFor, value)
        }

    var otp: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.otp)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.otp, value)
        }

    var status: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.status)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.status, value)
        }

    var country_name: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.country_name)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.country_name, value)
        }

    var gender: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.gender)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.gender, value)
        }

    var profile_photo: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.profile_photo)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.profile_photo, value)
        }

    var mobile_country_code: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.mobile_country_code)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.mobile_country_code, value)
        }

    var mobile: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.mobile)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.mobile, value)
        }

    var currencyCode: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.currencyCode)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.currencyCode, value)
        }


    var companyName: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.companyName)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.companyName, value)
        }

    var companyPhone: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.companyPhone)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.companyPhone, value)
        }
    var companyAddress: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.companyAddress)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.companyAddress, value)
        }

    var offlineData: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.OFFLINE_DATA)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.OFFLINE_DATA, value)
        }

    var isWheelChair: Boolean = false
        get() {
            return sharedPref.getValueBoolien(PrefKeys.isWheelChair, false)
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.isWheelChair, value)
        }

    var IS_LOGOUTED: Boolean = false
        get() {
            return sharedPref.getValueBoolien(PrefKeys.IS_LOGOUTED, false)
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.IS_LOGOUTED, value)
        }

    var isUserLogin: Boolean = false
        get() {
            return sharedPref.getValueBoolien(PrefKeys.IS_LOGIN, false)
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.IS_LOGIN, value)
        }

    var LANGUAGE_ID: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.LANGUAGE_ID)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.LANGUAGE_ID, value)
        }

    var languageCode: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.LANGUAGE_CODE)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.LANGUAGE_CODE, value)
        }

//    var customer_id: String = ""
//        get() {
//            return sharedPref.getValueString(PrefKeys.customer_id)!!
//        }
//        set(value) {
//            field = value
//            sharedPref.save(PrefKeys.customer_id, value)
//        }

    var userName: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.userName)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.userName, value)
        }
    var password: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.PASSWORD)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.PASSWORD, value)
        }

    var profilePicUrl: String = "ProfilePic"
        get() {
            return sharedPref.getValueString(PrefKeys.USER_PROFILE_PIC)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.USER_PROFILE_PIC, value)
        }

    var USER_ID: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.USER_ID)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.USER_ID, value)
        }
    var email: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.USER_EMAIL)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.USER_EMAIL, value)
        }
    var serviceCategoryId: String = "SEC1580979143355062120"
        get() {
            return sharedPref.getValueString(PrefKeys.SERVICE_CATEGORY_ID)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.SERVICE_CATEGORY_ID, value)
        }

    var addressId: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.ADDRESS_ID)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.ADDRESS_ID, value)
        }

    var jobId: String = ""
        get() {
            return sharedPref.getValueString(PrefKeys.JOB_ID)!!
        }
        set(value) {
            field = value
            sharedPref.save(PrefKeys.JOB_ID, value)
        }

///////////////////////////////////////////////////////////////////////////////

    val prefName: String = "just_her_rideshare_pref"
    val labelPrefName: String = "label_just_her_rideshare_pref"
    private var PRIVATE_MODE = 0

    private var langPref: SharedPreferences =
            context.getSharedPreferences( labelPrefName, PRIVATE_MODE)

    fun getLabel(key: String): String {
        return langPref.getString(key, "").toString()
    }


    @SuppressLint("CommitPrefEdits")
    fun initLabel(labelList: List<LanguageListApiResponse.Payload>) {

        val edit = langPref.edit()
//        for (label in labelList) {
//            Log.e(
//                    "languagelabel",
//                    "languagelabel = public static final String " + label.code + " = \"" + label.code + "\"" + ";"
//            )
//            edit.putString(label.code, label.value)
//        }

        edit.apply()
    }

}