package com.ottu.network

import com.ottu.checkout.network.model.payment.ApiTransactionDetails

data class SessionResponse(
    val session_id: String,
    val sdk_setup_preload_payload: ApiTransactionDetails?
)