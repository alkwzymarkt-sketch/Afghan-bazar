package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.model.AppMode
import com.example.ui.admin.AlkoziAdminScreen
import com.example.ui.theme.AlkoziMarketTheme
import com.example.viewmodel.MarketViewModel

/**
 * Alkozi Admin (الکوزی ادمین)
 *
 * Standalone separate application for the platform owner and executive administrators.
 * Directly manages Alkozi Market, communicates with the live online cloud server,
 * syncs all 34 Afghan provinces, controls seller subscriptions, approves vendor shops,
 * and tracks real-time telecom payments.
 */
class AlkoziAdminActivity : ComponentActivity() {

    private val viewModel: MarketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set mode explicitly to ADMIN for this dedicated application
        viewModel.switchAppMode(AppMode.ADMIN)

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current
            val layoutDirection = if (uiState.appLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            LaunchedEffect(uiState.authSuccessToast) {
                uiState.authSuccessToast?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            AlkoziMarketTheme {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    AlkoziAdminScreen(
                        viewModel = viewModel,
                        uiState = uiState
                    )
                }
            }
        }
    }
}
