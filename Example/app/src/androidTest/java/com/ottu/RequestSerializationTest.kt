package com.ottu

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ottu.checkout.network.moshi.MoshiFactory
import com.ottu.network.BrandingOption
import com.ottu.network.BrandingOptionsRequest
import com.ottu.network.BrandingPaymentMethods
import com.ottu.network.CreateTransactionRequest
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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
        val option = JSONObject(json).getJSONObject("payment_methods").getJSONObject("cod")
        assertEquals("Pay on delivery", option.getString("text"))
        assertEquals("#123456", option.getString("color"))
        assertEquals(600, option.getInt("font_weight"))
        assertEquals(branding, adapter.fromJson(json))
    }
}
