package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PromotionalPopupData
import com.example.ui.ads.CityTargetedAdSection
import com.example.ui.ads.PromotionalPopupDialog
import com.example.ui.ads.PromotionalStoriesRow
import com.example.ui.ads.QuadBannersRow
import com.example.ui.ads.StoryViewerDialog
import com.example.ui.ads.TopHeaderAdBanner
import com.example.ui.ads.TwinBannersRow
import com.example.ui.auth.AuthDialog
import com.example.ui.auth.AuthScreen
import com.example.ui.components.AlkoziHeader
import com.example.ui.components.BannerSlider
import com.example.ui.components.CartBottomSheet
import com.example.ui.components.CategoryChips
import com.example.ui.components.CheckoutDialog
import com.example.ui.components.FloatingCartBar
import com.example.ui.components.IncredibleOffersRow
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ProductCard
import com.example.ui.components.ProductDetailSheet
import com.example.ui.profile.ProfileSheet
import com.example.ui.seller.AddProductDialog
import com.example.ui.seller.SellerBannerStrip
import com.example.ui.seller.SellerDashboardSheet
import com.example.ui.seller.SellerRegistrationDialog
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.model.AppMode
import com.example.ui.admin.AlkoziAdminScreen
import com.example.ui.chat.ChatDialog
import com.example.ui.components.FilterSortDialog
import com.example.ui.components.NotificationsSheet
import com.example.ui.theme.AlkoziMarketTheme
import com.example.viewmodel.AuthStep
import com.example.viewmodel.MarketUiState
import com.example.viewmodel.MarketViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MarketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlkoziMarketTheme {
                AlkoziMarketApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AlkoziMarketApp(viewModel: MarketViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val layoutDirection = if (uiState.appLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    // Handle toast messages
    LaunchedEffect(uiState.authSuccessToast) {
        uiState.authSuccessToast?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        // Alkozi Market: Customer and Seller Application (Admin is in standalone AlkoziAdminActivity)
        AnimatedContent(
            targetState = if (uiState.authStep != AuthStep.LOGGED_IN || uiState.userProfile == null) "AUTH" else "MARKET",
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "market_app_router"
        ) { screenKey ->
            when (screenKey) {
                "AUTH" -> {
                    // Screen 1: Registration with Afghan Phone & Operator Detection & OTP
                    AuthScreen(
                        uiState = uiState,
                        onPhoneChanged = viewModel::onPhoneInputChange,
                        onNameChanged = viewModel::onNameInputChange,
                        onQuickFillOperator = viewModel::quickFillOperatorSample,
                        onProceedToOtp = viewModel::proceedToOtp,
                        onOtpChanged = viewModel::onOtpCodeChange,
                        onAutoFillTestOtp = viewModel::autoFillTestOtp,
                        onVerifyOtp = { viewModel.verifyOtpCode() },
                        onResendSms = viewModel::resendOtpSms,
                        onRequestVoiceCall = viewModel::requestVoiceCallOtp,
                        onBackToPhone = viewModel::backToPhoneInput
                    )
                }
                else -> {
                    // Screen 2: Full Alkozi Market e-commerce store with Advertising System
                    AlkoziMarketStoreScreen(viewModel = viewModel, uiState = uiState)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlkoziMarketStoreScreen(viewModel: MarketViewModel, uiState: MarketUiState) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val productDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val checkoutSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val authSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val profileSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sellerRegSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sellerDashboardSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val addProductSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val chatSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val notificationsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val filterSortSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 1. Top Header Ad Banner (جایگاه بنر عریض بالای صفحه)
                TopHeaderAdBanner(
                    isVisible = uiState.isTopHeaderBannerVisible,
                    onBannerClick = { viewModel.togglePromoPopup(true) },
                    onCloseClick = viewModel::closeTopHeaderBanner
                )

                AlkoziHeader(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChange,
                    cartItemCount = uiState.totalCartItemsCount,
                    unreadNotificationsCount = uiState.unreadNotificationsCount,
                    userProfile = uiState.userProfile,
                    selectedProvince = uiState.selectedProvinceFilter,
                    currentLanguage = uiState.appLanguage,
                    onLanguageChange = viewModel::setAppLanguage,
                    onProvinceChange = viewModel::setProvinceFilter,
                    onCartClick = { viewModel.toggleCartSheet(true) },
                    onNotificationsClick = { viewModel.toggleNotificationsSheet(true) },
                    onFilterSortClick = { viewModel.toggleFilterSortDialog(true) },
                    onProfileClick = { viewModel.toggleProfileSheet(true) },
                    onAuthClick = { viewModel.toggleAuthDialog(true) }
                )
            }
        },
        bottomBar = {
            FloatingCartBar(
                totalItems = uiState.totalCartItemsCount,
                totalPriceAfn = uiState.cartFinalPayableAfn,
                onViewCartClick = { viewModel.toggleCartSheet(true) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F7F8))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("main_market_grid")
            ) {
                // 2. Promotional Stories (استوری‌های تبلیغاتی مگنت/اینستاگرام)
                item(span = { GridItemSpan(2) }) {
                    PromotionalStoriesRow(
                        stories = uiState.promotionalStories,
                        onStoryClick = viewModel::openStory
                    )
                }

                // Telecom Operators Announcement Strip
                item(span = { GridItemSpan(2) }) {
                    TelecomStrip(
                        onOpenAuth = { viewModel.toggleProfileSheet(true) }
                    )
                }

                // Seller Banner Strip: "این یک بازار آنلاین است هر کسی که بخواهد فروشنده شود"
                item(span = { GridItemSpan(2) }) {
                    SellerBannerStrip(
                        isSeller = uiState.userProfile?.isSeller == true,
                        boothName = uiState.userProfile?.sellerBooth?.boothNameFa,
                        onOpenSellerAction = {
                            if (uiState.userProfile?.isSeller == true) {
                                viewModel.toggleSellerDashboardSheet(true)
                            } else {
                                viewModel.toggleSellerRegistrationDialog(true)
                            }
                        }
                    )
                }

                // Promotional Banner Slider (Real generated 16:9 banners)
                item(span = { GridItemSpan(2) }) {
                    BannerSlider(
                        onBannerClick = { bannerTitle ->
                            Toast.makeText(context, "مشاهده $bannerTitle", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Digikala-style Incredible Offers Row with Live Countdown Timer
                item(span = { GridItemSpan(2) }) {
                    IncredibleOffersRow(
                        products = uiState.specialOfferProducts,
                        remainingSeconds = uiState.offersRemainingSeconds,
                        onProductClick = viewModel::openProductDetail,
                        onAddToCart = { prod ->
                            viewModel.addToCart(prod)
                            Toast.makeText(context, "«${prod.titleFa.take(20)}...» به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // 3. Twin Banners Row (ردیف بنرهای دوقلو)
                item(span = { GridItemSpan(2) }) {
                    TwinBannersRow(
                        onAdClick = { ad ->
                            viewModel.onCategorySelect(ad.targetCategoryId)
                            Toast.makeText(context, "مشاهده ${ad.titleFa}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Category Chips Row
                item(span = { GridItemSpan(2) }) {
                    CategoryChips(
                        categories = uiState.categories,
                        selectedCategoryId = uiState.selectedCategoryId,
                        onCategorySelected = viewModel::onCategorySelect
                    )
                }

                // 4. Quad Banners Row (ردیف بنرهای چهارقلو)
                item(span = { GridItemSpan(2) }) {
                    QuadBannersRow(
                        onAdClick = { ad ->
                            viewModel.onCategorySelect(ad.targetCategoryId)
                            Toast.makeText(context, "دسته ${ad.titleFa}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // 5. City/Provincial Targeted Ad Section (بنرهای تفکیک‌شده بر اساس شهر)
                item(span = { GridItemSpan(2) }) {
                    CityTargetedAdSection(
                        selectedCity = uiState.selectedCityAdFilter,
                        onSelectCity = viewModel::selectCityAdFilter,
                        onAdClick = { ad ->
                            ad.discountCode?.let { code ->
                                viewModel.applyVoucher(code)
                            }
                        }
                    )
                }

                // Section Title for Products (Highlighting Sponsored & Organic)
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (uiState.selectedCategoryId == "all") "کالاهای منتخب بازار (آگهی‌های برتر در ابتدا)" else "کالاهای این دسته",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF222222)
                            )
                        }
                        Text(
                            text = "${uiState.filteredProducts.size} کالا (واحد: افغانی)",
                            fontSize = 11.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }

                // Product Cards Grid (Sponsored products appear at the top)
                items(uiState.filteredProducts, key = { it.id }) { product ->
                    Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                        ProductCard(
                            product = product,
                            onClick = { viewModel.openProductDetail(product) },
                            onAddToCart = {
                                viewModel.addToCart(product)
                                Toast.makeText(context, "به سبد خرید افزوده شد", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // 6. Fullscreen Story Viewer Dialog
        uiState.activeStory?.let { story ->
            StoryViewerDialog(
                story = story,
                onDismiss = viewModel::closeStory,
                onCtaClick = { catId ->
                    catId?.let { viewModel.onCategorySelect(it) }
                }
            )
        }

        // 7. Promotional Popup Dialog (پاپ‌آپ اعلان تخفیف با طرح گرافیکی ۳بعدی)
        if (uiState.showPromoPopup) {
            PromotionalPopupDialog(
                data = PromotionalPopupData(),
                onApplyVoucher = viewModel::applyVoucher,
                onDismiss = { viewModel.togglePromoPopup(false) }
            )
        }

        // Product Detail Bottom Sheet
        uiState.selectedProductForDetail?.let { product ->
            ProductDetailSheet(
                product = product,
                onDismiss = viewModel::closeProductDetail,
                onAddToCart = { prod ->
                    viewModel.addToCart(prod)
                    Toast.makeText(context, "کالا به سبد خرید افزوده شد", Toast.LENGTH_SHORT).show()
                },
                onChatWithVendor = { prod ->
                    viewModel.openChatForProduct(prod)
                },
                sheetState = productDetailSheetState
            )
        }

        // In-app Real-time Chat with Vendors
        if (uiState.activeChatShop != null) {
            ChatDialog(
                shopName = uiState.activeChatShop.name,
                messages = uiState.chatMessages,
                onSendMessage = viewModel::sendChatMessage,
                onDismiss = viewModel::closeChat,
                sheetState = chatSheetState
            )
        }

        // Shopping Cart Bottom Sheet
        if (uiState.showCartSheet) {
            CartBottomSheet(
                cartItems = uiState.cartItems,
                subtotalAfn = uiState.cartSubtotalAfn,
                discountAfn = uiState.cartTotalDiscountAfn,
                shippingFeeAfn = uiState.shippingFeeAfn,
                finalPayableAfn = uiState.cartFinalPayableAfn,
                onDismiss = { viewModel.toggleCartSheet(false) },
                onUpdateQuantity = viewModel::updateCartItemQuantity,
                onRemoveItem = viewModel::removeFromCart,
                onClearCart = viewModel::clearCart,
                onProceedCheckout = viewModel::openCheckout,
                sheetState = cartSheetState
            )
        }

        // Afghan Checkout Dialog
        if (uiState.showCheckoutDialog) {
            CheckoutDialog(
                selectedProvince = uiState.selectedProvince,
                onProvinceSelected = viewModel::onProvinceSelected,
                customerAddress = uiState.customerAddress,
                onAddressChanged = viewModel::onAddressDetailChanged,
                paymentMethod = uiState.paymentMethod,
                onPaymentMethodChanged = viewModel::onPaymentMethodChanged,
                payableAfn = uiState.cartFinalPayableAfn,
                onConfirmOrder = viewModel::completeOrder,
                onDismiss = viewModel::closeCheckout,
                sheetState = checkoutSheetState
            )
        }

        // Order Success Modal
        uiState.lastCompletedOrder?.let { order ->
            OrderSuccessDialog(
                order = order,
                onDismiss = viewModel::dismissCompletedOrder
            )
        }

        // Auth & Afghan Operators Verification Sheet
        if (uiState.showAuthDialog) {
            AuthDialog(
                authStep = uiState.authStep,
                phoneInput = uiState.phoneInput,
                detectedOperator = uiState.detectedOperator,
                isPhoneValid = uiState.isPhoneValid,
                phoneErrorMessage = uiState.phoneErrorMessage,
                otpCode = uiState.otpCode,
                otpRemainingSeconds = uiState.otpRemainingSeconds,
                isVoiceCallRequested = uiState.isVoiceCallRequested,
                onPhoneChanged = viewModel::onPhoneInputChange,
                onQuickFillOperator = viewModel::quickFillOperatorSample,
                onProceedToOtp = viewModel::proceedToOtp,
                onOtpChanged = viewModel::onOtpCodeChange,
                onAutoFillTestOtp = viewModel::autoFillTestOtp,
                onVerifyOtp = { viewModel.verifyOtpCode() },
                onResendSms = viewModel::resendOtpSms,
                onRequestVoiceCall = viewModel::requestVoiceCallOtp,
                onBackToPhone = viewModel::backToPhoneInput,
                onDismiss = { viewModel.toggleAuthDialog(false) },
                sheetState = authSheetState
            )
        }

        // User Profile Sheet with Seller Booth links (Admin portal is removed into separate app)
        if (uiState.showProfileSheet) {
            ProfileSheet(
                userProfile = uiState.userProfile,
                onDismiss = { viewModel.toggleProfileSheet(false) },
                onLogout = viewModel::logout,
                onSwitchAccount = {
                    viewModel.toggleProfileSheet(false)
                    viewModel.logout()
                },
                onOpenSellerDashboard = {
                    viewModel.toggleProfileSheet(false)
                    viewModel.toggleSellerDashboardSheet(true)
                },
                onOpenSellerRegister = {
                    viewModel.toggleProfileSheet(false)
                    viewModel.toggleSellerRegistrationDialog(true)
                },
                sheetState = profileSheetState
            )
        }

        // Seller Booth Registration Sheet
        if (uiState.showSellerRegistrationDialog && uiState.userProfile != null) {
            SellerRegistrationDialog(
                userProfile = uiState.userProfile,
                onRegister = viewModel::registerSellerBooth,
                onDismiss = { viewModel.toggleSellerRegistrationDialog(false) },
                sheetState = sellerRegSheetState
            )
        }

        // Seller Dashboard Sheet
        if (uiState.showSellerDashboardSheet && uiState.userProfile?.sellerBooth != null) {
            SellerDashboardSheet(
                booth = uiState.userProfile.sellerBooth,
                products = uiState.products,
                userCreatedIds = uiState.userCreatedProductIds,
                onOpenAddProduct = { viewModel.toggleAddProductDialog(true) },
                onDeleteProduct = viewModel::deleteSellerProduct,
                onDismiss = { viewModel.toggleSellerDashboardSheet(false) },
                sheetState = sellerDashboardSheetState
            )
        }

        // Add Product Dialog (Seller)
        if (uiState.showAddProductDialog) {
            AddProductDialog(
                categories = uiState.categories,
                onAddProduct = { title, catId, price, original, disc, desc, img, isOffer, stock ->
                    viewModel.addProductAsSeller(
                        title = title,
                        categoryId = catId,
                        priceAfn = price,
                        originalPriceAfn = original,
                        discountPercent = disc,
                        description = desc,
                        imageUrl = img,
                        isSpecialOffer = isOffer,
                        stock = stock
                    )
                },
                onDismiss = { viewModel.toggleAddProductDialog(false) },
                sheetState = addProductSheetState
            )
        }

        // Real-time Notifications Center Sheet
        if (uiState.showNotificationsSheet) {
            NotificationsSheet(
                notifications = uiState.notifications,
                onMarkAllAsRead = viewModel::markAllNotificationsAsRead,
                onNotificationClick = { notif ->
                    viewModel.markNotificationAsRead(notif.id)
                },
                onDismiss = { viewModel.toggleNotificationsSheet(false) },
                sheetState = notificationsSheetState
            )
        }

        // Advanced Filter and Multi-factor Sorting Dialog
        if (uiState.showFilterSortDialog) {
            FilterSortDialog(
                currentSort = uiState.productSortOption,
                currentProvince = uiState.selectedProvinceFilter,
                onlyInStock = uiState.onlyInStockFilter,
                onlySpecialOffers = uiState.onlySpecialOffersFilter,
                onApply = { sort, prov, stock, offers ->
                    viewModel.applyFilterSort(sort, prov, stock, offers)
                },
                onReset = viewModel::resetFilterSort,
                onDismiss = { viewModel.toggleFilterSortDialog(false) },
                sheetState = filterSortSheetState
            )
        }
    }
}

@Composable
private fun TelecomStrip(onOpenAuth: () -> Unit) {
    Surface(
        color = Color(0xFFFFF3E0),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenAuth() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SimCard,
                contentDescription = "اپراتورها",
                tint = Color(0xFFE65100),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "احراز هویت هوشمند ۵ اپراتور مخابراتی افغانستان",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "افغان بیسیم (070/071) • روشن (079/072) • اتصالات (078/073) • MTN (077/076) • سلام (074/075)",
                    fontSize = 9.sp,
                    color = Color(0xFF6D4C41),
                    lineHeight = 13.sp
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFE65100)
            ) {
                Text(
                    text = "پروفایل من",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
