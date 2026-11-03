package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppMode
import com.example.model.AuditLog
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Shop
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.AlkoziDarkGreen
import com.example.ui.theme.AlkoziDeepGreen
import com.example.ui.theme.AlkoziGold
import com.example.ui.theme.AlkoziGoldDark
import com.example.ui.theme.AlkoziLightGreen
import com.example.viewmodel.MarketUiState
import com.example.viewmodel.MarketViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlkoziAdminScreen(
    viewModel: MarketViewModel,
    uiState: MarketUiState,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val formatter = remember { DecimalFormat("#,###") }

    val tabs = listOf(
        "داشبورد و اشتراک‌ها",
        "مدیریت تبلیغات (${uiState.inAppAdvertisements.size})",
        "مدیریت دکان‌ها (${uiState.shops.size})",
        "کاربران و نقش‌ها (${uiState.allUsers.size})",
        "سفارشات و درگاه‌ها",
        "گزارشات و لاگ‌ها (${uiState.auditLogs.size})"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F5))
            .statusBarsPadding()
            .testTag("alkozi_admin_screen")
    ) {
        // --- Admin App Header ---
        Surface(
            color = AlkoziDeepGreen,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AlkoziGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin Icon",
                                tint = AlkoziDeepGreen,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Alkozi Admin",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = AlkoziGold,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "سوپر ادمین",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlkoziDeepGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "سامانه مدیریت مرکزی پلتفرم و ۳۴ ولایت",
                                fontSize = 11.sp,
                                color = AlkoziLightGreen
                            )
                        }
                    }

                    // Live Platform Online Badge & Quick Open Market Intent
                    val context = androidx.compose.ui.platform.LocalContext.current
                    Surface(
                        color = AlkoziGold,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .clickable {
                                try {
                                    val intent = android.content.Intent(context, com.example.MainActivity::class.java)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Keep in admin
                                }
                            }
                            .testTag("open_market_app_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "مشاهده بازار آنلاین",
                                tint = AlkoziDeepGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مشاهده بازار",
                                color = AlkoziDeepGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- Live Online Server Synchronization Hub Banner ---
        var showServerUrlDialog by remember { mutableStateOf(false) }
        var serverUrlInput by remember(uiState.serverStatus.serverUrl) {
            mutableStateOf(uiState.serverStatus.serverUrl)
        }

        if (showServerUrlDialog) {
            AlertDialog(
                onDismissRequest = { showServerUrlDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = null,
                            tint = AlkoziDeepGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تنظیمات اتصال سرور مرکزی",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlkoziDeepGreen
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            text = "آدرس اینترنتی (URL) سرور آنلاین متصل به پایگاه داده مرکزی Alkozi را وارد کنید:",
                            fontSize = 12.sp,
                            color = Color(0xFF555555),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = serverUrlInput,
                            onValueChange = { serverUrlInput = it },
                            label = { Text("آدرس سرور آنلاین (REST API / HTTPS)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("server_url_input_field"),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "وضعیت کنونی: ${if (uiState.serverStatus.isConnected) "متصل به کلاود (پروتکل امن HTTPS)" else "آفلاین / در حال بررسی"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (uiState.serverStatus.isConnected) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateServerAddress(serverUrlInput)
                            showServerUrlDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlkoziDeepGreen)
                    ) {
                        Text("ذخیره و اتصال مجدد")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showServerUrlDialog = false }) {
                        Text("انصراف")
                    }
                }
            )
        }

        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (uiState.serverStatus.isConnected) Color(0xFF00C853) else Color(0xFFD50000))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (uiState.serverStatus.isConnected) "متصل به سرور ابری آنلاین" else "در انتظار اتصال سرور",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.serverStatus.isConnected) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0xFFF1F8E9),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${uiState.serverStatus.pingLatencyMs}ms",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "همگام‌سازی ۳۴ ولایت • ${uiState.serverStatus.serverVersion}",
                            fontSize = 10.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Change Server URL Button
                    IconButton(
                        onClick = { showServerUrlDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("admin_server_settings_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "تنظیمات سرور",
                            tint = AlkoziDeepGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Sync Now Button
                    Button(
                        onClick = { viewModel.syncWithOnlineServerNow() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.serverStatus.isSyncing) AlkoziGoldDark else AlkoziLightGreen,
                            contentColor = AlkoziDeepGreen
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        enabled = !uiState.serverStatus.isSyncing,
                        modifier = Modifier.testTag("admin_sync_now_btn")
                    ) {
                        Icon(
                            imageVector = if (uiState.serverStatus.isSyncing) Icons.Default.Sync else Icons.Default.CloudSync,
                            contentDescription = "همگام‌سازی",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (uiState.serverStatus.isSyncing) "در حال اتصال..." else "همگام‌سازی",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // --- Tabs Navigation ---
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = AlkoziDarkGreen,
            edgePadding = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == index) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (selectedTab == index) AlkoziDarkGreen else Color(0xFF666666)
                        )
                    }
                )
            }
        }

        // --- Tab Contents ---
        when (selectedTab) {
            0 -> DashboardAndSubscriptionTab(viewModel = viewModel, uiState = uiState, formatter = formatter)
            1 -> AdsManagementTab(viewModel = viewModel, uiState = uiState, formatter = formatter)
            2 -> ShopManagementTab(viewModel = viewModel, uiState = uiState, formatter = formatter)
            3 -> UserManagementTab(viewModel = viewModel, uiState = uiState)
            4 -> OrdersAndGatewaysTab(viewModel = viewModel, uiState = uiState, formatter = formatter)
            5 -> AuditLogsTab(uiState = uiState)
        }
    }
}

// =========================================================================
// TAB 1: DASHBOARD & SUBSCRIPTION MASTER SWITCH (بخش حیاتی)
// =========================================================================

@Composable
private fun DashboardAndSubscriptionTab(
    viewModel: MarketViewModel,
    uiState: MarketUiState,
    formatter: DecimalFormat
) {
    val settings = uiState.platformSettings

    var monthlyInput by remember(settings.monthlySubscriptionFeeAfn) {
        mutableStateOf(settings.monthlySubscriptionFeeAfn.toString())
    }
    var quarterlyInput by remember(settings.quarterlySubscriptionFeeAfn) {
        mutableStateOf(settings.quarterlySubscriptionFeeAfn.toString())
    }
    var yearlyInput by remember(settings.yearlySubscriptionFeeAfn) {
        mutableStateOf(settings.yearlySubscriptionFeeAfn.toString())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. KPI Stats Cards Grid
        item {
            Text(
                text = "شاخص‌های کلیدی پلتفرم (Key Metrics)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "فروش ناخالص (AFN)",
                    value = "${formatter.format(2370000)} افغانی",
                    subtitle = "مجموع تراکنش‌ها در ۳۴ ولایت",
                    icon = Icons.Default.MonetizationOn,
                    cardColor = Color(0xFFE8F5E9),
                    accentColor = AlkoziDarkGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "دکان‌های فعال",
                    value = "${uiState.shops.size} غرفه رسمی",
                    subtitle = "با ۵ اپراتور مخابراتی",
                    icon = Icons.Default.Store,
                    cardColor = Color(0xFFFFF8E1),
                    accentColor = AlkoziGoldDark,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "کاربران ثبت‌نامی",
                    value = "${formatter.format(uiState.allUsers.size + 1835)} شهروند",
                    subtitle = "پوشش ۱۰۰٪ سیم‌کارت‌های افغان",
                    icon = Icons.Default.People,
                    cardColor = Color(0xFFE3F2FD),
                    accentColor = Color(0xFF0288D1),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "سفارشات پلتفرم",
                    value = "${uiState.allOrders.size + 42} سفارش",
                    subtitle = "تأییدشده و در حال تحویل",
                    icon = Icons.Default.ShoppingBag,
                    cardColor = Color(0xFFF3E5F5),
                    accentColor = Color(0xFF7B1FA2),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 2. CRITICAL SECTION: Global Subscription Master Switch
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = if (settings.isFreeSubscriptionEnabled) AlkoziDarkGreen else AlkoziGoldDark,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("admin_subscription_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (settings.isFreeSubscriptionEnabled) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (settings.isFreeSubscriptionEnabled) Icons.Default.LockOpen else Icons.Default.AttachMoney,
                                    contentDescription = "Subscription Switch",
                                    tint = if (settings.isFreeSubscriptionEnabled) AlkoziDarkGreen else AlkoziGoldDark
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "کلید سراسری اشتراک رایگان دکانداران",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF222222)
                                )
                                Text(
                                    text = "Master Subscription Toggle",
                                    fontSize = 11.sp,
                                    color = Color(0xFF777777)
                                )
                            }
                        }

                        // Big Toggle Switch
                        Switch(
                            checked = settings.isFreeSubscriptionEnabled,
                            onCheckedChange = { viewModel.toggleAdminSubscriptionSwitch(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AlkoziDarkGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFB0BEC5)
                            ),
                            modifier = Modifier.testTag("global_subscription_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Status Banner
                    Surface(
                        color = if (settings.isFreeSubscriptionEnabled) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (settings.isFreeSubscriptionEnabled) Icons.Default.CheckCircle else Icons.Default.Security,
                                contentDescription = "Status Icon",
                                tint = if (settings.isFreeSubscriptionEnabled) AlkoziDarkGreen else Color(0xFFE65100),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (settings.isFreeSubscriptionEnabled) {
                                    "وضعیت کنونی: اشتراک ۱۰۰٪ رایگان فعال است. تمامی هموطنان در ۳۴ ولایت می‌توانند بدون پرداخت هزینه، دکان آنلاین بسازند و کالای خود را بفروشند."
                                } else {
                                    "وضعیت کنونی: اشتراک پولی پلتفرم فعال است. دکانداران موظف به پرداخت حق عضویت ماهانه/سالانه هستند مگر دکان‌هایی که معافیت دستی دارند."
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (settings.isFreeSubscriptionEnabled) AlkoziDarkGreen else Color(0xFFB71C1C),
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Subscription Pricing Controls (AFN)
                    Text(
                        text = "تنظیم تعرفه اشتراک دکانداران (به افغانی - AFN)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = monthlyInput,
                            onValueChange = { monthlyInput = it.filter { c -> c.isDigit() } },
                            label = { Text("ماهانه (AFN)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AlkoziDarkGreen),
                            modifier = Modifier.weight(1f).testTag("monthly_fee_input")
                        )
                        OutlinedTextField(
                            value = quarterlyInput,
                            onValueChange = { quarterlyInput = it.filter { c -> c.isDigit() } },
                            label = { Text("۳ ماهه (AFN)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AlkoziDarkGreen),
                            modifier = Modifier.weight(1f).testTag("quarterly_fee_input")
                        )
                        OutlinedTextField(
                            value = yearlyInput,
                            onValueChange = { yearlyInput = it.filter { c -> c.isDigit() } },
                            label = { Text("سالانه (AFN)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AlkoziDarkGreen),
                            modifier = Modifier.weight(1f).testTag("yearly_fee_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val m = monthlyInput.toLongOrNull() ?: settings.monthlySubscriptionFeeAfn
                            val q = quarterlyInput.toLongOrNull() ?: settings.quarterlySubscriptionFeeAfn
                            val y = yearlyInput.toLongOrNull() ?: settings.yearlySubscriptionFeeAfn
                            viewModel.updateSubscriptionFees(m, q, y)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlkoziDarkGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_subscription_fees_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "ذخیره")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ذخیره تغییرات تعرفه اشتراک",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// TAB 2: SHOP MANAGEMENT & MANUAL EXEMPTION (مدیریت دکان‌ها و معافیت دستی)
// =========================================================================

@Composable
private fun ShopManagementTab(
    viewModel: MarketViewModel,
    uiState: MarketUiState,
    formatter: DecimalFormat
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "فهرست دکان‌های رسمی پلتفرم (${uiState.shops.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF222222)
                )
                Text(
                    text = "دارای دسترسی معافیت دستی",
                    fontSize = 11.sp,
                    color = AlkoziGoldDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(uiState.shops, key = { it.id }) { shop ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_shop_card_${shop.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Shop Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AlkoziLightGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = shop.name.take(1),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = AlkoziDarkGreen
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = shop.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E1E1E)
                                    )
                                    if (shop.isVerified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "تأییدشده",
                                            tint = Color(0xFF0288D1),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "ولایت: ${shop.province} | ${shop.businessPhone}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF777777)
                                )
                            }
                        }

                        // Active/Suspended Badge
                        Surface(
                            color = if (shop.isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (shop.isActive) "فعال" else "مسدود",
                                color = if (shop.isActive) AlkoziDarkGreen else Color(0xFFC62828),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = shop.description,
                        fontSize = 12.sp,
                        color = Color(0xFF555555),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "فروش کل: ${formatter.format(shop.totalSalesAfn)} AFN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "طرح اشتراک: ${shop.subscriptionPlan}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (shop.isExemptFromSubscription) AlkoziGoldDark else AlkoziDarkGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Controls: Exemption toggle & Verification toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // CRITICAL: Manual Exemption Switch
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.toggleShopExemption(shop.id) }
                        ) {
                            Switch(
                                checked = shop.isExemptFromSubscription,
                                onCheckedChange = { viewModel.toggleShopExemption(shop.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AlkoziGoldDark
                                ),
                                modifier = Modifier.size(36.dp).testTag("shop_exempt_switch_${shop.id}")
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (shop.isExemptFromSubscription) "معاف از حق اشتراک" else "تعرفه عادی اشتراک",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (shop.isExemptFromSubscription) AlkoziGoldDark else Color(0xFF666666)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Verification Toggle
                            OutlinedButton(
                                onClick = { viewModel.toggleShopVerification(shop.id) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp).testTag("verify_shop_btn_${shop.id}")
                            ) {
                                Text(
                                    text = if (shop.isVerified) "لغو نشان" else "تأیید مدارک",
                                    fontSize = 11.sp
                                )
                            }

                            // Active / Suspend Toggle
                            OutlinedButton(
                                onClick = { viewModel.toggleShopActiveStatus(shop.id) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (shop.isActive) Color(0xFFC62828) else AlkoziDarkGreen
                                ),
                                modifier = Modifier.height(34.dp).testTag("toggle_shop_active_btn_${shop.id}")
                            ) {
                                Text(
                                    text = if (shop.isActive) "مسدودسازی" else "فعال‌سازی",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// TAB 3: USER MANAGEMENT & RBAC (مدیریت کاربران و نقش‌ها)
// =========================================================================

@Composable
private fun UserManagementTab(
    viewModel: MarketViewModel,
    uiState: MarketUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "کاربران سیستم و کنترل سطوح دسترسی (RBAC)",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222)
            )
            Text(
                text = "تعریف نقش‌ها: خریدار، فروشنده، ناظر کالا، مدیر مالی، مدیر عمومی، سوپر ادمین",
                fontSize = 11.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
            )
        }

        items(uiState.allUsers, key = { it.phone }) { user ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_user_card_${user.phone}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(user.operator.accentColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.nameFa.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = user.operator.tagColor,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = user.nameFa,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF222222)
                                )
                                Text(
                                    text = "${user.phone} (${user.operator.name}) | ${user.province}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF777777)
                                )
                            }
                        }

                        // Role Badge
                        Surface(
                            color = Color(user.role.badgeColorHex),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = user.role.titleFa,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFF5F5F5))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Role promotion quick actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "تغییر نقش:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF555555)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            RoleQuickButton(
                                title = "فروشنده",
                                isCurrent = user.role == UserRole.SELLER,
                                onClick = { viewModel.updateUserRole(user.phone, UserRole.SELLER) }
                            )
                            RoleQuickButton(
                                title = "ناظر کالا",
                                isCurrent = user.role == UserRole.PRODUCT_MODERATOR,
                                onClick = { viewModel.updateUserRole(user.phone, UserRole.PRODUCT_MODERATOR) }
                            )
                            RoleQuickButton(
                                title = "مدیر مالی",
                                isCurrent = user.role == UserRole.FINANCE_MANAGER,
                                onClick = { viewModel.updateUserRole(user.phone, UserRole.FINANCE_MANAGER) }
                            )
                            RoleQuickButton(
                                title = "سوپر ادمین",
                                isCurrent = user.role == UserRole.SUPER_ADMIN,
                                onClick = { viewModel.updateUserRole(user.phone, UserRole.SUPER_ADMIN) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleQuickButton(
    title: String,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isCurrent) AlkoziDarkGreen else Color(0xFFF0F0F0),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isCurrent) Color.White else Color(0xFF444444),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

// =========================================================================
// TAB 4: ORDERS & PAYMENT GATEWAYS (سفارشات و درگاه‌ها)
// =========================================================================

@Composable
private fun OrdersAndGatewaysTab(
    viewModel: MarketViewModel,
    uiState: MarketUiState,
    formatter: DecimalFormat
) {
    val settings = uiState.platformSettings

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Payment Gateways Toggle Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("payment_gateways_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "مدیریت درگاه‌های پرداخت افغانستان",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    GatewayToggleRow(
                        name = "پرداخت نقدی هنگام تحویل (COD کابل اکسپرس)",
                        icon = Icons.Default.LocalShipping,
                        isEnabled = settings.codEnabled,
                        onToggle = { viewModel.togglePaymentGateway("cod", it) }
                    )
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 6.dp))

                    GatewayToggleRow(
                        name = "کیف پول M-Paisa روشن",
                        icon = Icons.Default.PhoneAndroid,
                        isEnabled = settings.mpaisaEnabled,
                        onToggle = { viewModel.togglePaymentGateway("mpaisa", it) }
                    )
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 6.dp))

                    GatewayToggleRow(
                        name = "کیف پول MyMoney اتصالات",
                        icon = Icons.Default.PhoneAndroid,
                        isEnabled = settings.mymoneyEnabled,
                        onToggle = { viewModel.togglePaymentGateway("mymoney", it) }
                    )
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(vertical = 6.dp))

                    GatewayToggleRow(
                        name = "حواله صرافی و بانکی (سرای شهزاده)",
                        icon = Icons.Default.AccountBalance,
                        isEnabled = settings.bankTransferEnabled,
                        onToggle = { viewModel.togglePaymentGateway("bank", it) }
                    )
                }
            }
        }

        // 2. Orders List
        item {
            Text(
                text = "سفارشات زنده پلتفرم (${uiState.allOrders.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )
        }

        items(uiState.allOrders, key = { it.id }) { order ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("admin_order_card_${order.id}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "شناسه: ${order.id}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AlkoziDarkGreen
                        )
                        Surface(
                            color = Color(order.orderStatus.colorHex),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = order.orderStatus.titleFa,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "مبلغ: ${formatter.format(order.totalAfn)} افغانی | ولایت مقصد: ${order.provinceFa}",
                        fontSize = 12.sp,
                        color = Color(0xFF333333),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "روش پرداخت: ${order.paymentMethodFa}",
                        fontSize = 11.sp,
                        color = Color(0xFF777777)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFF5F5F5))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lifecycle advance buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OrderStatus.entries.take(4).forEach { status ->
                            Surface(
                                color = if (order.orderStatus == status) Color(status.colorHex) else Color(0xFFF0F0F0),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { viewModel.updateOrderStatus(order.id, status) }
                            ) {
                                Text(
                                    text = status.titleFa,
                                    fontSize = 10.sp,
                                    fontWeight = if (order.orderStatus == status) FontWeight.Bold else FontWeight.Normal,
                                    color = if (order.orderStatus == status) Color.White else Color(0xFF444444),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GatewayToggleRow(
    name: String,
    icon: ImageVector,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = AlkoziDarkGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AlkoziDarkGreen
            ),
            modifier = Modifier.size(36.dp)
        )
    }
}

// =========================================================================
// TAB 5: AUDIT LOGS (گزارشات و لاگ‌های امنیتی)
// =========================================================================

@Composable
private fun AuditLogsTab(uiState: MarketUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "لاگ‌های امنیتی و عملیاتی پلتفرم (Audit Logs)",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF222222)
            )
            Text(
                text = "ثبت شفاف تمامی تغییرات اشتراک، معافیت‌ها، و اقدامات مدیران",
                fontSize = 11.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
            )
        }

        items(uiState.auditLogs, key = { it.id }) { log ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().testTag("audit_log_${log.id}")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AlkoziLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Log",
                            tint = AlkoziDarkGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = log.actionType,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlkoziDarkGreen
                            )
                            Text(
                                text = log.timestampFa,
                                fontSize = 10.sp,
                                color = Color(0xFF999999)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = log.details,
                            fontSize = 12.sp,
                            color = Color(0xFF444444),
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "اقدام‌کننده: ${log.adminName}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AlkoziGoldDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    cardColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

// =========================================================================
// TAB: IN-APP ADVERTISING & PROMOTION MANAGEMENT (مدیریت تبلیغات و پروموشن)
// =========================================================================

@Composable
fun AdsManagementTab(
    viewModel: MarketViewModel,
    uiState: MarketUiState,
    formatter: DecimalFormat
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("https://images.unsplash.com/photo-1544717305-2782549b5136?w=800") }
    var selectedPlacement by remember { mutableStateOf(com.example.model.AdPlacementArea.HOME_PAGE) }
    var selectedTargetType by remember { mutableStateOf(com.example.model.AdTargetType.COMPANY) }
    var phoneInput by remember { mutableStateOf("+93 700 123 456") }
    var whatsappInput by remember { mutableStateOf("https://wa.me/93700123456") }
    var websiteInput by remember { mutableStateOf("https://afghan-wireless.com") }
    var feeInput by remember { mutableStateOf("15000") }

    val totalRevenueAfn = uiState.inAppAdvertisements.sumOf { it.feeChargedAfn }
    val activeAdsCount = uiState.inAppAdvertisements.count { it.isActive }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text(
                    text = "ثبت آگهی و بنر تبلیغاتی جدید",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlkoziDeepGreen
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("عنوان تبلیغ") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descInput,
                        onValueChange = { descInput = it },
                        label = { Text("شرح و آفر تخفیف") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = imageUrlInput,
                        onValueChange = { imageUrlInput = it },
                        label = { Text("آدرس تصویر بنر (URL)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = feeInput,
                        onValueChange = { feeInput = it },
                        label = { Text("مبلغ تعرفه دریافتی (AFN)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("شماره تماس تبلیغ‌دهنده") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = whatsappInput,
                        onValueChange = { whatsappInput = it },
                        label = { Text("لینک گفتگوی مستقیم واتساپ") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            viewModel.createAdvertisement(
                                title = titleInput,
                                description = descInput,
                                bannerImageUrl = imageUrlInput,
                                placement = selectedPlacement,
                                targetType = selectedTargetType,
                                advertiserPhone = phoneInput,
                                whatsappLink = whatsappInput,
                                websiteUrl = websiteInput,
                                feeChargedAfn = feeInput.toLongOrNull() ?: 10000L
                            )
                            titleInput = ""
                            descInput = ""
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlkoziDeepGreen)
                ) {
                    Text("انتشار در اپلیکیشن")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Stats Overview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "کل تبلیغات پلتفرم",
                    value = "${uiState.inAppAdvertisements.size} بنر",
                    subtitle = "$activeAdsCount تبلیغ هم‌اکنون فعال",
                    icon = Icons.Default.Campaign,
                    cardColor = Color(0xFFE8F5E9),
                    accentColor = AlkoziDarkGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "درآمد حاصل از تبلیغات",
                    value = "${formatter.format(totalRevenueAfn)} AFN",
                    subtitle = "واریزی مستقیم به پلتفرم",
                    icon = Icons.Default.MonetizationOn,
                    cardColor = Color(0xFFFFF8E1),
                    accentColor = AlkoziGoldDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Add New Ad Button
        item {
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = AlkoziDeepGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_create_ad_btn")
            ) {
                Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = AlkoziGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ثبت تبلیغ و اسپانسر جدید در پلتفرم",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // List of Advertisements
        items(uiState.inAppAdvertisements, key = { it.id }) { ad ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color(ad.placement.badgeColor).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = ad.placement.titleFa,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(ad.placement.badgeColor),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = ad.targetType.titleFa,
                                    fontSize = 11.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = ad.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222)
                            )
                            if (ad.description.isNotBlank()) {
                                Text(
                                    text = ad.description,
                                    fontSize = 12.sp,
                                    color = Color(0xFF555555),
                                    maxLines = 2
                                )
                            }
                        }

                        // Active Toggle Switch
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Switch(
                                checked = ad.isActive,
                                onCheckedChange = { viewModel.toggleAdvertisementStatus(ad.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AlkoziDarkGreen
                                )
                            )
                            Text(
                                text = if (ad.isActive) "فعال در کلاینت" else "غیرفعال",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ad.isActive) Color(0xFF2E7D32) else Color(0xFF888888)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "تعرفه: ${formatter.format(ad.feeChargedAfn)} AFN",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlkoziDarkGreen
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${ad.impressionsCount} بازدید • ${ad.clicksCount} کلیک",
                                fontSize = 11.sp,
                                color = Color(0xFF777777)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.deleteAdvertisement(ad.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = "حذف تبلیغ",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

