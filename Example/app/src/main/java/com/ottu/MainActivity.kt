package com.ottu

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ottu.checkout.Checkout
import com.ottu.checkout.network.model.payment.ApiTransactionDetails
import com.ottu.checkout.ui.theme.CheckoutTheme
import com.ottu.checkout.ui.util.setOnSingleClickListener
import com.ottu.customization.ThemeCustomizationActivityResultContract
import com.ottu.databinding.ActivityMainBinding
import com.ottu.network.CreateTransactionRequest
import com.ottu.network.SessionResponse
import com.ottu.network.retrofit
import com.ottu.network.services
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var amount = 10.0

    private var merchantId = "alpha.ottu.net"
    private var apiKey = "cHSLW0bE.56PLGcUYEhRvzhHVVO9CbF68hmDiXcPI"
    private var customerId: String? = "john2"
    private var currencyCode = "KWD"

    //FIXME comment line
//        private var customerPhone = "966557877988"
    private var customerPhone = null

    private var currentSessionId: String? = null
    private var currentPreloadPayload: ApiTransactionDetails? = null
    private var currentThemeAppearancePair: Pair<CheckoutTheme.Appearance?, CheckoutTheme.Appearance?>? =
        null


    private val themeCustomizationActivityResultLauncher =
        registerForActivityResult(ThemeCustomizationActivityResultContract()) { appearancePair ->
            this.currentThemeAppearancePair = appearancePair
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.setupViews()
    }

    private fun ActivityMainBinding.setupViews() {
        btnPay.isEnabled = currentSessionId != null

        etAmount.setText(amount.toString())
        etCurrencyCode.setText(currencyCode)
        etMerchantId.setText(merchantId)
        etApiKey.setText(apiKey)
        etCustomerId.setText(customerId)

        cbNoFormsOfPayment.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbGooglePay.isChecked = false
                cbStcPay.isChecked = false
                cbRedirect.isChecked = false
                cbTokenPay.isChecked = false
                cbOttuPg.isChecked = false
            }
        }

        listOf(cbGooglePay, cbStcPay, cbRedirect, cbTokenPay, cbOttuPg).forEach {
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    cbNoFormsOfPayment.isChecked = false
                }
            }
        }

        btnThemeCustomization.setOnSingleClickListener {
            themeCustomizationActivityResultLauncher.launch(Unit)
        }

        btnGetSession.setOnSingleClickListener {

            amount = etAmount.text.toString().toDouble()
            currencyCode = etCurrencyCode.text.toString()
            merchantId = etMerchantId.text.toString()
            apiKey = etApiKey.text.toString()
            customerId = etCustomerId.text.toString()

            if (customerId?.isEmpty() == true) {
                customerId = null
            }

            if (merchantId.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Merchant ID cannot be null or empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSingleClickListener
            }

            if (apiKey.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "API Key cannot be null or empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSingleClickListener
            }

            if (currencyCode.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Currency Code cannot be null or empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSingleClickListener
            }

            if (amount <= 0) {
                Toast.makeText(
                    this@MainActivity,
                    "Amount cannot be null or <= 0",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSingleClickListener
            }

            fetchSession {
                this@MainActivity.currentSessionId = it.session_id
                this@MainActivity.currentPreloadPayload = it.sdk_setup_preload_payload
                btnPay.isEnabled = true
            }
        }

        btnPay.setOnSingleClickListener {
            val formsOfPayment = getFormsOfPayment()

            if (formsOfPayment?.isEmpty() == true) {
                Toast.makeText(
                    this@MainActivity,
                    "Forms Of Payment cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnSingleClickListener
            }

            this@MainActivity.currentSessionId?.let {
                CheckoutSampleActivity.openActivity(
                    this@MainActivity,
                    it,
                    amount,
                    merchantId,
                    apiKey,
                    customerId,
                    formsOfPayment,
                    cbShowPaymentDetails.isChecked,
                    currentPreloadPayload,
                    currentThemeAppearancePair?.first,
                    currentThemeAppearancePair?.second,
                    cbCrash.isChecked
                )
            }
        }
    }

    private fun fetchSession(
        session: (SessionResponse) -> Unit,
    ) {
        lifecycleScope.launch {
            val fetchSessionIdBuilder =
                MaterialAlertDialogBuilder(this@MainActivity).apply {
                    setMessage("Fetching Session Id")
                    setCancelable(false)
                }

            val fetchSessionIdDialog = fetchSessionIdBuilder.show()

            val sessionResponse =
                async { getSession(amount, merchantId, apiKey, customerId) }.await()

            fetchSessionIdDialog.dismiss()

            sessionResponse?.let {
                session.invoke(it)
            } ?: run {
                MaterialAlertDialogBuilder(this@MainActivity).apply {
                    setMessage("Cannot fetch Session Id")
                }.show()
            }
        }
    }

    private suspend fun getSession(
        amount: Double,
        merchantId: String,
        apiKey: String,
        customerId: String?,
    ): SessionResponse? {
        val services = retrofit(merchantId, apiKey).services()

        val language = Locale.getDefault().language.ifEmpty { "en" }

        Log.e("TAG", "getSession: $language", )

        return try {
            val request = CreateTransactionRequest(
                amount = amount.toString(),
                currency_code = currencyCode,
                pg_codes = listOf(
                    "mpgs-testing",
                    "ottu_pg_kwd_tkn",
                    "knet-staging",
                    "benefit",
                    "benefitpay",
                    "stc_pay",
                    "nbk-mpgs",
                    "gbk-cc"
                ),
                type = "e_commerce",
                customer_id = customerId,
                customer_phone = customerPhone,
                include_sdk_setup_preload = binding?.cbPreloadPayload?.isChecked ?: false,
                language = language
            )


            services?.createTransaction(language, request)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            null
        }
    }

    private fun getFormsOfPayment(): ArrayList<Checkout.FormsOfPayment>? {
        val formsOfPayment = arrayListOf<Checkout.FormsOfPayment>()

        binding?.apply {
            if (cbGooglePay.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.GooglePay)
            }

            if (cbStcPay.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.StcPay)
            }

            if (cbOttuPg.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.OttuPG)
            }

            if (cbTokenPay.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.TokenPay)
            }

            if (cbRedirect.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.Redirect)
            }
        }

        return if (binding?.cbNoFormsOfPayment?.isChecked == true) null else formsOfPayment
    }
}