package com.ottu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.compose.AndroidFragment
import com.ottu.checkout.ui.payment.view.PaymentButton
import com.ottu.checkout.R as OttuR

@Composable
fun CheckoutSampleScreen(
    checkoutFragment: Fragment?,
    modifier: Modifier = Modifier,
    splitPayButton: Boolean = false,
) {
    val scrollState = rememberScrollState()
    val teal700 = colorResource(id = R.color.teal_700)

    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // tvTitle
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Customers Application",
            color = teal700,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        // If splitPayButton is true, PaymentButton is placed above the checkout container
        if (splitPayButton) {
            Spacer(modifier = Modifier.height(18.dp))
            AndroidView(
                factory = { context ->
                    PaymentButton(context)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // tvAboveContent
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Some users UI elements, Some users UI elements, Some users UI elements, Some users UI elements, Some users UI elements",
            color = teal700,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
        )

        // ottu_checkout_container -> AndroidFragment Host
        Spacer(modifier = Modifier.height(15.dp))
        AndroidFragment<OttuCheckoutHostFragment>(
            modifier = Modifier.fillMaxWidth(),
            onUpdate = { host -> host.showCheckout(checkoutFragment) },
        )

        // If splitPayButton is true, show nestedWidget below ottu_checkout_container
        if (splitPayButton) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = OttuR.color.grayScale18))
                    .padding(12.dp),
            ) {
                Text(
                    text = "That is a text posted by merchat. You may add there any type of View except List containers.",
                    color = teal700,
                    fontSize = 14.sp,
                )
            }
        } else {
            // If splitPayButton is false, PaymentButton is placed below the checkout container
            Spacer(modifier = Modifier.height(20.dp))
            AndroidView(
                factory = { context ->
                    PaymentButton(context)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // tvBelowContent
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Some users UI elements, Some users UI elements, Some users UI elements, Some users UI elements, Some users UI elements",
            color = teal700,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Host Fragment used inside AndroidFragment compose container to manage the Ottu Checkout Fragment.
 */
class OttuCheckoutHostFragment : Fragment() {
    private val containerId by lazy { ViewCompat.generateViewId() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FrameLayout(requireContext()).apply {
            id = containerId
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
        }
    }

    fun showCheckout(checkoutFragment: Fragment?) {
        val fragment = checkoutFragment ?: return
        if (childFragmentManager.findFragmentById(containerId) != fragment) {
            childFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss()
        }
    }
}
