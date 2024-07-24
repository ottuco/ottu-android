package com.ottu.network

import com.ottu.checkout.network.Headers
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("pymt-txn/")
    suspend fun createTransaction(
        @Header(Headers.HEADER_ACCEPT_LANGUAGE_NAME) language: String,
        @Body body: CreateTransactionRequest
    ): SessionResponse
}