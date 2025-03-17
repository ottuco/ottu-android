package com.ottu.network


data class CreateTransactionRequest(
    val amount: String,
    val currency_code: String,
    val pg_codes: List<String>,
    val type: String,
    val customer_id: String?,
    val customer_phone: String?,
    val customer_first_name: String?,
    val customer_last_name: String?,
    val customer_email: String?,
    val billing_address: BillingAddress?,
    val include_sdk_setup_preload: Boolean,
    val card_acceptance_criteria: CardAcceptanceCriteria? = null,
    val language: String
) {

    data class BillingAddress(
        val country: String?,
        val city: String?,
        val line1: String?,
    )

    data class CardAcceptanceCriteria(
        val min_expiry_time: Int
    )

}