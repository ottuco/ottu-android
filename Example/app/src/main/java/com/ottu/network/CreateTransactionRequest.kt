package com.ottu.network


data class CreateTransactionRequest(
    val amount: String,
    val currency_code: String,
    val pg_codes: List<String>,
    val type: String,
    val customer_id: String?,
    val customer_phone: String?,
    val include_sdk_setup_preload: Boolean,
    val language: String
)