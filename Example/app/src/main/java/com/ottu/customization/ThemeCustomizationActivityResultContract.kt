package com.ottu.customization

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ottu.checkout.ui.theme.CheckoutTheme

class ThemeCustomizationActivityResultContract :
    ActivityResultContract<Unit, Pair<CheckoutTheme.Appearance?, CheckoutTheme.Appearance?>?>() {

    object Param {
        const val RESULT_APPEARANCE_LIGHT_PARAM = "RESULT_APPEARANCE_LIGHT_PARAM"
        const val RESULT_APPEARANCE_DARK_PARAM = "RESULT_APPEARANCE_DARK_PARAM"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, ThemeCustomizationActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<CheckoutTheme.Appearance?, CheckoutTheme.Appearance?>? {
        val appearanceLight = intent?.getParcelableExtra<CheckoutTheme.Appearance>(Param.RESULT_APPEARANCE_LIGHT_PARAM)
        val appearanceDark = intent?.getParcelableExtra<CheckoutTheme.Appearance>(Param.RESULT_APPEARANCE_DARK_PARAM)

        return appearanceLight to appearanceDark
    }

}