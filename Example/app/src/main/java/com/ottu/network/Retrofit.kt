package com.ottu.network

import com.ottu.checkout.network.moshi.MoshiFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private fun okhttp(apiKey: String) = OkHttpClient
    .Builder()
    .addInterceptor(HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    })
    .addInterceptor(Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(
                name = "Content-Type",
                value = "application/json"
            )
            .addHeader(
                name = "Authorization",
                value = "Api-Key $apiKey"
            )
            .build()

        chain.proceed(request)
    })
    .build()

fun retrofit(
    merchantId: String,
    apiKey: String,
): Retrofit? = Retrofit.Builder()
    .baseUrl("https://${merchantId}/b/checkout/v1/")
    .client(okhttp(apiKey))
    .addConverterFactory(
        MoshiConverterFactory.create(
            MoshiFactory.newInstance()
        )
    )
    .build()

fun Retrofit?.services() = this?.create(ApiService::class.java)