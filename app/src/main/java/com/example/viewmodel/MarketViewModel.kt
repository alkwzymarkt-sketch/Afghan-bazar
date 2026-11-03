package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppLanguage
import com.example.model.AppMode
import com.example.model.AppNotification
import com.example.model.AuditLog
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.ChatMessage
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.PlatformSettings
import com.example.model.Product
import com.example.model.ProductSortOption
import com.example.model.SampleEcosystemData
import com.example.model.SampleMarketData
import com.example.model.Shop
import com.example.model.TelecomOperator
import com.example.model.UserProfile
import com.example.model.UserRole
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AuthStep {
    PHONE_INPUT,
    OTP_VERIFICATION,
    LOGGED_IN
}

data class MarketUiState(
    // App Ecosystem
    val currentAppMode: AppMode = AppMode.MARKET,
    val appLanguage: AppLanguage = AppLanguage.FA,
    val platformSettings: PlatformSettings = PlatformSettings(),
    val shops: List<Shop> = SampleEcosystemData.sampleShops,
    val allUsers: List<UserProfile> = SampleMarketData.sampleUsers,
    val allOrders: List<Order> = SampleMarketData.sampleOrders,
    val auditLogs: List<AuditLog> = SampleEcosystemData.sampleAuditLogs,

    val categories: List<Category> = SampleMarketData.categories,
    val selectedCategoryId: String = "all",
    val searchQuery: String = "",
    val products: List<Product> = SampleMarketData.products,
    val cartItems: List<CartItem> = emptyList(),
    val selectedProductForDetail: Product? = null,
    val showCartSheet: Boolean = false,
    val showAuthDialog: Boolean = false,
    val showCheckoutDialog: Boolean = false,
    val showProfileSheet: Boolean = false,
    val offersRemainingSeconds: Long = 14 * 3600 + 42 * 60 + 19, // ~14h 42m countdown

    // Auth state
    val authStep: AuthStep = AuthStep.PHONE_INPUT,
    val phoneInput: String = "",
    val nameInput: String = "",
    val detectedOperator: TelecomOperator? = null,
    val isPhoneValid: Boolean = false,
    val phoneErrorMessage: String? = null,
    val otpCode: String = "",
    val otpRemainingSeconds: Int = 120,
    val isVoiceCallRequested: Boolean = false,
    val userProfile: UserProfile? = null,
    val authSuccessToast: String? = null,

    // Checkout & Geography
    val selectedProvince: String = SampleMarketData.afghanProvinces.first(),
    val selectedProvinceFilter: String = "همه ۳۴ ولایت",
    val customerAddress: String = "",
    val paymentMethod: String = "پرداخت نقدی هنگام تحویل (کابل اکسپرس)",
    val lastCompletedOrder: Order? = null,

    // Seller marketplace state
    val showSellerRegistrationDialog: Boolean = false,
    val showSellerDashboardSheet: Boolean = false,
    val showAddProductDialog: Boolean = false,
    val showSubscriptionPaywallDialog: Boolean = false,
    val userCreatedProductIds: Set<String> = emptySet(),

    // In-app Chat with Vendors
    val activeChatShop: Shop? = null,
    val chatMessages: List<ChatMessage> = SampleEcosystemData.sampleChatMessages,

    // Real-time Notifications Center
    val notifications: List<AppNotification> = SampleEcosystemData.sampleNotifications,
    val showNotificationsSheet: Boolean = false,

    // Advanced Filtering and Sorting (34 Afghan Provinces + Multi-factor Sort)
    val showFilterSortDialog: Boolean = false,
    val productSortOption: ProductSortOption = ProductSortOption.RECOMMENDED,
    val onlyInStockFilter: Boolean = false,
    val onlySpecialOffersFilter: Boolean = false,

    // Advertising & Promotion System
    val promotionalStories: List<com.example.model.PromotionalStory> = com.example.model.SampleAdsData.stories,
    val activeStory: com.example.model.PromotionalStory? = null,
    val selectedCityAdFilter: String = "همه ولایات",
    val showPromoPopup: Boolean = false,
    val appliedVoucherCode: String? = null,
    val voucherDiscountAfn: Long = 0L,
    val isTopHeaderBannerVisible: Boolean = true,

    // Real Online Server Connectivity State
    val serverStatus: com.example.network.ServerConnectionStatus = com.example.network.ServerConnectionStatus(),

    // In-App Advertising Management System State
    val inAppAdvertisements: List<com.example.model.InAppAdvertisement> = com.example.model.SampleInAppAdsData.initialAds
) {
    val totalCartItemsCount: Int
        get() = cartItems.sumOf { it.quantity }

    val unreadNotificationsCount: Int
        get() = notifications.count { !it.isRead }

    val cartSubtotalAfn: Long
        get() = cartItems.sumOf { it.product.priceAfn * it.quantity }

    val cartTotalDiscountAfn: Long
        get() = cartItems.sumOf {
            (it.product.originalPriceAfn - it.product.priceAfn).coerceAtLeast(0L) * it.quantity
        }

    val shippingFeeAfn: Long
        get() = if (cartSubtotalAfn >= 3000L || cartItems.isEmpty()) 0L else 150L

    val cartFinalPayableAfn: Long
        get() = (cartSubtotalAfn + shippingFeeAfn - voucherDiscountAfn).coerceAtLeast(0L)

    // Filter products by Category, Search Query, 34 Provinces Filter, Stock and Sort
    val filteredProducts: List<Product>
        get() {
            val list = products.filter { product ->
                val matchesCategory = selectedCategoryId == "all" || product.categoryId == selectedCategoryId
                val matchesSearch = searchQuery.isBlank() ||
                    product.titleFa.contains(searchQuery, ignoreCase = true) ||
                    product.vendorNameFa.contains(searchQuery, ignoreCase = true) ||
                    product.descriptionFa.contains(searchQuery, ignoreCase = true)
                val matchesProvince = selectedProvinceFilter == "همه ۳۴ ولایت" ||
                    product.vendorLocationFa.contains(selectedProvinceFilter) ||
                    product.titleFa.contains(selectedProvinceFilter)
                val matchesStock = !onlyInStockFilter || product.stock > 0
                val matchesOffer = !onlySpecialOffersFilter || product.isSpecialOffer
                matchesCategory && matchesSearch && matchesProvince && matchesStock && matchesOffer
            }

            return when (productSortOption) {
                ProductSortOption.RECOMMENDED -> list.sortedByDescending { it.isSponsored }
                ProductSortOption.NEWEST -> list.sortedByDescending { it.id }
                ProductSortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.priceAfn }
                ProductSortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.priceAfn }
                ProductSortOption.BEST_RATING -> list.sortedByDescending { it.rating }
                ProductSortOption.DISCOUNT -> list.sortedByDescending { it.discountPercent }
            }
        }

    val specialOfferProducts: List<Product>
        get() = products.filter { it.isSpecialOffer }

}

class MarketViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    private val serverClient = com.example.network.AlkoziServerClient()

    private var otpTimerJob: Job? = null
    private var offersTimerJob: Job? = null
    private var serverPingJob: Job? = null

    init {
        startOffersCountdown()
        startOnlineServerMonitoring()
        // Registration is required on first launch: userProfile is null and authStep is PHONE_INPUT
    }

    private fun startOnlineServerMonitoring() {
        serverPingJob?.cancel()
        serverPingJob = viewModelScope.launch {
            while (isActive) {
                pingOnlineServer()
                delay(30_000L) // Refresh server health every 30 seconds
            }
        }
    }

    fun pingOnlineServer() {
        viewModelScope.launch {
            val (isConnected, latency) = serverClient.pingServer()
            _uiState.update { current ->
                current.copy(
                    serverStatus = current.serverStatus.copy(
                        isConnected = isConnected,
                        pingLatencyMs = latency,
                        lastSyncTimeFa = "هم‌اکنون",
                        isSyncing = false
                    )
                )
            }
        }
    }

    fun syncWithOnlineServerNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(serverStatus = it.serverStatus.copy(isSyncing = true)) }
            val (isConnected, latency) = serverClient.pingServer()
            val settings = _uiState.value.platformSettings
            serverClient.pushSubscriptionSettings(
                isFree = settings.isFreeSubscriptionEnabled,
                monthlyFeeAfn = settings.monthlySubscriptionFeeAfn
            )
            delay(600L) // UX smooth feedback
            _uiState.update { current ->
                val newLog = AuditLog(
                    id = "LOG-SYNC-${System.currentTimeMillis() % 10000}",
                    adminName = current.userProfile?.nameFa ?: "احمد شاه الکوزی (Super Admin)",
                    actionType = "همگام‌سازی ابری با سرور آنلاین",
                    details = "همگام‌سازی موفقیت‌آمیز پایگاه داده سرور مرکزی در ۳۴ ولایت (${latency}ms)",
                    timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
                )
                current.copy(
                    serverStatus = current.serverStatus.copy(
                        isConnected = isConnected,
                        pingLatencyMs = latency,
                        lastSyncTimeFa = "هم‌اکنون",
                        isSyncing = false
                    ),
                    auditLogs = listOf(newLog) + current.auditLogs,
                    authSuccessToast = "همگام‌سازی کامل با سرور مرکزی Alkozi با پینگ ${latency} میلی‌ثانیه انجام شد."
                )
            }
        }
    }

    fun updateServerAddress(newUrl: String) {
        serverClient.updateServerUrl(newUrl)
        _uiState.update { current ->
            current.copy(
                serverStatus = current.serverStatus.copy(serverUrl = newUrl),
                authSuccessToast = "آدرس سرور آنلاین به $newUrl بروزرسانی شد."
            )
        }
        pingOnlineServer()
    }

    private fun startOffersCountdown() {
        offersTimerJob?.cancel()
        offersTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _uiState.update { current ->
                    val next = if (current.offersRemainingSeconds > 0) current.offersRemainingSeconds - 1 else 24 * 3600
                    current.copy(offersRemainingSeconds = next)
                }
            }
        }
    }

    fun onCategorySelect(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openProductDetail(product: Product) {
        _uiState.update { it.copy(selectedProductForDetail = product) }
    }

    fun closeProductDetail() {
        _uiState.update { it.copy(selectedProductForDetail = null) }
    }

    fun toggleCartSheet(show: Boolean) {
        _uiState.update { it.copy(showCartSheet = show) }
    }

    fun toggleProfileSheet(show: Boolean) {
        _uiState.update { it.copy(showProfileSheet = show) }
    }

    fun toggleAuthDialog(show: Boolean) {
        _uiState.update { current ->
            if (show) {
                // If user already logged in, show options or start fresh
                current.copy(showAuthDialog = true)
            } else {
                current.copy(showAuthDialog = false)
            }
        }
    }

    fun addToCart(product: Product) {
        _uiState.update { current ->
            val existingIndex = current.cartItems.indexOfFirst { it.product.id == product.id }
            val newItems = current.cartItems.toMutableList()
            if (existingIndex >= 0) {
                val item = newItems[existingIndex]
                newItems[existingIndex] = item.copy(quantity = item.quantity + 1)
            } else {
                newItems.add(CartItem(product = product, quantity = 1))
            }
            current.copy(cartItems = newItems)
        }
    }

    fun updateCartItemQuantity(productId: String, delta: Int) {
        _uiState.update { current ->
            val newItems = current.cartItems.mapNotNull { item ->
                if (item.product.id == productId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else {
                    item
                }
            }
            current.copy(cartItems = newItems)
        }
    }

    fun removeFromCart(productId: String) {
        _uiState.update { current ->
            current.copy(cartItems = current.cartItems.filterNot { it.product.id == productId })
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    // --- Afghan Phone & Operator Detection Logic ---

    fun onPhoneInputChange(input: String) {
        // Keep digits and common separators
        val detected = TelecomOperator.detect(input)
        val isValid = TelecomOperator.isValidAfghanNumber(input)
        _uiState.update {
            it.copy(
                phoneInput = input,
                detectedOperator = detected,
                isPhoneValid = isValid,
                phoneErrorMessage = if (input.isNotBlank() && !isValid && input.filter { c -> c.isDigit() }.length >= 9) {
                    "شماره وارد شده معتبر نمی‌باشد. لطفاً شماره ۹ رقمی افغانستان (با پیش‌شماره 07) را وارد کنید."
                } else null
            )
        }
    }

    /**
     * Fast test helper to prefill sample valid phone numbers for all 5 Afghan operators
     */
    fun quickFillOperatorSample(operator: TelecomOperator) {
        val sampleNumber = when (operator) {
            TelecomOperator.AWCC -> "0700123456"
            TelecomOperator.ROSHAN -> "0799887766"
            TelecomOperator.ETISALAT -> "0788334455"
            TelecomOperator.MTN -> "0777112233"
            TelecomOperator.SALAAM -> "0744998877"
        }
        onPhoneInputChange(sampleNumber)
    }

    fun proceedToOtp() {
        val state = _uiState.value
        if (!state.isPhoneValid) {
            _uiState.update {
                it.copy(phoneErrorMessage = "لطفاً ابتدا یک شماره معتبر ۹ رقمی مربوط به یکی از اپراتورهای افغانستان وارد نمایید.")
            }
            return
        }

        _uiState.update {
            it.copy(
                authStep = AuthStep.OTP_VERIFICATION,
                otpCode = "",
                otpRemainingSeconds = 120,
                isVoiceCallRequested = false,
                phoneErrorMessage = null
            )
        }
        startOtpTimer()
    }

    private fun startOtpTimer() {
        otpTimerJob?.cancel()
        otpTimerJob = viewModelScope.launch {
            while (_uiState.value.otpRemainingSeconds > 0) {
                delay(1000L)
                _uiState.update { it.copy(otpRemainingSeconds = it.otpRemainingSeconds - 1) }
            }
        }
    }

    fun resendOtpSms() {
        if (_uiState.value.otpRemainingSeconds <= 0) {
            _uiState.update {
                it.copy(
                    otpRemainingSeconds = 120,
                    isVoiceCallRequested = false,
                    authSuccessToast = "کد تایید مجدداً از طریق پیامک ارسال شد."
                )
            }
            startOtpTimer()
        }
    }

    fun requestVoiceCallOtp() {
        _uiState.update {
            it.copy(
                isVoiceCallRequested = true,
                otpRemainingSeconds = 120,
                authSuccessToast = "تماس صوتی خودکار با شماره شما برقرار می‌شود. لطفاً پاسخ دهید..."
            )
        }
        startOtpTimer()
    }

    fun onOtpCodeChange(newCode: String) {
        if (newCode.length <= 5 && newCode.all { it.isDigit() }) {
            _uiState.update { it.copy(otpCode = newCode) }
            if (newCode.length == 5) {
                verifyOtpCode(newCode)
            }
        }
    }

    fun autoFillTestOtp() {
        onOtpCodeChange("12345")
    }

    fun onNameInputChange(name: String) {
        _uiState.update { it.copy(nameInput = name) }
    }

    fun verifyOtpCode(code: String = _uiState.value.otpCode) {
        // Standard test code is 12345 or any 5 digit code entered by user
        if (code.length == 5) {
            val current = _uiState.value
            val standardPhone = TelecomOperator.formatToStandard(current.phoneInput)
            val op = current.detectedOperator ?: TelecomOperator.AWCC
            val displayName = current.nameInput.trim().ifBlank { "کاربر محترم الکوزی" }
            loginUserWithOperator(standardPhone, op, displayName)
            _uiState.update {
                it.copy(
                    showAuthDialog = false,
                    authStep = AuthStep.LOGGED_IN,
                    authSuccessToast = "ثبت‌نام با موفقیت انجام شد! به الکوزی مارکت خوش آمدید."
                )
            }
        }
    }

    fun backToPhoneInput() {
        otpTimerJob?.cancel()
        _uiState.update { it.copy(authStep = AuthStep.PHONE_INPUT) }
    }

    fun loginUserWithOperator(phone: String, operator: TelecomOperator, userName: String) {
        val profile = UserProfile(
            phone = phone,
            operator = operator,
            nameFa = userName,
            balanceAfn = 28500L,
            cityFa = "کابل، افغانستان"
        )
        _uiState.update {
            it.copy(
                userProfile = profile,
                authStep = AuthStep.LOGGED_IN,
                detectedOperator = operator,
                phoneInput = phone
            )
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                userProfile = null,
                authStep = AuthStep.PHONE_INPUT,
                phoneInput = "",
                nameInput = "",
                detectedOperator = null,
                isPhoneValid = false,
                otpCode = "",
                showProfileSheet = false,
                authSuccessToast = "شما از حساب خارج شدید. برای دسترسی به بازار مجدداً ثبت‌نام کنید."
            )
        }
    }

    fun clearToast() {
        _uiState.update { it.copy(authSuccessToast = null) }
    }

    // --- Checkout Flow ---

    fun openCheckout() {
        _uiState.update {
            it.copy(
                showCartSheet = false,
                showCheckoutDialog = true
            )
        }
    }

    fun closeCheckout() {
        _uiState.update { it.copy(showCheckoutDialog = false) }
    }

    fun onProvinceSelected(province: String) {
        _uiState.update { it.copy(selectedProvince = province) }
    }

    fun onAddressDetailChanged(address: String) {
        _uiState.update { it.copy(customerAddress = address) }
    }

    fun onPaymentMethodChanged(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun completeOrder() {
        val state = _uiState.value
        val newOrder = Order(
            id = "ALK-${(100000..999999).random()}",
            dateFa = "۱۴۰۵/۰۲/۱۵",
            items = state.cartItems,
            totalAfn = state.cartFinalPayableAfn,
            provinceFa = state.selectedProvince,
            addressDetailFa = state.customerAddress.ifBlank { "آدرس پیش‌فرض تحویل کابل" },
            statusFa = "در حال پردازش و بسته‌بندی در انبار مرکزی کابل",
            paymentMethodFa = state.paymentMethod
        )
        _uiState.update {
            it.copy(
                lastCompletedOrder = newOrder,
                cartItems = emptyList(),
                showCheckoutDialog = false,
                authSuccessToast = "سفارش شما با شناسه ${newOrder.id} ثبت گردید و به زودی ارسال می‌شود!"
            )
        }
    }

    fun dismissCompletedOrder() {
        _uiState.update { it.copy(lastCompletedOrder = null) }
    }

    // --- Marketplace Seller Management ---

    fun toggleSellerRegistrationDialog(show: Boolean) {
        _uiState.update { it.copy(showSellerRegistrationDialog = show) }
    }

    fun toggleSellerDashboardSheet(show: Boolean) {
        _uiState.update { it.copy(showSellerDashboardSheet = show) }
    }

    fun toggleAddProductDialog(show: Boolean) {
        _uiState.update { it.copy(showAddProductDialog = show) }
    }

    fun registerSellerBooth(
        boothName: String,
        province: String,
        category: String,
        description: String
    ) {
        val currentProfile = _uiState.value.userProfile ?: return
        val isFree = _uiState.value.platformSettings.isFreeSubscriptionEnabled

        val newBooth = com.example.model.SellerBooth(
            boothId = "BOOTH-${(1000..9999).random()}",
            boothNameFa = boothName.ifBlank { "غرفه ${currentProfile.nameFa}" },
            provinceFa = province,
            categoryFa = category,
            descriptionFa = description.ifBlank { "عرضه کالاهای باکیفیت و اصیل با گارانتی معتبر در الکوزی مارکت" },
            contactPhone = currentProfile.phone,
            totalRevenueAfn = 0L,
            totalSalesCount = 0
        )

        // Also register into platform shops list
        val newShop = Shop(
            id = "shop_${System.currentTimeMillis()}",
            ownerId = currentProfile.phone,
            name = newBooth.boothNameFa,
            businessPhone = currentProfile.phone,
            logoUrl = "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=800",
            description = newBooth.descriptionFa,
            address = "$province، بازار مرکزی",
            province = province,
            city = province,
            isVerified = true,
            isActive = true,
            isExemptFromSubscription = isFree,
            subscriptionPlan = if (isFree) "اشتراک رایگان پلتفرم" else "اشتراک استاندارد",
            subscriptionExpiresAtFa = "۱۴۰۵/۱۲/۲۹",
            rating = 5.0f,
            totalSalesAfn = 0L
        )

        val updatedProfile = currentProfile.copy(
            role = UserRole.SELLER,
            sellerBooth = newBooth
        )

        _uiState.update { current ->
            current.copy(
                userProfile = updatedProfile,
                shops = listOf(newShop) + current.shops,
                showSellerRegistrationDialog = false,
                showSellerDashboardSheet = true,
                authSuccessToast = if (isFree) {
                    "تبریک! غرفه «${newBooth.boothNameFa}» با اشتراک ۱۰۰٪ رایگان افتتاح شد."
                } else {
                    "غرفه «${newBooth.boothNameFa}» ایجاد شد. برای فروش سراسری فعال گردید."
                }
            )
        }
    }

    fun toggleSubscriptionPaywall(show: Boolean) {
        _uiState.update { it.copy(showSubscriptionPaywallDialog = show) }
    }

    // --- Dual-App Ecosystem & Alkozi Admin Operations ---

    fun switchAppMode(mode: AppMode) {
        _uiState.update {
            it.copy(
                currentAppMode = mode,
                authSuccessToast = if (mode == AppMode.ADMIN) {
                    "به پنل اختصاصی مدیریت ارشد Alkozi Admin خوش آمدید."
                } else {
                    "به بازار آنلاین سراسری Alkozi Market منتقل شدید."
                }
            )
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _uiState.update {
            it.copy(
                appLanguage = lang,
                authSuccessToast = "زبان سیستم به ${lang.displayName} تغییر یافت."
            )
        }
    }

    fun setProvinceFilter(province: String) {
        _uiState.update { it.copy(selectedProvinceFilter = province) }
    }

    /**
     * CRITICAL MASTER SWITCH: Global Free/Paid Seller Subscription
     */
    fun toggleAdminSubscriptionSwitch(isFree: Boolean) {
        _uiState.update { current ->
            val updatedSettings = current.platformSettings.copy(isFreeSubscriptionEnabled = isFree)
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "احمد شاه الکوزی (Super Admin)",
                actionType = "تغییر کلید اشتراک پلتفرم",
                details = if (isFree) {
                    "فعال‌سازی سوییچ اشتراک رایگان دکانداران سراسر کشور"
                } else {
                    "غیرفعال‌سازی اشتراک رایگان و الزام پرداخت حق عضویت (${updatedSettings.monthlySubscriptionFeeAfn} AFN)"
                },
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                platformSettings = updatedSettings,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = if (isFree) {
                    "اشتراک رایگان دکانداران در سراسر ۳۴ ولایت فعال شد!"
                } else {
                    "اشتراک پولی فعال شد. دکانداران موظف به پرداخت حق اشتراک ماهانه هستند."
                }
            )
        }
    }

    fun updateSubscriptionFees(monthly: Long, quarterly: Long, yearly: Long) {
        _uiState.update { current ->
            val updatedSettings = current.platformSettings.copy(
                monthlySubscriptionFeeAfn = monthly,
                quarterlySubscriptionFeeAfn = quarterly,
                yearlySubscriptionFeeAfn = yearly
            )
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "احمد شاه الکوزی (Super Admin)",
                actionType = "بروزرسانی تعرفه‌های اشتراک",
                details = "تعرفه ماهانه: $monthly AFN | سه ماهه: $quarterly AFN | سالانه: $yearly AFN",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                platformSettings = updatedSettings,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "تعرفه‌های اشتراک دکانداران به افغانی (AFN) بروزرسانی شد."
            )
        }
    }

    fun togglePaymentGateway(gateway: String, isEnabled: Boolean) {
        _uiState.update { current ->
            val s = current.platformSettings
            val updated = when (gateway.lowercase()) {
                "cod" -> s.copy(codEnabled = isEnabled)
                "mpaisa" -> s.copy(mpaisaEnabled = isEnabled)
                "mymoney" -> s.copy(mymoneyEnabled = isEnabled)
                "bank" -> s.copy(bankTransferEnabled = isEnabled)
                else -> s
            }
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "مدیر سیستم",
                actionType = "تنظیم درگاه پرداخت",
                details = "وضعیت درگاه $gateway به ${if (isEnabled) "فعال" else "غیرفعال"} تغییر یافت.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                platformSettings = updated,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "درگاه $gateway بروزرسانی شد."
            )
        }
    }

    // --- In-App Advertising Management System (Admin CRUD & Toggles) ---

    fun toggleAdvertisementStatus(adId: String) {
        _uiState.update { current ->
            val updated = current.inAppAdvertisements.map { ad ->
                if (ad.id == adId) ad.copy(isActive = !ad.isActive) else ad
            }
            val target = updated.firstOrNull { it.id == adId }
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "احمد شاه الکوزی (Super Admin)",
                actionType = "تغییر وضعیت تبلیغ درون‌برنامه‌ای",
                details = "تبلیغ «${target?.title}» در جایگاه ${target?.placement?.titleFa} به وضعیت ${if (target?.isActive == true) "فعال" else "غیرفعال"} تغییر یافت.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                inAppAdvertisements = updated,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = if (target?.isActive == true) "تبلیغ فعال و در اپلیکیشن منتشر شد." else "تبلیغ غیرفعال شد."
            )
        }
    }

    fun deleteAdvertisement(adId: String) {
        _uiState.update { current ->
            val target = current.inAppAdvertisements.firstOrNull { it.id == adId }
            val updated = current.inAppAdvertisements.filter { it.id != adId }
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "احمد شاه الکوزی (Super Admin)",
                actionType = "حذف تبلیغ تجاری",
                details = "تبلیغ «${target?.title}» از سیستم تبلیغات حذف گردید.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                inAppAdvertisements = updated,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "تبلیغ با موفقیت حذف گردید."
            )
        }
    }

    fun createAdvertisement(
        title: String,
        description: String,
        bannerImageUrl: String,
        placement: com.example.model.AdPlacementArea,
        targetType: com.example.model.AdTargetType,
        advertiserPhone: String,
        whatsappLink: String,
        websiteUrl: String,
        feeChargedAfn: Long
    ) {
        val newAd = com.example.model.InAppAdvertisement(
            id = "ad-${System.currentTimeMillis() % 10000}",
            title = title,
            description = description,
            bannerImageUrl = bannerImageUrl,
            placement = placement,
            targetType = targetType,
            advertiserPhone = advertiserPhone,
            whatsappLink = whatsappLink,
            websiteUrl = websiteUrl,
            feeChargedAfn = feeChargedAfn,
            startDate = "۱۴۰۵/۰۲/۱۵",
            endDate = "۱۴۰۵/۰۳/۱۵",
            isActive = true
        )
        _uiState.update { current ->
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "Super Admin",
                actionType = "ثبت تبلیغ جدید",
                details = "تبلیغ «$title» در جایگاه ${placement.titleFa} با مبلغ $feeChargedAfn افغانی ثبت شد.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                inAppAdvertisements = listOf(newAd) + current.inAppAdvertisements,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "تبلیغ جدید با موفقیت ایجاد و در پلتفرم منتشر شد."
            )
        }
    }

    fun toggleShopVerification(shopId: String) {
        _uiState.update { current ->
            val updated = current.shops.map { shop ->
                if (shop.id == shopId) shop.copy(isVerified = !shop.isVerified) else shop
            }
            val target = updated.firstOrNull { it.id == shopId }
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "ناظر پلتفرم",
                actionType = "تغییر نشان تأیید دکان",
                details = "وضعیت اعتبارسنجی «${target?.name}» به ${if (target?.isVerified == true) "تأییدشده" else "در انتظار"} تغییر یافت.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                shops = updated,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "نشان اصالت دکان با موفقیت تغییر کرد."
            )
        }
    }

    /**
     * Admin manual exemption of specific shops from paying subscription fees
     */
    fun toggleShopExemption(shopId: String) {
        _uiState.update { current ->
            val updated = current.shops.map { shop ->
                if (shop.id == shopId) {
                    val newExempt = !shop.isExemptFromSubscription
                    shop.copy(
                        isExemptFromSubscription = newExempt,
                        subscriptionPlan = if (newExempt) "معافیت ویژه مدیریت" else "اشتراک استاندارد"
                    )
                } else shop
            }
            val target = updated.firstOrNull { it.id == shopId }
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "Super Admin",
                actionType = "تغییر معافیت حق اشتراک",
                details = "وضعیت معافیت دکان «${target?.name}» به ${if (target?.isExemptFromSubscription == true) "معاف از پرداخت" else "عادی"} تغییر یافت.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                shops = updated,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = if (target?.isExemptFromSubscription == true) {
                    "دکان «${target.name}» از پرداخت هرگونه حق اشتراک معاف شد."
                } else {
                    "معافیت دکان «${target?.name}» لغو شد."
                }
            )
        }
    }

    fun toggleShopActiveStatus(shopId: String) {
        _uiState.update { current ->
            val updated = current.shops.map { shop ->
                if (shop.id == shopId) shop.copy(isActive = !shop.isActive) else shop
            }
            val target = updated.firstOrNull { it.id == shopId }
            current.copy(
                shops = updated,
                authSuccessToast = "وضعیت فعالیت دکان به ${if (target?.isActive == true) "فعال" else "مسدود موقت"} تغییر یافت."
            )
        }
    }

    fun toggleUserSuspension(phone: String) {
        _uiState.update { current ->
            val updated = current.allUsers.map { user ->
                if (user.phone == phone) user.copy(isSuspended = !user.isSuspended) else user
            }
            current.copy(
                allUsers = updated,
                authSuccessToast = "وضعیت دسترسی کاربر بروزرسانی شد."
            )
        }
    }

    fun updateUserRole(phone: String, newRole: UserRole) {
        _uiState.update { current ->
            val updated = current.allUsers.map { user ->
                if (user.phone == phone) user.copy(role = newRole) else user
            }
            val updatedSelf = if (current.userProfile?.phone == phone) {
                current.userProfile.copy(role = newRole)
            } else current.userProfile
            val newLog = AuditLog(
                id = "LOG-${System.currentTimeMillis() % 10000}",
                adminName = current.userProfile?.nameFa ?: "Super Admin",
                actionType = "ارتقای سطح دسترسی (RBAC)",
                details = "نقش کاربر با شماره $phone به «${newRole.titleFa}» ارتقا داده شد.",
                timestampFa = "۱۴۰۵/۰۲/۱۵ - اکنون"
            )
            current.copy(
                allUsers = updated,
                userProfile = updatedSelf,
                auditLogs = listOf(newLog) + current.auditLogs,
                authSuccessToast = "نقش کاربری به «${newRole.titleFa}» تغییر یافت."
            )
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _uiState.update { current ->
            val updated = current.allOrders.map { order ->
                if (order.id == orderId) order.copy(orderStatus = newStatus, statusFa = newStatus.titleFa) else order
            }
            current.copy(
                allOrders = updated,
                authSuccessToast = "وضعیت سفارش $orderId به «${newStatus.titleFa}» تغییر یافت."
            )
        }
    }

    // In-app Real-time Chat
    fun openChatWithShop(shop: Shop) {
        _uiState.update { it.copy(activeChatShop = shop) }
    }

    fun openChatForProduct(product: Product) {
        val shop = _uiState.value.shops.firstOrNull { it.name == product.vendorNameFa }
            ?: Shop(
                id = "shop_${product.id}",
                ownerId = "seller_${product.id}",
                name = product.vendorNameFa,
                businessPhone = "+93 700 000 000",
                logoUrl = product.imageUrl,
                bannerUrl = product.imageUrl,
                description = "فروشنده رسمی در ${product.vendorLocationFa}",
                address = product.vendorLocationFa,
                province = "کابل",
                city = "کابل",
                isVerified = true,
                isActive = true,
                rating = product.rating,
                totalSalesAfn = 50000L
            )
        _uiState.update { it.copy(activeChatShop = shop, selectedProductForDetail = null) }
    }

    fun closeChat() {
        _uiState.update { it.copy(activeChatShop = null) }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentProfile = _uiState.value.userProfile
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = currentProfile?.phone ?: "me",
            senderName = currentProfile?.nameFa ?: "خریدار محترم",
            messageText = text.trim(),
            timestampFa = "اکنون",
            isFromMe = true
        )
        _uiState.update { current ->
            current.copy(chatMessages = current.chatMessages + newMsg)
        }

        // Automatic authentic reply from vendor
        viewModelScope.launch {
            delay(1200L)
            val replyMsg = ChatMessage(
                id = "reply_${System.currentTimeMillis()}",
                senderId = "vendor_auto",
                senderName = _uiState.value.activeChatShop?.name ?: "دکاندار الکوزی مارکت",
                messageText = "سلام و احترام! پیام شما دریافت گردید. در خدمت شما هستیم و سفارش شما بلافاصله پس از ثبت بسته‌بندی خواهد شد.",
                timestampFa = "لحظاتی پیش",
                isFromMe = false
            )
            _uiState.update { it.copy(chatMessages = it.chatMessages + replyMsg) }
        }
    }

    fun addProductAsSeller(
        title: String,
        categoryId: String,
        priceAfn: Long,
        originalPriceAfn: Long,
        discountPercent: Int,
        description: String,
        imageUrl: String,
        isSpecialOffer: Boolean,
        stock: Int,
        specs: Map<String, String> = emptyMap()
    ) {
        val booth = _uiState.value.userProfile?.sellerBooth
        val newId = "user_prod_${System.currentTimeMillis()}"
        val newProduct = Product(
            id = newId,
            titleFa = title,
            vendorNameFa = booth?.boothNameFa ?: "غرفه من",
            vendorLocationFa = booth?.provinceFa ?: "کابل، افغانستان",
            categoryId = categoryId,
            priceAfn = priceAfn,
            originalPriceAfn = if (originalPriceAfn > 0) originalPriceAfn else priceAfn,
            discountPercent = discountPercent,
            rating = 5.0f,
            reviewCount = 1,
            imageUrl = imageUrl.ifBlank {
                "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80"
            },
            isSpecialOffer = isSpecialOffer,
            stock = stock.coerceAtLeast(1),
            descriptionFa = description.ifBlank { "کالای عرضه شده توسط غرفه رسمی ${booth?.boothNameFa}" },
            specs = specs.ifEmpty {
                mapOf(
                    "وضعیت" to "کاملاً نو و آکبند",
                    "فروشنده" to (booth?.boothNameFa ?: "غرفه رسمی"),
                    "گارانتی" to "تضمین اصالت کالا"
                )
            }
        )

        _uiState.update { current ->
            val updatedList = listOf(newProduct) + current.products
            val updatedIds = current.userCreatedProductIds + newId
            current.copy(
                products = updatedList,
                userCreatedProductIds = updatedIds,
                showAddProductDialog = false,
                authSuccessToast = "کالای «${newProduct.titleFa.take(22)}...» با موفقیت در بازار آنلاین درج شد!"
            )
        }
    }

    fun deleteSellerProduct(productId: String) {
        _uiState.update { current ->
            current.copy(
                products = current.products.filterNot { it.id == productId },
                userCreatedProductIds = current.userCreatedProductIds - productId,
                authSuccessToast = "کالا از غرفه شما حذف گردید."
            )
        }
    }

    // --- Advertising & Promotion Methods ---

    fun openStory(story: com.example.model.PromotionalStory) {
        _uiState.update { it.copy(activeStory = story) }
    }

    fun closeStory() {
        _uiState.update { it.copy(activeStory = null) }
    }

    fun selectCityAdFilter(city: String) {
        _uiState.update { it.copy(selectedCityAdFilter = city) }
    }

    fun togglePromoPopup(show: Boolean) {
        _uiState.update { it.copy(showPromoPopup = show) }
    }

    fun closeTopHeaderBanner() {
        _uiState.update { it.copy(isTopHeaderBannerVisible = false) }
    }

    fun applyVoucher(code: String) {
        val trimmed = code.trim().uppercase()
        if (trimmed == "AFGHAN500" || trimmed == "ALKOZI500" || trimmed == "EID-AFN") {
            _uiState.update {
                it.copy(
                    appliedVoucherCode = trimmed,
                    voucherDiscountAfn = 500L,
                    showPromoPopup = false,
                    authSuccessToast = "کد تخفیف ۵۰۰ افغانی با موفقیت روی سبد خرید شما اعمال شد!"
                )
            }
        } else {
            _uiState.update {
                it.copy(authSuccessToast = "کد تخفیف وارد شده نامعتبر است. لطفاً از کد AFGHAN500 استفاده کنید.")
            }
        }
    }

    // --- Notifications & Alerts ---

    fun toggleNotificationsSheet(show: Boolean) {
        _uiState.update { it.copy(showNotificationsSheet = show) }
    }

    fun markNotificationAsRead(id: String) {
        _uiState.update { current ->
            val updated = current.notifications.map {
                if (it.id == id) it.copy(isRead = true) else it
            }
            current.copy(notifications = updated)
        }
    }

    fun markAllNotificationsAsRead() {
        _uiState.update { current ->
            val updated = current.notifications.map { it.copy(isRead = true) }
            current.copy(notifications = updated, authSuccessToast = "تمامی اعلان‌ها به عنوان خوانده شده علامت‌گذاری شدند.")
        }
    }

    // --- Advanced Filter & Sort ---

    fun toggleFilterSortDialog(show: Boolean) {
        _uiState.update { it.copy(showFilterSortDialog = show) }
    }

    fun applyFilterSort(
        sort: ProductSortOption,
        province: String,
        inStockOnly: Boolean,
        specialOffersOnly: Boolean
    ) {
        _uiState.update {
            it.copy(
                productSortOption = sort,
                selectedProvinceFilter = province,
                onlyInStockFilter = inStockOnly,
                onlySpecialOffersFilter = specialOffersOnly,
                showFilterSortDialog = false,
                authSuccessToast = "فیلترها با موفقیت اعمال گردیدند."
            )
        }
    }

    fun resetFilterSort() {
        _uiState.update {
            it.copy(
                productSortOption = ProductSortOption.RECOMMENDED,
                selectedProvinceFilter = "همه ۳۴ ولایت",
                onlyInStockFilter = false,
                onlySpecialOffersFilter = false
            )
        }
    }

    // --- Language Localization ---

    fun setAppLanguage(lang: AppLanguage) {
        _uiState.update {
            it.copy(
                appLanguage = lang,
                authSuccessToast = "زبان برنامه تغییر یافت: ${lang.displayName}"
            )
        }
    }
}

