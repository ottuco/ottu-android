package com.ottu.customization

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.ottu.checkout.ui.theme.CheckoutTheme
import com.ottu.customization.ThemeCustomizationActivityResultContract.Param.RESULT_APPEARANCE_DARK_PARAM
import com.ottu.customization.ThemeCustomizationActivityResultContract.Param.RESULT_APPEARANCE_LIGHT_PARAM
import com.ottu.databinding.ActivityThemeCustomizationBinding


class ThemeCustomizationActivity : AppCompatActivity() {

    private var binding: ActivityThemeCustomizationBinding? = null

    private var appearanceLight: CheckoutTheme.Appearance = CheckoutTheme.Appearance()
    private var appearanceDark: CheckoutTheme.Appearance = CheckoutTheme.Appearance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThemeCustomizationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.setupViews()
    }

    private fun ActivityThemeCustomizationBinding.setupViews() {
        btnDefaults.setOnClickListener {
            appearanceLight = CheckoutTheme.Appearance()
            appearanceDark = CheckoutTheme.Appearance()
        }

        btnSave.setOnClickListener {
            val intent = Intent()
                .putExtras(
                    bundleOf(
                        RESULT_APPEARANCE_LIGHT_PARAM to appearanceLight,
                        RESULT_APPEARANCE_DARK_PARAM to appearanceDark
                    )
                )

            setResult(RESULT_OK, intent)
            finish()
        }

        rvLightComponents.layoutManager = LinearLayoutManager(this@ThemeCustomizationActivity)
        rvDarkComponents.layoutManager = LinearLayoutManager(this@ThemeCustomizationActivity)

        rvLightComponents.adapter = ComponentsAdapter(
            items = getComponents()
        ) { innerComponentItem ->
            onInnerComponentClick(innerComponentItem, false)
        }

        rvDarkComponents.adapter = ComponentsAdapter(
            items = getComponents()
        ) { innerComponentItem ->
            onInnerComponentClick(innerComponentItem, true)
        }
    }

    private fun onInnerComponentClick(componentItem: ComponentItem, isDark: Boolean) {
        when (componentItem.tag) {
            THEME_MAIN_TITLE_TEXT -> {
                handleMainTitleText(isDark)
            }

            THEME_TITLE_TEXT -> {
                handleTitleText(isDark)
            }

            THEME_SUBTITLE_TEXT -> {
                handleSubtitleText(isDark)
            }

            THEME_FEES_TITLE_TEXT -> {
                handleFeesTitleText(isDark)
            }

            THEME_FEES_SUBTITLE_TEXT -> {
                handleFeesSubtitleText(isDark)
            }

            THEME_DATA_LABEL_TEXT -> {
                handleDataLabelText(isDark)
            }

            THEME_DATA_VALUE_TEXT -> {
                handleDataValueText(isDark)
            }

            THEME_ERROR_MESSAGE_TEXT -> {
                handleErrorMessageText(isDark)
            }

            THEME_INPUT_FIELD -> {
                handleInputField(isDark)
            }

            THEME_BUTTON -> {
                handleButton(isDark)
            }

            THEME_SELECTOR_BUTTON -> {
                handleSelectorButton(isDark)
            }

            THEME_BACK_BUTTON -> {
                handleBackButton(isDark)
            }

            THEME_SWITCH -> {
                handleSwitch(isDark)
            }

            THEME_SDK_MARGINS -> {
                handleMargins(isDark)
            }

            THEME_SDK_BACKGROUND_COLOR -> {
                handleSdkBackgroundColor(isDark)
            }

            THEME_SDK_MODAL_BACKGROUND_COLOR -> {
                handleModalBackgroundColor(isDark)
            }

            THEME_SDK_SELECTOR_ITEM_BACKGROUND_COLOR -> {
                handleSelectorItemBackgroundColor(isDark)
            }

            THEME_SDK_SELECTOR_ICON_COLOR -> {
                handleSelectorIconColor(isDark)
            }

            THEME_SDK_SAVE_PHONE_ICON_COLOR -> {
                handleSavePhoneIconColor(isDark)
            }
        }
    }

    private fun handleMainTitleText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(mainTitleText = it)
            } else {
                appearanceLight = appearanceLight.copy(mainTitleText = it)
            }
        }
    }

    private fun handleTitleText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(titleText = it)
            } else {
                appearanceLight = appearanceLight.copy(titleText = it)
            }
        }
    }

    private fun handleSubtitleText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(subtitleText = it)
            } else {
                appearanceLight = appearanceLight.copy(subtitleText = it)
            }
        }
    }

    private fun handleFeesTitleText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(feesTitleText = it)
            } else {
                appearanceLight = appearanceLight.copy(feesTitleText = it)
            }
        }
    }

    private fun handleFeesSubtitleText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(feesSubtitleText = it)
            } else {
                appearanceLight = appearanceLight.copy(feesSubtitleText = it)
            }
        }
    }

    private fun handleDataLabelText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(dataLabelText = it)
            } else {
                appearanceLight = appearanceLight.copy(dataLabelText = it)
            }
        }
    }

    private fun handleDataValueText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(dataValueText = it)
            } else {
                appearanceLight = appearanceLight.copy(dataValueText = it)
            }
        }
    }

    private fun handleErrorMessageText(isDark: Boolean) {
        changeTextStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(errorMessageText = it)
            } else {
                appearanceLight = appearanceLight.copy(errorMessageText = it)
            }
        }
    }

    private fun handleInputField(isDark: Boolean) {
        changeTextFieldStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(inputTextField = it)
            } else {
                appearanceLight = appearanceLight.copy(inputTextField = it)
            }
        }
    }

    private fun handleButton(isDark: Boolean) {
        changeButtonStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(button = it)
            } else {
                appearanceLight = appearanceLight.copy(button = it)
            }
        }
    }

    private fun handleSelectorButton(isDark: Boolean) {
        changeButtonStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(selectorButton = it)
            } else {
                appearanceLight = appearanceLight.copy(selectorButton = it)
            }
        }
    }

    private fun handleBackButton(isDark: Boolean) {
        openColorPickerView  {
            if (isDark) {
                appearanceDark = appearanceDark.copy(backButton = CheckoutTheme.RippleColor(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(backButton = CheckoutTheme.RippleColor(color = it.color))
            }
        }
    }

    private fun handleSwitch(isDark: Boolean) {
        changeSwitchStyle(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(switch = it)
            } else {
                appearanceLight = appearanceLight.copy(switch = it)
            }
        }
    }

    private fun handleSdkBackgroundColor(isDark: Boolean) {
        openColorPickerView {
            if (isDark) {
                appearanceDark = appearanceDark.copy(sdkBackgroundColor = CheckoutTheme.Color(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(sdkBackgroundColor = CheckoutTheme.Color(color = it.color))
            }
        }
    }

    private fun handleModalBackgroundColor(isDark: Boolean) {
        openColorPickerView {
            if (isDark) {
                appearanceDark = appearanceDark.copy(modalBackgroundColor = CheckoutTheme.Color(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(modalBackgroundColor = CheckoutTheme.Color(color = it.color))
            }
        }
    }

    private fun handleSelectorItemBackgroundColor(isDark: Boolean) {
        openColorPickerView {
            if (isDark) {
                appearanceDark = appearanceDark.copy(paymentItemBackgroundColor = CheckoutTheme.Color(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(paymentItemBackgroundColor = CheckoutTheme.Color(color = it.color))
            }
        }
    }

    private fun handleSavePhoneIconColor(isDark: Boolean) {
        openColorPickerView {
            if (isDark) {
                appearanceDark = appearanceDark.copy(savePhoneNumberIconColor = CheckoutTheme.Color(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(savePhoneNumberIconColor = CheckoutTheme.Color(color = it.color))
            }
        }
    }

    private fun handleSelectorIconColor(isDark: Boolean) {
        openColorPickerView {
            if (isDark) {
                appearanceDark = appearanceDark.copy(selectorIconColor = CheckoutTheme.Color(color = it.color))
            } else {
                appearanceLight = appearanceLight.copy(selectorIconColor = CheckoutTheme.Color(color = it.color))
            }
        }
    }

    private fun handleMargins(isDark: Boolean) {
        changeMargins(this) {
            if (isDark) {
                appearanceDark = appearanceDark.copy(margins = it)
            } else {
                appearanceLight = appearanceLight.copy(margins = it)
            }
        }
    }

}
