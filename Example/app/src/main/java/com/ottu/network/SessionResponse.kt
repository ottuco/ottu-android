package com.ottu.network

import com.ottu.checkout.data.model.payment.PayloadPaymentType
import com.ottu.checkout.network.model.payment.ApiTransactionDetails
import com.squareup.moshi.Json

data class SessionResponse(
    val session_id: String,
    val sdk_setup_preload_payload: ApiTransactionDetails?,
    @Json(name = "payment_type")
    val paymentType: PayloadPaymentType?,
)