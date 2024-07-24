package com.ottu.customization

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ottu.checkout.ui.theme.CheckoutTheme
import com.ottu.checkout.ui.theme.style.Margins
import com.ottu.databinding.DialogThemeCustomizationBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

fun changeTextStyle(
    context: Context,
    onConfirm: (text: CheckoutTheme.Text) -> Unit,
) {
    var textStyle = CheckoutTheme.Text()

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(TEXT_COMPONENT_COLOR, "Text Color"))
        add(ComponentItem(TEXT_COMPONENT_FONT_TYPE, "Text Font"))
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(context))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(context)
            rvParams.adapter = ComponentsAdapter(params) {
                when (it.tag) {
                    TEXT_COMPONENT_COLOR -> {
                        context.openColorPickerView { color ->
                            textStyle = textStyle.copy(textColor = CheckoutTheme.Color(color.color))
                        }
                    }

                    TEXT_COMPONENT_FONT_TYPE -> {
                        context.openTextFontTypeDialog { fontType ->
                            textStyle = textStyle.copy(fontType = fontType)
                        }
                    }
                }
            }
        }

    MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .setTitle("Text Style")
        .setPositiveButton("Confirm") { dialog, which -> onConfirm.invoke(textStyle) }
        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        .show()
}

fun changeTextFieldStyle(
    context: Context,
    onConfirm: (dialog: CheckoutTheme.TextField) -> Unit,
) {
    var textFieldStyle = CheckoutTheme.TextField()

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(TEXT_FIELD_COMPONENT_BACKGROUND_COLOR, "Background Color"))
        add(ComponentItem(TEXT_FIELD_COMPONENT_PRIMARY_COLOR, "Primary Color"))
        add(ComponentItem(TEXT_FIELD_COMPONENT_FOCUSED_COLOR, "Focused Color"))
        add(ComponentItem(TEXT_FIELD_COMPONENT_INPUT_TEXT, "Input Text"))
        add(ComponentItem(TEXT_FIELD_COMPONENT_ERROR_TEXT, "Error Text"))
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(context))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(context)
            rvParams.adapter = ComponentsAdapter(params) {
                when (it.tag) {
                    TEXT_FIELD_COMPONENT_BACKGROUND_COLOR -> {
                        context.openColorPickerView { color ->
                            textFieldStyle =
                                textFieldStyle.copy(background = CheckoutTheme.Color(color = color.color))
                        }
                    }

                    TEXT_FIELD_COMPONENT_PRIMARY_COLOR -> {
                        context.openColorPickerView { color ->
                            textFieldStyle =
                                textFieldStyle.copy(primaryColor = CheckoutTheme.Color(color = color.color))
                        }
                    }

                    TEXT_FIELD_COMPONENT_FOCUSED_COLOR -> {
                        context.openColorPickerView { color ->
                            textFieldStyle =
                                textFieldStyle.copy(focusedColor = CheckoutTheme.Color(color = color.color))
                        }
                    }

                    TEXT_FIELD_COMPONENT_INPUT_TEXT -> {
                        changeTextStyle(context) { text ->
                            textFieldStyle = textFieldStyle.copy(text = text)
                        }
                    }

                    TEXT_FIELD_COMPONENT_ERROR_TEXT -> {
                        changeTextStyle(context) { text ->
                            textFieldStyle = textFieldStyle.copy(error = text)
                        }
                    }
                }
            }
        }

    MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .setTitle("Text Field Style")
        .setPositiveButton("Confirm") { dialog, which -> onConfirm.invoke(textFieldStyle) }
        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        .show()
}

fun changeButtonStyle(
    context: Context,
    onConfirm: (dialog: CheckoutTheme.Button) -> Unit,
) {
    var buttonStyle = CheckoutTheme.Button()

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(BUTTON_COMPONENT_COLOR, "Button Color"))
        add(ComponentItem(BUTTON_COMPONENT_TEXT_COLOR, "Button Text"))
        add(ComponentItem(BUTTON_COMPONENT_FONT_TYPE, "Button Text Font Type"))
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(context))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(context)
            rvParams.adapter = ComponentsAdapter(params) {
                when (it.tag) {
                    BUTTON_COMPONENT_COLOR -> {
                        context.openColorPickerView { color ->
                            buttonStyle =
                                buttonStyle.copy(rippleColor = CheckoutTheme.RippleColor(color = color.color))
                        }
                    }

                    BUTTON_COMPONENT_TEXT_COLOR -> {
                        context.openColorPickerView { color ->
                            buttonStyle =
                                buttonStyle.copy(textColor = CheckoutTheme.Color(color.color))
                        }
                    }

                    BUTTON_COMPONENT_FONT_TYPE -> {
                        context.openTextFontTypeDialog { fontType ->
                            buttonStyle =
                                buttonStyle.copy(fontType = fontType)
                        }
                    }
                }
            }
        }

    MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .setTitle("Button Style")
        .setPositiveButton("Confirm") { dialog, which -> onConfirm.invoke(buttonStyle) }
        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        .show()
}

fun changeSwitchStyle(
    context: Context,
    onConfirm: (dialog: CheckoutTheme.Switch) -> Unit,
) {
    var switchStyle = CheckoutTheme.Switch()

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(SWITCH_COMPONENT_CHECKED_THUMB_COLOR, "Checked Thumb Color"))
        add(ComponentItem(SWITCH_COMPONENT_UNCHECKED_THUMB_COLOR, "Unchecked Thumb Color"))
        add(ComponentItem(SWITCH_COMPONENT_CHECKED_TRACK_COLOR, "Checked Track Color"))
        add(ComponentItem(SWITCH_COMPONENT_UNCHECKED_TRACK_COLOR, "Unchecked Track Color"))
        add(
            ComponentItem(
                SWITCH_COMPONENT_CHECKED_DECORATION_COLOR,
                "Checked Decoration Color"
            )
        )
        add(
            ComponentItem(
                SWITCH_COMPONENT_UNCHECKED_DECORATION_COLOR,
                "Unchecked Decoration Color"
            )
        )
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(context))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(context)
            rvParams.adapter = ComponentsAdapter(params) {
                when (it.tag) {
                    SWITCH_COMPONENT_CHECKED_THUMB_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(checkedThumbTintColor = color.color)
                        }
                    }

                    SWITCH_COMPONENT_UNCHECKED_THUMB_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(uncheckedThumbTintColor = color.color)
                        }
                    }

                    SWITCH_COMPONENT_CHECKED_TRACK_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(checkedTrackTintColor = color.color)
                        }
                    }

                    SWITCH_COMPONENT_UNCHECKED_TRACK_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(uncheckedTrackTintColor = color.color)
                        }
                    }

                    SWITCH_COMPONENT_CHECKED_DECORATION_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(checkedTrackDecorationColor = color.color)
                        }
                    }

                    SWITCH_COMPONENT_UNCHECKED_DECORATION_COLOR -> {
                        context.openColorPickerView { color ->
                            switchStyle =
                                switchStyle.copy(uncheckedTrackDecorationColor = color.color)
                        }
                    }
                }
            }
        }

    MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .setTitle("Switch Style")
        .setPositiveButton("Confirm") { dialog, which -> onConfirm.invoke(switchStyle) }
        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        .show()
}

fun Context.openTextFontTypeDialog(
    onConfirm: (fontType: Int) -> Unit,
) {

    var fontType: Int = 0
    var dialog: AlertDialog? = null

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(FONT_TYPE_INTER, "Inter"))
        add(ComponentItem(FONT_TYPE_MONTSERRAT, "Montserrat"))
        add(ComponentItem(FONT_TYPE_MONTSERRAT_SEMIBOLD, "Montserrat SemiBold"))
        add(ComponentItem(FONT_TYPE_POPPINS, "Poppins"))
        add(ComponentItem(FONT_TYPE_POPPINS_SEMIBOLD, "Poppins SemiBold"))
        add(ComponentItem(FONT_TYPE_ROBOTO, "Roboto"))
        add(ComponentItem(FONT_TYPE_ROBOTO_BOLD, "Roboto Bold"))
        add(ComponentItem(FONT_TYPE_SF_PRO, "SF Pro"))
        add(ComponentItem(FONT_TYPE_SF_PRO_BOLD, "SF Pro Bold"))
    }

    val adapter = ComponentsAdapter(params) {
        fontType = when (it.tag) {
            FONT_TYPE_INTER -> com.ottu.checkout.R.font.inter_regular
            FONT_TYPE_MONTSERRAT -> com.ottu.checkout.R.font.montserrat_regular
            FONT_TYPE_MONTSERRAT_SEMIBOLD -> com.ottu.checkout.R.font.montserrat_semibold
            FONT_TYPE_POPPINS -> com.ottu.checkout.R.font.poppins_regular
            FONT_TYPE_POPPINS_SEMIBOLD -> com.ottu.checkout.R.font.poppins_semibold
            FONT_TYPE_ROBOTO -> com.ottu.checkout.R.font.roboto_regular
            FONT_TYPE_ROBOTO_BOLD -> com.ottu.checkout.R.font.roboto_bold
            FONT_TYPE_SF_PRO -> com.ottu.checkout.R.font.sf_pro_regular
            FONT_TYPE_SF_PRO_BOLD -> com.ottu.checkout.R.font.sf_pro_bold
            else -> 0
        }

        onConfirm.invoke(fontType)
        dialog?.dismiss()
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(this))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(this@openTextFontTypeDialog)
            rvParams.adapter = adapter
        }

    dialog = MaterialAlertDialogBuilder(this)
        .setView(binding.root)
        .setTitle("Font Type")
        .show()
}

fun changeMargins(
    context: Context,
    onConfirm: (margins: Margins) -> Unit,
) {
    var marginsStyle = Margins()
    val marginsRange = IntRange(0, 30)

    val params = mutableListOf<ComponentItem>().apply {
        add(ComponentItem(MARGIN_LEFT, "Margin Left"))
        add(ComponentItem(MARGIN_TOP, "Margin Top"))
        add(ComponentItem(MARGIN_RIGHT, "Margin Right"))
        add(ComponentItem(MARGIN_BOTTOM, "Margin Bottom"))
    }

    val binding = DialogThemeCustomizationBinding
        .inflate(LayoutInflater.from(context))
        .apply {
            rvParams.layoutManager = LinearLayoutManager(context)
            rvParams.adapter = ComponentsAdapter(params) {
                when (it.tag) {
                    MARGIN_LEFT -> {
                        context.openNumberPicker(marginsRange) { margin ->
                            marginsStyle = marginsStyle.copy(left = margin)
                        }
                    }

                    MARGIN_TOP -> {
                        context.openNumberPicker(marginsRange) { margin ->
                            marginsStyle = marginsStyle.copy(top = margin)
                        }
                    }

                    MARGIN_RIGHT -> {
                        context.openNumberPicker(marginsRange) { margin ->
                            marginsStyle = marginsStyle.copy(right = margin)
                        }
                    }

                    MARGIN_BOTTOM -> {
                        context.openNumberPicker(marginsRange) { margin ->
                            marginsStyle = marginsStyle.copy(bottom = margin)
                        }
                    }
                }
            }
        }

    MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .setTitle("Margins Style")
        .setPositiveButton("Confirm") { dialog, which -> onConfirm.invoke(marginsStyle) }
        .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        .show()
}

fun Context.openNumberPicker(range: IntRange, onConfirm: (value: Int) -> Unit) {
    val dialog = NumberPickerDialog(
        this, range.first, range.last
    ) { value ->
        onConfirm.invoke(value)
    }
    dialog.show()
}

fun Context.openColorPickerView(onConfirm: (color: ColorEnvelope) -> Unit) {
    ColorPickerDialog.Builder(this)
        .setTitle("ColorPicker Dialog")
        .setPreferenceName("MyColorPickerDialog")
        .setPositiveButton("Confirm", object : ColorEnvelopeListener {
            override fun onColorSelected(color: ColorEnvelope, fromUser: Boolean) {
                onConfirm.invoke(color)
            }

        })
        .setNegativeButton("Cancel") { dialog, which -> dialog?.dismiss() }
        .show()
}