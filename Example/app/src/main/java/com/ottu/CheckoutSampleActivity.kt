package com.ottu

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.ottu.checkout.Checkout
import com.ottu.checkout.network.model.payment.ApiTransactionDetails
import com.ottu.checkout.ui.base.CheckoutSdkFragment
import com.ottu.checkout.ui.theme.CheckoutTheme
import com.ottu.databinding.ActivityCheckoutSampleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutSampleActivity : AppCompatActivity() {

    private var binding: ActivityCheckoutSampleBinding? = null

    private var checkoutFragment: CheckoutSdkFragment? = null

    private val amount by lazy { intent.extras?.getDouble(AMOUNT_PARAM) }
    private val merchantId by lazy { intent.extras?.getString(MERCHANT_ID_PARAM) }
    private val apiKey by lazy { intent.extras?.getString(API_KEY_PARAM) }
    private val showPaymentDetails by lazy {
        intent.extras?.getBoolean(
            SHOW_PAYMENT_DETAILS_PARAM,
            true
        ) ?: true
    }
    private val sessionId by lazy { intent.extras?.getString(SESSION_ID_PARAM) }

    private val appearanceLight by lazy {
        intent.extras?.getParcelable(
            THEME_APPEARANCE_LIGHT_PARAM,
        ) as? CheckoutTheme.Appearance?
    }

    private val appearanceDark by lazy {
        intent.extras?.getParcelable(
            THEME_APPEARANCE_DARK_PARAM,
        ) as? CheckoutTheme.Appearance?
    }

    private val formsOfPayment by lazy {
        intent.extras?.getParcelableArrayList<Checkout.FormsOfPayment>(
            FORMS_OF_PAYMENT_PARAM
        )
    }

    private val setupPreload by lazy {
        intent.extras?.getParcelable<ApiTransactionDetails>(
            SETUP_PAYLOAD_PARAM
        )
    }

    private val paymentOptionsDisplaySettings by lazy {
        intent.extras?.getParcelable(PAYMENT_OPTION_SETTINGS_PARAM)
            ?: Checkout.PaymentOptionsDisplaySettings.DEFAULT
    }

    private val withCrash by lazy {
        intent.extras?.getBoolean(
            WITH_CRASH_PARAM, false
        ) ?: false
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutSampleBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        if (savedInstanceState != null) {
            checkoutFragment =
                supportFragmentManager.findFragmentById(R.id.ottuPaymentView) as CheckoutSdkFragment?
        } else {
            initSdk()
        }

        setupViews()

        if (withCrash) {
            scope.launch {
                delay(20_000)
                throw RuntimeException("This is a test app crash + ${Math.random()}")
            }
        }
    }

    private fun setupViews() {
        binding?.apply {
            switchUiMode.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun initSdk() {
        if (merchantId.isNullOrEmpty()) {
            Toast.makeText(this, "Merchant ID cannot be null or empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (apiKey.isNullOrEmpty()) {
            Toast.makeText(this, "API Key cannot be null or empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (amount == null || amount!! <= 0) {
            Toast.makeText(this, "Amount cannot be null or <= 0", Toast.LENGTH_SHORT).show()
            return
        }

        if (sessionId.isNullOrEmpty()) {
            Toast.makeText(this, "SessionId cannot be null or <= 0", Toast.LENGTH_SHORT).show()
            return
        }

        initSdk(sessionId!!, formsOfPayment)
    }

    private fun initSdk(sessionId: String, formsOfPayment: List<Checkout.FormsOfPayment>?) {
        val theme = getCheckoutTheme()

        val builder = Checkout
            .Builder(merchantId!!, sessionId, apiKey!!, amount!!)
            .paymentOptionsDisplaySettings(paymentOptionsDisplaySettings)
            .formsOfPayments(formsOfPayment)
            .theme(theme)
            .logger(Checkout.Logger.INFO)
            .build()

        if (Checkout.isInitialized) {
            Checkout.release()
        }

        lifecycleScope.launch {
            runCatching {
                Checkout.init(
                    context = this@CheckoutSampleActivity,
                    builder = builder,
                    setupPreload = setupPreload,
                    successCallback = {
                        Log.e("TAG", "successCallback: $it")
                        showResultDialog(it)
                    },
                    cancelCallback = {
                        Log.e("TAG", "cancelCallback: $it")
                        showResultDialog(it)
                    },
                    errorCallback = { errorData, throwable ->
                        Log.e("TAG", "errorCallback: $errorData")
                        showResultDialog(errorData, throwable)
                    },
                )
            }.onSuccess {
                checkoutFragment = it
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.ottuPaymentView, it)
                    .commit()
            }.onFailure {
                showErrorDialog(it)
            }
        }
    }

    private fun getCheckoutTheme(): CheckoutTheme {
        return CheckoutTheme(
            uiMode = CheckoutTheme.UiMode.AUTO,
            showPaymentDetails = showPaymentDetails,
            appearanceLight = appearanceLight,
            appearanceDark = appearanceDark,
        )
    }

    private fun showResultDialog(result: JSONObject?, throwable: Throwable? = null) {
        val sb = StringBuilder()

        result?.let {
            sb.apply {
                append(("Status : " + result.opt("status")) + "\n")
                append(("Message : " + result.opt("message")) + "\n")
                append(("Session id : " + result.opt("session_id")) + "\n")
                append(("operation : " + result.opt("operation")) + "\n")
                append(("Reference number : " + result.opt("reference_number")) + "\n")
                append(("Challenge Occurred : " + result.opt("challenge_occurred")) + "\n")
                append(("Form of payment: " + result.opt("form_of_payment")) + "\n")
            }
        } ?: run {
            sb.append(throwable?.message ?: "Unknown Error")
        }

        AlertDialog.Builder(this)
            .setTitle("Order Information")
            .setMessage(sb)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorDialog(throwable: Throwable? = null) {
        if (throwable is SecurityException) return

        AlertDialog.Builder(this)
            .setTitle(R.string.failed)
            .setMessage(throwable?.message ?: "Unknown Error")
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                finish()
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        if (isFinishing) {
            Checkout.release()
        }
    }

    companion object {
        private const val AMOUNT_PARAM = "AMOUNT_PARAM"
        private const val MERCHANT_ID_PARAM = "MERCHANT_ID_PARAM"
        private const val API_KEY_PARAM = "API_KEY_PARAM"
        private const val CUSTOMER_ID_PARAM = "CUSTOMER_ID_PARAM"
        private const val SESSION_ID_PARAM = "SESSION_ID_PARAM"
        private const val FORMS_OF_PAYMENT_PARAM = "FORMS_OF_PAYMENT_PARAM"
        private const val SHOW_PAYMENT_DETAILS_PARAM = "SHOW_PAYMENT_DETAILS_PARAM"
        private const val SETUP_PAYLOAD_PARAM = "SETUP_PAYLOAD_PARAM"
        private const val PAYMENT_OPTION_SETTINGS_PARAM = "PAYMENT_OPTION_SETTINGS_PARAM"
        private const val THEME_APPEARANCE_LIGHT_PARAM = "THEME_APPEARANCE_LIGHT_PARAM"
        private const val THEME_APPEARANCE_DARK_PARAM = "THEME_APPEARANCE_DARK_PARAM"

        private const val WITH_CRASH_PARAM = "WITH_CRASH_PARAM"

        fun openActivity(
            context: Context,
            sessionId: String,
            amount: Double,
            merchantId: String,
            apiKey: String,
            customerId: String?,
            formsOfPayment: ArrayList<Checkout.FormsOfPayment>?,
            showPaymentDetails: Boolean,
            preloadPayload: ApiTransactionDetails? = null,
            paymentOptionsDisplaySettings: Checkout.PaymentOptionsDisplaySettings? = null,
            appearanceLight: CheckoutTheme.Appearance? = null,
            appearanceDark: CheckoutTheme.Appearance? = null,
            withCrash: Boolean = false
        ) {
            val intent = Intent(context, CheckoutSampleActivity::class.java).apply {
                putExtra(AMOUNT_PARAM, amount)
                putExtra(MERCHANT_ID_PARAM, merchantId)
                putExtra(API_KEY_PARAM, apiKey)
                putExtra(SESSION_ID_PARAM, sessionId)
                putExtra(CUSTOMER_ID_PARAM, customerId)
                putExtra(SHOW_PAYMENT_DETAILS_PARAM, showPaymentDetails)
                putParcelableArrayListExtra(FORMS_OF_PAYMENT_PARAM, formsOfPayment)
                putExtra(SETUP_PAYLOAD_PARAM, preloadPayload)
                putExtra(PAYMENT_OPTION_SETTINGS_PARAM, paymentOptionsDisplaySettings)
                putExtra(THEME_APPEARANCE_LIGHT_PARAM, appearanceLight)
                putExtra(THEME_APPEARANCE_DARK_PARAM, appearanceDark)
                putExtra(WITH_CRASH_PARAM, withCrash)
            }

            context.startActivity(intent)
        }
    }
}