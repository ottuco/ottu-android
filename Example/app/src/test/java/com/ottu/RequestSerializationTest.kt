package com.ottu

import com.ottu.checkout.network.moshi.MoshiFactory
import com.ottu.network.BrandingOption
import com.ottu.network.BrandingOptionsRequest
import com.ottu.network.BrandingPaymentMethods
import com.ottu.network.CreateTransactionRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RequestSerializationTest {
    @Test
    fun transactionRequestAdapterIncludesBrandingModels() {
        val moshi = MoshiFactory.newInstance()
        // Adapter creation traverses nullable properties too, including branding_options.
        assertNotNull(moshi.adapter(CreateTransactionRequest::class.java))

        val branding = BrandingOptionsRequest(
            BrandingPaymentMethods(
                cod = BrandingOption(text = "Pay on delivery", color = "#123456", fontWeight = 600),
            ),
        )
        val adapter = moshi.adapter(BrandingOptionsRequest::class.java)
        val json = adapter.toJson(branding)
        assertTrue(json.contains("\"payment_methods\""))
        assertTrue(json.contains("\"font_weight\":600"))
        assertEquals(branding, adapter.fromJson(json))
    }
}
