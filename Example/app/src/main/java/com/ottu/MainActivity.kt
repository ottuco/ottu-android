package com.ottu

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ottu.checkout.Checkout
import com.ottu.checkout.data.model.payment.AutoDebitAgreement
import com.ottu.checkout.data.model.payment.PayloadPaymentType
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

    private var merchantId = "" // insert your merchant ID

    private var apiKey = "" // insert your API key for a transaction creation

    private var customerId: String? = "customer"
    private var currencyCode = "KWD"
    private var transactionType = "e_commerce"

    private var customerFirstName = "Name"
    private var customerLastName = "Surname"
    private var customerEmail = "customer@some.mail"
    private val billingCountry = "KW"
    private val billingCity = "Kuwait City"
    
    private var customerPhone: String? = "99459272"

    private var currentSessionId: String? = null
    private var currentPreloadPayload: ApiTransactionDetails? = null
    private var currentThemeAppearancePair: Pair<CheckoutTheme.Appearance?, CheckoutTheme.Appearance?>? =
        null

    private val pgCodes = mutableListOf(
        PgCodeItem("mpgs-testing"),
        PgCodeItem("knet-staging"),
        PgCodeItem("benefit"),
        PgCodeItem("benefitpay"),
        PgCodeItem("stc_pay"),
        PgCodeItem("nbk-mpgs"),
//        PgCodeItem("urpay"),
        PgCodeItem("tamara"),
        PgCodeItem("tabby"),
        PgCodeItem("tap_pg"),
        PgCodeItem("ottu_sdk", false),
        PgCodeItem("muscatbank_demo", false),
    )

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
        etPhone.setText(customerPhone)
        etCardExpire.filters = arrayOf(
            InputFilter.LengthFilter(3), InputFilter { source, _, _, dest, dstart, dend ->
                val newText = dest.replaceRange(dstart, dend, source.toString()).toString()
                val num = newText.toIntOrNull() ?: return@InputFilter ""
                if (num in 1..365) null else ""
            }
        )

        cbPaymentOptionMode.setOnCheckedChangeListener { buttonView, isChecked ->
            tilPaymentListVisibleItems.isEnabled = isChecked
        }

        cbNoFormsOfPayment.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cbGooglePay.isChecked = false
                cbFlexMethods.isChecked = false
                cbStcPay.isChecked = false
                cbRedirect.isChecked = false
                cbTokenPay.isChecked = false
                cbCardOnsite.isChecked = false
                cbUrpay.isChecked = false
            }
        }

        listOf(
            cbGooglePay,
            cbFlexMethods,
            cbStcPay,
            cbRedirect,
            cbTokenPay,
            cbCardOnsite,
            cbUrpay
        ).forEach {
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    cbNoFormsOfPayment.isChecked = false
                }
            }
        }

        pgCodesContainer.removeAllViews()
        pgCodes.chunked(3).forEach { rowItems ->
            val row = LinearLayout(this@MainActivity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            rowItems.forEach {
                val view = MaterialCheckBox(this@MainActivity).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    text = it.value
                    isChecked = it.isChecked
                    setOnCheckedChangeListener { view, isChecked ->
                        val index = pgCodes.indexOfFirst { code -> code.value == it.value }
                        val toUpdate = pgCodes[index]
                        pgCodes[index] = toUpdate.copy(isChecked = isChecked)
                    }
                }
                row.addView(view)
            }
            pgCodesContainer.addView(row)
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
            customerPhone = etPhone.text.toString()

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

            if (customerPhone?.isEmpty() == true) {
                customerPhone = null
            }

            fetchSession {
                this@MainActivity.currentSessionId = it.session_id
                this@MainActivity.currentPreloadPayload = it.sdk_setup_preload_payload
                btnPay.isEnabled = true
            }
        }

        btnPay.setOnSingleClickListener {
            root.hideSoftKeyboard()
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
                    getPaymentOptionSettings(),
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

        var language = Locale.getDefault().language.ifEmpty { "en" }
        if (language != "ar")
            language = "en"

        return try {
            val payloadPaymentType = if (binding?.cbAutoDebit?.isChecked == true
            ) PayloadPaymentType.AUTO_DEBIT else null
            val request = CreateTransactionRequest(
                amount = amount.toString(),
                currency_code = currencyCode,
                pg_codes = pgCodes.filter { it.isChecked }.map { it.value },
                type = transactionType,
                customer_id = customerId,
                customer_phone = customerPhone,
                include_sdk_setup_preload = binding?.cbPreloadPayload?.isChecked ?: false,
                language = language,
                customer_first_name = customerFirstName,
                customer_last_name = customerLastName,
                customer_email = customerEmail,
                billing_address = CreateTransactionRequest.BillingAddress(
                    country = billingCountry,
                    city = billingCity,
                    line1 = "something"
                ),
                card_acceptance_criteria = binding?.etCardExpire?.text?.takeIf { it.isNotEmpty() }
                    ?.let {
                        CreateTransactionRequest.CardAcceptanceCriteria(it.toString().toInt())
                    },
                payment_type = payloadPaymentType,
                agreement = if (payloadPaymentType == PayloadPaymentType.AUTO_DEBIT) AutoDebitAgreement.default() else null
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

            if (cbFlexMethods.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.FlexMethods)
            }

            if (cbStcPay.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.StcPay)
            }

            if (cbTokenPay.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.TokenPay)
            }

            if (cbRedirect.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.Redirect)
            }

            if (cbCardOnsite.isChecked) {
                formsOfPayment.add(Checkout.FormsOfPayment.CardOnsite)
            }

//            if (cbUrpay.isChecked) {
//                formsOfPayment.add(Checkout.FormsOfPayment.URPay)
//            }
        }

        return if (binding?.cbNoFormsOfPayment?.isChecked == true) null else formsOfPayment
    }

    private fun getPaymentOptionSettings(): Checkout.PaymentOptionsDisplaySettings {
        val mode = if (binding?.cbPaymentOptionMode?.isChecked == true) {
            Checkout.PaymentOptionsDisplaySettings.PaymentOptionsDisplayMode.List(
                binding?.etPaymentListVisibleItems
                    ?.text?.toString()?.toIntOrNull() ?: 5
            )
        } else {
            Checkout.PaymentOptionsDisplaySettings.PaymentOptionsDisplayMode.BottomSheet
        }

        return Checkout.PaymentOptionsDisplaySettings(
            mode,
            binding?.etSelectedPgCode?.text?.toString()
        )
    }

    private fun View.hideSoftKeyboard() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(windowToken, 0)
    }
}