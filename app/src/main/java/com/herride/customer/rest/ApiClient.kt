package com.herride.customer.rest

import android.util.Log
import com.herride.customer.BuildConfig
import com.herride.customer.common.Constants
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

  //  private val baseURL = "http://api.acargolb.crypticpoint.com/"
    private val baseURL = "http://54.157.133.185:3000/"
    private var retrofit : Retrofit? = null
    private val REQUEST_TIMEOUT = 60
    private var logging = HttpLoggingInterceptor()
    companion object {
        private const val REQUEST_TIMEOUT = 360
        private var logging = HttpLoggingInterceptor()
        fun createRetrofit(token: String): Retrofit? {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .baseUrl(Constants.baseURL)
                .client(getHttpLogClient(token))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }


        private fun getHttpLogClient(token: String): OkHttpClient {
            val httpClient = OkHttpClient().newBuilder()
                .addInterceptor(object : Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization","Bearer $token")
                            .build()
                        Log.e("AUTH_TOKEN_IN_HEADER","getHttpLogClient = Authorization = Bearer = ${token}")

                        return chain.proceed(newRequest)
                    }

                })
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            }
            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(logger)
            return httpClient.build()
        }

//    fun createRetrofit(): Retrofit? {
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
//        return Retrofit.Builder()
//            .baseUrl(baseURL)
//            .client(getHttpLogClient())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//    }
    }
}