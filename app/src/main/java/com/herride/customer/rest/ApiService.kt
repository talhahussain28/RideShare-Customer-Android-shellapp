package com.herride.customer.rest

import com.herride.customer.common.Constants
import com.herride.customer.model.map_poliline.Result
import com.herride.customer.preference.SessionManager
import com.herride.customer.responsesNew.SignUpResponseNew
import com.herride.customer.ui.home.driver_details.response.DriverDetailApiResponse
import com.herride.customer.ui.home.faq.response.FaqListApiResponse
import com.herride.customer.ui.home.faq.response.FaqSubListApiResponse
import com.herride.customer.ui.home.notification.response.NotificationApiResponse
import com.herride.customer.ui.home.notification.response.NotificationDeleteApiResponse
import com.herride.customer.ui.home.order_place.model.BookOrderRequest
import com.herride.customer.ui.home.order_place.response.BookTripApiResponse
import com.herride.customer.ui.home.order_place.response.GetVehicleListApiResponse
import com.herride.customer.ui.home.order_place.response.PreferenceListApiResponse
import com.herride.customer.ui.home.payment_card_setting.response.AddCardApiResponse
import com.herride.customer.ui.home.ratetheride.response.RateTheRideUnratedApiResponse
import com.herride.customer.ui.home.response.GetProfileApiResponse
import com.herride.customer.ui.home.response.GetSettingApiResponse
import com.herride.customer.ui.home.response.NewCurrentJobApiResponse
import com.herride.customer.ui.home.response.ReceiptApiResponse
import com.herride.customer.ui.home.setting.response.AddressGetAddDeleteApiResponse
import com.herride.customer.ui.home.setting.response.EmergencyContactApiResponse
import com.herride.customer.ui.home.setting.response.LanguageListApiResponse
import com.herride.customer.ui.home.trips.response.TripApiResponse
import com.herride.customer.ui.signin_up.response.EmailMObileExistApiResponse
import com.herride.customer.ui.signin_up.response.PhoneVerifyOtpApiResponse
import com.herride.customer.ui.signin_up.response.ResendOtpApiResponse
import com.herride.customer.ui.signin_up.response.SignInApiResponse
import com.herride.customer.ui.webview.GetCMSApiResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * All the API services are here.
 */

interface ApiService {

    /* @GET("language/list")
     fun getLanguageListAPI(): Observable<LanguageListResponse>

     @FormUrlEncoded
     @POST("auth/sign-in")
     fun callSignInAPI(
         @Field("mobile_country_code") _mobile_country_code: String,
         @Field("mobile") _mobile: String,
         @Field("password") _password: String,
         @Field("user_type") _user_type: String,
         @Field(Constants.REQUEST_PRMS.code) _code: String
     ): Observable<SignInResponse>*/


    object parameters {
        const val code = "code"
        const val type = "type"
        const val email = "email"
        const val mobile = "mobile"
        const val mobile_country_code = "mobile_country_code"
        const val verify_mobile_id = "verify_mobile_id"
        const val otp = "otp"
        const val password = "password"
        const val user_id = "user_id"
        const val driver_id = "driver_id"
        const val user_type = "user_type"
        const val status = "status"
        const val gender = "gender"
        const val profile_picture = "profile_picture"
        const val register_type = "register_type"
        const val player_id = "player_id"
        const val device_name = "device_name"
        const val device_type = "device_type"
        const val social_id = "social_id"
        const val name = "name"
        const val customer_id = "customer_id"
        const val page = "page"
        const val trip_id = "trip_id"
        const val comment = "comment"
        const val dispute_msg = "dispute_msg"
        const val rating = "rating"
        const val action = "action"
        const val address_type = "address_type"
        const val address = "address"
        const val latitude = "latitude"
        const val longitude = "longitude"
        const val address_id = "address_id"
        const val start_address = "start_address"
        const val finish_address = "finish_address"
        const val start_latitude = "start_latitude"
        const val start_longitude = "start_longitude"
        const val finish_latitude = "finish_latitude"
        const val finish_longitude = "finish_longitude"
        const val mode_id = "mode_id"
        const val music_id = "music_id"
        const val accessible_id = "accessible_id"
        const val temperature = "temperature"
        const val help_category_id = "help_category_id"
        const val city_id = "city_id"
        const val state_id = "state_id"
    }

    @GET("/api/language/list")
    fun getLanguageListAPI(): Observable<LanguageListApiResponse>


    @GET("/api/cms/list")
    fun getCMSAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.type) type: String
    ): Observable<retrofit2.Response<GetCMSApiResponse>>

    @FormUrlEncoded
    @POST("api/auth/check-email-mobile-exists")
    fun emialMobileExistCheckAPI_(
            @Field(parameters.code) codes: String,
            @Field(parameters.name) names: String,
            @Field(parameters.email) emails: String,
            @Field(parameters.mobile_country_code) mobile_country_codes: String,
            @Field(parameters.mobile) mobiles: String,
            @Field(parameters.user_type) user_types: String
    ): Observable<retrofit2.Response<EmailMObileExistApiResponse>>


    @FormUrlEncoded
    @POST("api/auth/sign-up")
    fun signUpAPI_(
            @Field(parameters.code) codes: String,
            @Field(parameters.name) names: String,
            @Field(parameters.email) emails: String,
            @Field(parameters.mobile_country_code) mobile_country_codes: String,
            @Field(parameters.mobile) mobiles: String,
            @Field(parameters.profile_picture) profile_pictures: String,
            @Field(parameters.register_type) register_types: String,
            @Field(parameters.player_id) player_id: String,
            @Field(parameters.social_id) social_ids: String,
            @Field(parameters.user_type) user_types: String
    ): Observable<retrofit2.Response<SignInApiResponse>>


    /*   @Multipart
       @POST("api/auth/sign-up")
       fun signUpAPI(
               @Part(parameters.code) codes: RequestBody,
               @Part(parameters.name) names: RequestBody,
               @Part(parameters.email) emails: RequestBody,
               @Part(parameters.mobile_country_code) mobile_country_codes: RequestBody,
               @Part(parameters.mobile) mobiles: RequestBody,
               @Part(parameters.user_type) user_types: RequestBody,
               @Part profile_picture: MultipartBody.Part
       ): Observable<retrofit2.Response<SignInApiResponse>>*/

    @Multipart
    @POST("api/auth/sign-up")
    fun signUpAPI(
            @Part(parameters.code) codes: RequestBody,
            @Part(parameters.name) names: RequestBody,
            @Part(parameters.email) emails: RequestBody,
            @Part(parameters.mobile_country_code) mobile_country_codes: RequestBody,
            @Part(parameters.mobile) mobiles: RequestBody,
            @Part(parameters.gender) genders: RequestBody,
            @Part(parameters.player_id) player_id: RequestBody,
            @Part(parameters.device_name) device_name: RequestBody,
            @Part(parameters.device_type) device_type: RequestBody,
            @Part(parameters.user_type) user_types: RequestBody,
            @Part profile_picture: MultipartBody.Part
    ): Observable<retrofit2.Response<SignInApiResponse>>

    @Multipart
    @POST("api/auth/sign-up")
    fun signUpAPINew(
      //  @Part(parameters.code) codes: RequestBody,
        @Part(parameters.name) names: RequestBody,
        @Part(parameters.email) emails: RequestBody,
        @Part(parameters.mobile_country_code) mobile_country_codes: RequestBody,
        @Part(parameters.mobile) mobiles: RequestBody,
        @Part(parameters.password) password: RequestBody,
        @Part(parameters.gender) genders: RequestBody,
        @Part(parameters.city_id) city_id: RequestBody,
        @Part(parameters.state_id) state_id: RequestBody,
      //  @Part(parameters.player_id) player_id: RequestBody,
      //  @Part(parameters.device_name) device_name: RequestBody,
      //  @Part(parameters.device_type) device_type: RequestBody,
        @Part(parameters.user_type) user_types: RequestBody
        //@Part profile_picture: MultipartBody.Part
    ): Observable<retrofit2.Response<SignUpResponseNew>>
    @FormUrlEncoded
    @POST("api/auth/sign-in")
    fun signInAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.mobile_country_code) mobile_country_codes: String,
            @Field(parameters.mobile) mobiles: String,
            @Field(parameters.player_id) player_id: String,
            @Field(parameters.device_name) device_name: String,
            @Field(parameters.device_type) device_type: String,
            @Field(parameters.user_type) user_types: String
    ): Observable<retrofit2.Response<SignInApiResponse>>

    @FormUrlEncoded
    @POST("api/auth/verify-mobile")
    fun verifyRegisterOtpAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.verify_mobile_id) verify_mobile_ids: String,
            @Field(parameters.otp) otps: String
    ): Observable<retrofit2.Response<SignInApiResponse>>

    @FormUrlEncoded
    @POST("api/auth/verify-otp")
    fun verifyOtpAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.user_type) user_types: String,
            @Field(parameters.user_id) user_ids: String,
            @Field(parameters.otp) otps: String
    ): Observable<retrofit2.Response<PhoneVerifyOtpApiResponse>>

    @FormUrlEncoded
    @POST("api/auth/resend-otp-verify-mobile")
    fun resendRegisterOtpAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.verify_mobile_id) verify_mobile_ids: String,
            @Field(parameters.name) names: String
    ): Observable<retrofit2.Response<SignInApiResponse>>

    @FormUrlEncoded
    @POST("api/auth/resend-otp")
    fun resendOtpAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.user_type) user_types: String,
            @Field(parameters.user_id) user_ids: String
    ): Observable<retrofit2.Response<ResendOtpApiResponse>>


    @GET("maps/api/directions/json")
    abstract fun getDirectionsSingle(
            @Query("mode") mmode: String,
            @Query("transit_routing_preference") routingPreference: String,
            @Query("origin") morigin: String,
            @Query("destination") mdestination: String,
            @Query("key") apiKey: String
    ): Single<Result>


    @FormUrlEncoded
    @POST("api/customer/address")
    fun getAddDeleteAddressAPI(
            @Field(parameters.code) codes: String,
            @Field(parameters.customer_id) customer_ids: String,
            @Field(parameters.action) actions: String,
            @Field(parameters.address_type) address_types: String,
            @Field(parameters.address) addresss: String,
            @Field(parameters.latitude) latitudes: String,
            @Field(parameters.longitude) longitudes: String,
            @Field(parameters.address_id) address_ids: String
    ): Observable<retrofit2.Response<SignInApiResponse>>


    @FormUrlEncoded
    @POST("api/customer/address")
    fun addAddressAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<AddressGetAddDeleteApiResponse>>


    @FormUrlEncoded
    @POST("api/card/add")
    fun addCardAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<AddCardApiResponse>>


    @GET("api/card/list")
    fun getCardAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.customer_id) customer_id: String
    ): Observable<retrofit2.Response<AddCardApiResponse>>

    @FormUrlEncoded
    @POST("api/card/remove")
    fun removeCardAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<AddCardApiResponse>>

    @FormUrlEncoded
    @POST("api/card/set-default")
    fun setdefaultCardAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<AddCardApiResponse>>

    @GET("api/vehicle/list")
    fun getVehicleAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.customer_id) customer_id: String,
            @Query(parameters.start_address) start_address: String,
            @Query(parameters.finish_address) finish_address: String,
            @Query(parameters.start_latitude) start_latitude: String,
            @Query(parameters.start_longitude) start_longitude: String,
            @Query(parameters.finish_latitude) finish_latitude: String,
            @Query(parameters.finish_longitude) finish_longitude: String
    ): Observable<retrofit2.Response<GetVehicleListApiResponse>>

    @GET("api/preference/list")
    fun getPreferenceAPI(
            @Query(parameters.code) code: String
    ): Observable<retrofit2.Response<PreferenceListApiResponse>>


    @GET("api/auth/get-profile")
    fun getProfileAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.user_id) user_id: String,
            @Query(parameters.user_type) user_type: String
    ): Observable<retrofit2.Response<GetProfileApiResponse>>

    @GET("api/settings/list")
    fun getSettingAPI(
            @Query(parameters.code) codes: String
    ): Observable<retrofit2.Response<GetSettingApiResponse>>

    @Multipart
    @POST("api/auth/update-profile")
    fun profileAPI(
            @Part(parameters.code) codes: RequestBody,
            @Part(parameters.user_id) user_id: RequestBody,
            @Part(parameters.name) names: RequestBody,
            @Part(parameters.email) emails: RequestBody,
            @Part(parameters.gender) gender: RequestBody,
            @Part(parameters.user_type) user_types: RequestBody,
            @Part profile_picture: MultipartBody.Part
    ): Observable<retrofit2.Response<SignInApiResponse>>
// @Part(parameters.mobile_country_code) mobile_country_codes: RequestBody,
//   @Part(parameters.mobile) mobiles: RequestBody,
//     @Part(parameters.gender) genders: RequestBody,

    @FormUrlEncoded
    @POST("api/auth/update-profile")
    fun profileAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<GetProfileApiResponse>>


    @GET("api/emergency/list")
    fun getEmergencyContactAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.customer_id) customer_id: String
    ): Observable<retrofit2.Response<EmergencyContactApiResponse>>


    @FormUrlEncoded
    @POST("api/emergency/add")
    fun addEmergencyContactAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<AddressGetAddDeleteApiResponse>>

    @Multipart
    @POST("api/emergency/add")
    fun addEmergencyContactAPI(
            @Part(parameters.code) codes: RequestBody,
            @Part(parameters.customer_id) customer_id: RequestBody,
            @Part(parameters.name) names: RequestBody,
            @Part(parameters.mobile) mobile: RequestBody,
            @Part(parameters.mobile_country_code) mobile_country_code: RequestBody,
            @Part profile_picture: MultipartBody.Part
    ): Observable<retrofit2.Response<EmergencyContactApiResponse>>


    @GET("api/help-category/list")
    fun getFaqAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.type) type: String
    ): Observable<retrofit2.Response<FaqListApiResponse>>

    @GET("api/help/list")
    fun getFaqSubAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.help_category_id) help_category_id: String
    ): Observable<retrofit2.Response<FaqSubListApiResponse>>


    ////////////////////////////////////////////////////////////////////////////////////////////
    //TODO Trip api flow

    @POST("api/trip/book")
    fun bookTripByModelAPI(
            @Body postTripModel: BookOrderRequest
    ): Observable<retrofit2.Response<BookTripApiResponse>>


    @FormUrlEncoded
    @POST("api/trip/book")
    fun bookTripByMapAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<SignInApiResponse>>


    @FormUrlEncoded
    @POST("api/trip/book")
    fun bookTripByJsonAPI(
            @Body bookjobJson: String
    ): Observable<retrofit2.Response<SignInApiResponse>>

    @GET("api/trip/ongoing")
    fun getNewCurrentJobAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.user_type) user_type: String,
            @Query(parameters.user_id) user_id: String
    ): Observable<retrofit2.Response<NewCurrentJobApiResponse>>

    @FormUrlEncoded
    @POST("api/trip/cancel")
    fun rejectJobAPI(
            @Field(parameters.code) code: String,
            @Field(parameters.customer_id) driver_id: String,
            @Field(parameters.trip_id) trip_id: String
    ): Observable<retrofit2.Response<NewCurrentJobApiResponse>>


    @GET("api/trip/receipt")
    fun getReceiptJobAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.trip_id) trip_id: String
    ): Observable<retrofit2.Response<ReceiptApiResponse>>


    @FormUrlEncoded
    @POST("api/trip/rating")
    fun updateRateAPI(
            @Field(parameters.code) code: String,
            @Field(parameters.user_type) user_type: String,
            @Field(parameters.user_id) user_id: String,
            @Field(parameters.trip_id) trip_id: String,
            @Field(parameters.rating) rating: String,
            @Field(parameters.comment) comment: String
    ): Observable<retrofit2.Response<ReceiptApiResponse>>

    @GET("api/driver/details")
    fun getDriverDetailAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.driver_id) driver_id: String
    ): Observable<retrofit2.Response<DriverDetailApiResponse>>


    @GET("api/notification/delete")
    fun deleteNotificationListAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.user_id) user_id: String
    ): Observable<retrofit2.Response<NotificationDeleteApiResponse>>

    @GET("api/notification/list")
    fun getNotificationListAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.user_id) user_id: String
    ): Observable<retrofit2.Response<NotificationApiResponse>>


    @FormUrlEncoded
    @POST("api/trip/dispute")
    fun updateDisputeAPI(
            @Field(parameters.code) code: String,
            @Field(parameters.customer_id) customer_id: String,
            @Field(parameters.trip_id) trip_id: String,
            @Field(parameters.dispute_msg) dispute_msg: String
    ): Observable<retrofit2.Response<ReceiptApiResponse>>


    @GET("api/trip/history")
    fun getTripsAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.page) page: String,
            @Query(parameters.user_type) user_type: String,
            @Query(parameters.status) status: String,
            @Query(parameters.user_id) user_id: String
    ): Observable<retrofit2.Response<TripApiResponse>>

    @GET("api/customer/rate-the-trip")
    fun getUnratedTripsAPI(
            @Query(parameters.code) code: String,
            @Query(parameters.page) page: String,
            @Query(parameters.customer_id) customer_id: String
    ): Observable<retrofit2.Response<RateTheRideUnratedApiResponse>>



    @GET("api/auth/logout-delete-account")
    fun logoutAPI(
            @Query(parameters.code) codes: String,
            @Query(parameters.action) action: String,
            @Query(parameters.user_id) user_id: String,
            @Query(parameters.user_type) user_type: String
    ): Observable<retrofit2.Response<GetSettingApiResponse>>


    @FormUrlEncoded
    @POST("api/auth/update-player-id")
    fun updatePayerIdAPI(
            @FieldMap params: Map<String, String>
    ): Observable<retrofit2.Response<SignInApiResponse>>

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    // TODO API NETWORK MANAGER
    companion object {
        fun create(pref: SessionManager): ApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//              .client(client)
                    .client(getHttpLogClient(pref))
                    .baseUrl(Constants.baseURL)
                    .build()

            return retrofit.create(ApiService::class.java)
        }


        private val REQUEST_TIMEOUT = 60

        private var logging = HttpLoggingInterceptor()

        private fun getHttpLogClient(pref: SessionManager): OkHttpClient {

            val httpClient = OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            /*if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            }*/

            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            // Create a Custom Interceptor to apply Headers application wide
//            val headerInterceptor = object : Interceptor {
//
//                override fun intercept(chain: Interceptor.Chain): Response {
//
//                    var request = chain.request()
//
//                    /*   request = request.newBuilder()
//                           .addHeader("Authorization", "Bearer" + " " + pref.access_token)
//                           .build()*/
//
//                    val response = chain.proceed(request)
//                    /* if (response.code != 200) {
//                         Log.e("SDgsdggdsdg", "DSGsdgsdg")
//                         if (response.code == 412) {
//                             var api = ApiService.create(pref)
//                             api.callRegenerateTockenAPI(
//                                 pref.passenger_id,
//                                 Constants.passenger,
//                                 pref.defaultLan
//                             )
//                                 .subscribeOn(Schedulers.io())
//                                 .observeOn(AndroidSchedulers.mainThread())
//                                 .subscribeBy(
//                                     onNext = {
//
//                                         if (it.status.equals(Constants.HTTP_STATUS.OK)) {
//
//                                             pref.access_token = it.payload.accessToken
//
//                                             response
//                                         }
//                                         println(it)
//                                     },
//
//                                     onComplete = {
//                                         println("Completed")
//                                     },
//
//                                     onError = {
//                                         // Log.e("ForgotPasswordViewModel","onError"+it.message)
//
//                                         *//*  if (it is HttpException) {
//
//                                         val errorJsonString = it.response().errorBody()?.string()
//                                         Log.e("ForgotPasswordViewModel","errorJsonString==>"+errorJsonString)
//                                         var message: JsonObject = JsonParser().parse(errorJsonString).asJsonObject["error"] as JsonObject
//                                         Log.e("ForgotPasswordViewModel","if==>" + message.get("message"))
//
//
//                                     } else {
//                                         Log.e("ForgotPasswordViewModel","else ==>" + message)
//                                     }
//*//*
//                               }
//                           )
//
//                   }
//               }*/
//                    return response
//                }
//            }
//
//            httpClient.addInterceptor(headerInterceptor)
            httpClient.addInterceptor(logger)

            return httpClient.build()
        }


    }


}