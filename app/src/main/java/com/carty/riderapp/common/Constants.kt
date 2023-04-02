package com.carty.riderapp.common


object Constants {


//    const val baseURL = "http://api.acargolb.crypticpoint.com/"
//    const val baseURL = "http://api.justherrideshare.crypticpoint.com/api/"
    //const val baseURL = "http://api.justherrideshare.crypticpoint.com/"
    const val baseURL = "http://54.157.133.185:3000/"
    const val android = "android"

    const val access_token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkNVUzE1ODU2NDg2NzNVVEg1NDg2MSIsIm5hbWUiOiJNZWV0IEFnaGVyYSIsImlhdCI6MTU4NTY0ODg2NiwiZXhwIjoxNTg1NjQ5NDY2fQ.iFfYD8KmUIHg3sBdl1ZnQ38r8sghTgJJ_aJi4_eHZJY"

    const val currency_symbol = "$"
//    const val baseURL = "http://192.168.1.48:9052/"




    object INSTANCE {
        const val SPLASH_DELAY = 3000
        const val BASE_URL = "https://api.nytimes.com/svc/"
        const val NEWS_ITEM_KEY = "NEWS_ITEM_KEY"
    }


    const val SUCCESS = "SUCCESS"
    const val ERROR = "ERROR"

    const val logout = "logout"

    var start_address: String = ""
    var finish_address: String = ""
    var start_latitude: String = ""
    var start_longitude: String = ""
    var finish_latitude: String = ""
    var finish_longitude: String = ""

    var PRIVACY_POLICY: String = "PRIVACY_POLICY"
    var TERMS_CONDITIONS: String = "TERMS_CONDITIONS"


    const val CAMERA_REQUEST = 101
    const val LOCATION_REQUEST = 102
    const val STORAGE_REQUEST = 103
    const val ADDRESSDROPOFF = 40
    const val PAYMENT_SELECTION = 140
    const val PAYMENT_CARD_SELECTION = 141
    const val PAYMENT_CASH_SELECTION = 142
    const val ADDRESSPICKUP = 50
    const val PREF_NAME: String = "PREF_NAME"
    const val LABEL_PREFEREANCE_NAME: String = "LABEL_PREFEREANCE_NAME"

    //    const val OTP_COUNTDOWN_TIMER: Long = 60000
    const val OTP_COUNTDOWN_TIMER: Long = 10000



    const val FROM_NAVIGATION = "FROM_NAVIGATION"
    const val FROM_ACCOUNT = "FROM_ACCOUNT"
    const val FROM_FAVOURITE = "FROM_FAVOURITE"
    const val FROM_CHANGE_NUMBER = "FROM_CHANGE_NUMBER"
    const val SOS_NUMBER = "911"

    const val trips = "trips"
    const val drivers = "drivers"

    const val get = "get"
    const val add = "add"
    const val selected = "selected"
    const val remove = "remove"
    const val delete = "delete"
    const val none = "none"
    const val monthly = "monthly"
    const val yearly = "yearly"
    const val home = "home"
    const val work = "work"
    const val other = "other"
    const val dispute = "dispute"

    const val MasterCard = "MasterCard"
    const val Visa = "Visa"
    const val Discover = "Discover"
    const val American_Express = "American Express"



    const val CMS_TERMS = "TERMS"
    const val CMS_PRIVACY_POLICY = "PRIVACY_POLICY"

    const val NEW = "new"
    const val PENDING = "pending"
    const val ACCEPTED = "accepted"
    const val STARTED = "started"
    const val COMPLETED = "completed"
    const val PICKED_UP = "pickedup"
    const val DELIVERED = "delivered"
    const val CANCELLED = "cancelled"


    const val action_accept = "accept"
    const val action_reject = "reject"
    const val action_pickup = "pickup"
    const val action_start = "start"
    const val action_complete = "complete"


    const val trip_completed = "completed"
    const val trip_canceled = "canceled"

    const val TIMEMIN = "#TIMEMIN#"
    const val TIMEMAX = "#TIMEMAX#"

    const val driverInfo = "driverInfo"
    const val customerInfo = "customerInfo"


    const val No_OTP = "No_OTP"

    // todo firebase connection - items
    const val driverOrder = "driverOrder"
    const val orders = "orders"
    const val user_session = "user_session"
    const val devices = "devices"
    const val devices_Cusomer = "Cusomer"
    const val active = "active"
    const val inactive = "inactive"


    const val New = "New"
    const val Accepted = "Accepted"
    const val InProgress = "InProgress"
    const val Complete = "Complete"
    const val Cancel = "Cancel"
    const val provider_cancel = "provider_cancel"

    const val session_Jobs = "Jobs"
    const val STATUS_Complete: String = "Complete"
    const val STATUS_InProgress: String = "InProgress"
    const val STATUS_Accepted: String = "Accepted"
    const val STATUS_provider_cancel: String = "provider_cancel"

    const val location = "location"
    const val refresh = "refresh"
    const val location_from_map = "location_from_map"
    const val serviceCategorySelected = "serviceCategorySelected"

    const val Register = "Register"
    const val Login = "Login"

    const val from = "from"
    const val actionOnAddress = "actionOnAddress"
    const val edit = "edit"
    const val default = "default"
    const val add_address = "add_address"
    const val address = "address"
    const val fav_id = "fav_id"
    const val latitude = "latitude"
    const val longitude = "longitude"
    const val vehicle_type_id = "vehicle_type_id"
    const val pickUp = "pickUp"
    const val stop = "stop"
    const val Now = "Now"
    const val dropOff = "dropOff"
    const val addressTitle = "addressTitle"
    const val addressForStore = "addressForStore"
    const val placeId = "placeId"
    const val latForStore = "latForStore"
    const val longForStore = "longForStore"
    const val selectedsubCategory = "selectedsubCategory"

    const val selectedServiceId = "selectedServiceId"
    const val selectedServiceName = "selectedServiceName"
    const val selectedServiceSubCategory = "selectedServiceSubCategory"

    const val order_photo = "order_photo"


//    const val Bank = "Bank"
    const val Bank = "BY_TRANSFER"
    const val CreditCard = "CARD"
    const val CASH = "CASH"
    const val currency_us = "$"


    val YYYY_MM_dd = "yyyy-MM-dd"
    val MMM_dd_yyyy = "MMM dd, yyyy"
    val E_dd_MMM_yyyy = "E, dd MMM yyyy"

    const val customer = "customer"
    var provider = "provider"
    var customer_id = ""
    var customer_language = "English"
    var customer_language_code = "EN"
    var pickup_Address_key = "pickup_Address_key"
    var pickup_Address = "pickup_Address"
    var dropoff_Address_key = "dropoff_Address_key"
    var dropoff_Address = "dropoff_Address"



    var bank_info_id = "bank_info_id"
    var bankName = "bankName"
    var bankIFSC = "bankIFSC"
    var bankAcNumber = "bankAcNumber"
    var actionOnBank = "actionOnBank"

    var forgotOTP = 0
    var loginOTP = 0


    //=================API Request prms====================
    object REQUEST_PRMS {

        const val refresh_token = "refresh_token"
        const val user_type = "user_type"
        const val user_id = "user_id"
        const val code = "code"
        const val type = "type"
        const val cms_code = "cms_code"
        const val customer_id = "customer_id"
        const val mobile_country_code = "mobile_country_code"
        const val mobile = "mobile"
        const val otp = "otp"
        const val page = "page"
        const val email = "email"
        const val name = "name"
        const val gender = "gender"
        const val dob = "dob"
        const val notify_latest_offer = "notify_latest_offer"
        const val profile_photo = "profile_photo"
    }

    object HTTP_STATUS {
        const val OK = "200"
        const val NOTFOUND = "404"
        const val ALREADY_EXIST = "409"
    }
}