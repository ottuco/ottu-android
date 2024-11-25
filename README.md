
# Ottu Checkout

The Ottu Checkout is an Android SDK which makes it quick and easy to build an excellent payment experience in your Android app. We provide powerful and customizable UI screens and elements that can be used out-of-the-box to collect your user's payment details. We also expose the low-level APIs that power those UIs so that you can build fully custom experiences.

## Features

**Simplified security**: We make it simple for you to collect sensitive data such as credit card numbers and remain PCI compliant. This means the sensitive data is sent directly to Ottu instead of passing through your server.

**Localized**: We support the following localizations: English, Arabic.

#### Privacy

The Ottu Checkout SDK collects data to help us improve our products and prevent fraud. This data is never used for advertising and is not rented, sold, or given to advertisers.

## Requirements

The OttuCheckout requires IDE to develop android app. The SDK is compatible with the minimum Android 8 (Android API 26) and above.

## Getting started

To initialize the SDK you need to create session token. 
You can create session token with our public API [Click here](https://app.apiary.io/iossdk2/editor) to see more detail about our public API.
    
Installation
==========================

#### Installation with dependecy

Put below dependency into your gradle

```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
    
dependencies {
       implementation("com.github.ottuco:ottu-android-checkout:1.0.6")
}
```

Below is the sample code of how you can use Ottu Payment SDK.

```kotlin

    val theme = getCheckoutTheme()

    val builder = Checkout
            .Builder(merchantId, sessionId, apiKey, amount)
            .formsOfPayments(formsOfPayment)
            .theme(theme)
            .logger(Checkout.Logger.INFO)
            .build()

        if (Checkout.isInitialized) {
            Checkout.release()
        }

        checkoutFragment = Checkout.init(
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
                if (errorData != null) {
                    showResultDialog(errorData)
                }
            },
        )

```

Get payment result in 3 callbacks: `successCallback`, `cancelCallback` or `errorCallback`, depending on a result.
Here is an example of the result dialog :

```kotlin
    private fun showResultDialog(result: JSONObject) {
        val sb = StringBuilder()
        sb.append(("Status : " + result.opt("status")) + "\n")
        sb.append(("Message : " + result.opt("message")) + "\n")
        sb.append(("Session id : " + result.opt("session_id")) + "\n")
        sb.append(("Order no : " + result.opt("order_no"]))+ "\n")
        sb.append(("operation : " + result.opt("operation")) + "\n")
        sb.append(("Reference number : " + result.opt("reference_number")) + "\n")
        sb.append(("Challenge Occurred : " + result.opt("challenge_occurred")) + "\n")
        sb.append(("Form of payment: " + result.opt("form_of_payment")) + "\n")

        AlertDialog.Builder(this)
            .setTitle("Order Information")
            .setMessage(sb)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
    
```

## ProGuard

 You may need to include the following lines in your progard-rules.pro file if enable progard or minifyEnble.
```java
-keep class Ottu** { *; }
```

## Licenses

- [OttuCheckout License](LICENSE)
