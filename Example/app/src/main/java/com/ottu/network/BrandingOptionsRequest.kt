package com.ottu.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class BrandingOptionsRequest(
    @Json(name = "payment_methods")
    val paymentMethods: BrandingPaymentMethods,
)

data class BrandingPaymentMethods(
    @Json(name = "knet-staging")
    var knetStaging: BrandingOption? = null,
    val stc: BrandingOption? = null,
    var cod: BrandingOption? = null,
    @Json(name = "mpgs-testing")
    var mpgs: BrandingOption? = null,
) {
     var cs: BrandingOption? = null
    @Json(name = "nbk-mpgs")
     var nbk_mpgs: BrandingOption? = null
     var tap_pg: BrandingOption? = null
     var ottu_sdk: BrandingOption? = null
}

@JsonClass(generateAdapter = true)
data class BrandingOption(
    val text: String,
    val color: String,
    @Json(name = "font_weight")
    val fontWeight: Int,
)