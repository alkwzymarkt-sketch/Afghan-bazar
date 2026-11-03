package com.example.model

enum class AppMode {
    MARKET,
    ADMIN
}

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    FA("fa", "دری / فارسی", true),
    PS("ps", "پښتو", true),
    EN("en", "English", false)
}

enum class UserRole(val titleFa: String, val badgeColorHex: Long) {
    CUSTOMER("خریدار", 0xFF6C757D),
    SELLER("دکاندار / فروشنده", 0xFF0F5132),
    PRODUCT_MODERATOR("ناظر کالا", 0xFF0288D1),
    FINANCE_MANAGER("مدیر مالی", 0xFFF57F17),
    GENERAL_ADMIN("مدیر عمومی", 0xFF6A1B9A),
    SUPER_ADMIN("مالک پلتفرم (Super Admin)", 0xFFD4AF37)
}

enum class OrderStatus(val titleFa: String, val colorHex: Long) {
    PENDING("در انتظار تأیید", 0xFFE65100),
    CONFIRMED("تأییدشده", 0xFF0288D1),
    PROCESSING("آماده ارسال", 0xFFF57F17),
    SHIPPED("ارسال‌شده", 0xFF6A1B9A),
    DELIVERED("تحویل‌شده", 0xFF2E7D32),
    CANCELLED("لغوشده", 0xFFC62828)
}

data class PlatformSettings(
    var isFreeSubscriptionEnabled: Boolean = true, // Crucial: Global toggle controlled in Alkozi Admin
    var monthlySubscriptionFeeAfn: Long = 1500L,
    var quarterlySubscriptionFeeAfn: Long = 4000L,
    var yearlySubscriptionFeeAfn: Long = 14000L,
    var codEnabled: Boolean = true,
    var mpaisaEnabled: Boolean = true,
    var mymoneyEnabled: Boolean = true,
    var bankTransferEnabled: Boolean = true,
    var supportPhone: String = "+93 700 123 456",
    var platformName: String = "Alkozi Market"
)

data class Shop(
    val id: String,
    val ownerId: String,
    val name: String,
    val businessPhone: String,
    val logoUrl: String,
    val bannerUrl: String,
    val description: String,
    val address: String,
    val province: String,
    val city: String,
    var isVerified: Boolean = true,
    var isActive: Boolean = true,
    var isExemptFromSubscription: Boolean = false, // Admin manual exemption
    var subscriptionPlan: String = "رایگان",
    var subscriptionExpiresAtFa: String = "۱۴۰۵/۱۲/۲۹",
    var rating: Float = 4.9f,
    var totalSalesAfn: Long = 125000L
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val messageText: String,
    val timestampFa: String,
    val isFromMe: Boolean
)

data class AuditLog(
    val id: String,
    val adminName: String,
    val actionType: String,
    val details: String,
    val timestampFa: String
)

enum class NotificationType(val titleFa: String, val iconColorHex: Long) {
    ORDER("سفارشات", 0xFF0F5132),
    VENDOR("غرفه و فروشگاه", 0xFFD4AF37),
    SYSTEM("سیستم و ادمین", 0xFF0288D1),
    PROMO("تخفیف و تبلیغات", 0xFFE65100)
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestampFa: String,
    var isRead: Boolean = false
)

enum class ProductSortOption(val titleFa: String, val titlePs: String, val titleEn: String) {
    RECOMMENDED("پیش‌فرض (اسپانسرها در ابتدا)", "اصلي (سپانسر شوي لومړی)", "Default / Sponsored First"),
    NEWEST("جدیدترین محصولات", "ترټولو نوي توکي", "Newest Arrivals"),
    PRICE_LOW_TO_HIGH("ارزان‌ترین قیمت", "ترټولو ارزانه بيه", "Price: Low to High"),
    PRICE_HIGH_TO_LOW("گران‌ترین قیمت", "ترټولو لوړه بيه", "Price: High to Low"),
    BEST_RATING("بیشترین امتیاز خریداران", "ترټولو غوره نمرې", "Highest Rated"),
    DISCOUNT("بیشترین درصد تخفیف", "ترټولو لوړ تخفیف", "Biggest Discount")
}

object AfghanProvinces {
    // 34 Provinces of Afghanistan Matrix
    val all34 = listOf(
        "کابل",
        "هرات",
        "بلخ",
        "ننگرهار",
        "قندهار",
        "کندز",
        "هلمند",
        "غزنی",
        "فاریاب",
        "بدخشان",
        "پکتیا",
        "بغلان",
        "تخار",
        "میدان وردک",
        "پروان",
        "سمنگان",
        "جوزجان",
        "غور",
        "بامیان",
        "لغمان",
        "بادغیس",
        "نیمروز",
        "فراه",
        "زابل",
        "کاپیسا",
        "سرپل",
        "کنر",
        "نورستان",
        "پنجشیر",
        "دایکندی",
        "خوست",
        "ارزگان",
        "پکتیکا",
        "لوگر"
    )
}

object DefaultCategories {
    // 16 Official Platform Categories
    val all16 = listOf(
        Category("mobile", "موبایل و تبلت", "Smartphone", 14),
        Category("computers", "کامپیوتر و لپ‌تاپ", "Laptop", 9),
        Category("home_appliances", "لوازم خانه", "Home", 12),
        Category("clothing", "لباس و پوشاک", "Checkroom", 18),
        Category("shoes_bags", "کفش و کیف", "ShoppingBag", 8),
        Category("vehicles", "وسایل نقلیه", "DirectionsCar", 6),
        Category("car_parts", "لوازم و پرزه‌جات موتر", "Build", 11),
        Category("groceries", "مواد غذایی", "Storefront", 22),
        Category("cosmetics", "لوازم آرایشی", "Face", 7),
        Category("baby_kids", "لوازم کودک", "ChildCare", 5),
        Category("sports", "وسایل ورزشی", "FitnessCenter", 8),
        Category("construction", "تجهیزات ساختمانی", "Handyman", 10),
        Category("services", "خدمات", "MiscellaneousServices", 4),
        Category("stationery", "کتاب و لوازم تحریر", "MenuBook", 9),
        Category("agriculture", "محصولات زراعتی", "Agriculture", 16),
        Category("others", "سایر محصولات", "Apps", 7)
    )
}

object SampleEcosystemData {
    val sampleShops = listOf(
        Shop(
            id = "shop_1",
            ownerId = "user_2",
            name = "دیجیتال کابل مارکت",
            businessPhone = "+93 700 123 456",
            logoUrl = "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1550009158-9ebf69173e03?w=800",
            description = "فروشگاه تخصصی انواع موبایل و تجهیزات دیجیتال با گارانتی اصالت در کابل",
            address = "چهارراهی دهبوری، مارکت ملی، طبقه ۲",
            province = "کابل",
            city = "کابل",
            isVerified = true,
            isActive = true,
            isExemptFromSubscription = false,
            subscriptionPlan = "اشتراک رایگان پلتفرم",
            subscriptionExpiresAtFa = "۱۴۰۵/۱۲/۲۹",
            rating = 4.9f,
            totalSalesAfn = 845000L
        ),
        Shop(
            id = "shop_2",
            ownerId = "user_3",
            name = "طلای سرخ هرات باستان",
            businessPhone = "+93 799 887 766",
            logoUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800",
            description = "تولید و صادرات مستقیم زعفران سوپر نگین و خشکبار صادراتی اعلای هرات",
            address = "جاده ولایت، روبروی درب ملک",
            province = "هرات",
            city = "هرات",
            isVerified = true,
            isActive = true,
            isExemptFromSubscription = true, // Admin exempted
            subscriptionPlan = "معافیت ویژه مدیریت",
            subscriptionExpiresAtFa = "دائمی (بدون انقضا)",
            rating = 5.0f,
            totalSalesAfn = 620000L
        ),
        Shop(
            id = "shop_3",
            ownerId = "user_4",
            name = "تکنولوژی آریانا بلخ",
            businessPhone = "+93 788 334 455",
            logoUrl = "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=800",
            description = "عرضه‌کننده لپ‌تاپ‌های گیمینگ و مهندسی ایسوس و لنوو در شمال کشور",
            address = "مزار شریف، جاده روضه شریف، مرکز تجاری بلخ",
            province = "بلخ",
            city = "مزار شریف",
            isVerified = true,
            isActive = true,
            isExemptFromSubscription = false,
            subscriptionPlan = "اشتراک سالانه طلایی",
            subscriptionExpiresAtFa = "۱۴۰۵/۰۹/۱۵",
            rating = 4.8f,
            totalSalesAfn = 430000L
        ),
        Shop(
            id = "shop_4",
            ownerId = "user_5",
            name = "فرش دستباف آقچه و جوزجان",
            businessPhone = "+93 777 112 233",
            logoUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=800",
            description = "تولید انواع قالین و گلیم سنتی پشمی و ابریشمی با رنگ‌های طبیعی گیاهی",
            address = "شبرغان، چوک آزادی",
            province = "جوزجان",
            city = "شبرغان",
            isVerified = false, // Pending verification
            isActive = true,
            isExemptFromSubscription = false,
            subscriptionPlan = "دوره آزمایشی",
            subscriptionExpiresAtFa = "۱۴۰۵/۰۳/۰۱",
            rating = 4.7f,
            totalSalesAfn = 290000L
        ),
        Shop(
            id = "shop_5",
            ownerId = "user_6",
            name = "صنایع دستی سفال استالف",
            businessPhone = "+93 744 998 877",
            logoUrl = "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=800",
            description = "سفالگری و ظروف سنتی فیروزه‌ای اصیل استالف کابل با قدمت تاریخی",
            address = "کابل، بازارچه تاریخی ولسوالی استالف",
            province = "کابل",
            city = "استالف",
            isVerified = true,
            isActive = true,
            isExemptFromSubscription = false,
            subscriptionPlan = "اشتراک رایگان پلتفرم",
            subscriptionExpiresAtFa = "۱۴۰۵/۱۲/۲۹",
            rating = 4.8f,
            totalSalesAfn = 185000L
        )
    )

    val sampleAuditLogs = listOf(
        AuditLog(
            id = "LOG-101",
            adminName = "احمد شاه الکوزی (Super Admin)",
            actionType = "تغییر کلید اشتراک پلتفرم",
            details = "فعال‌سازی سوییچ اشتراک رایگان دکانداران در سراسر ۳۴ ولایت",
            timestampFa = "۱۴۰۵/۰۲/۱۵ - ۱۰:۴۵"
        ),
        AuditLog(
            id = "LOG-102",
            adminName = "احمد شاه الکوزی (Super Admin)",
            actionType = "معافیت حق اشتراک دکان",
            details = "اعطای معافیت دائم اشتراک به «طلای سرخ هرات باستان» به پاس صادرات ملی",
            timestampFa = "۱۴۰۵/۰۲/۱۴ - ۱۶:۲۰"
        ),
        AuditLog(
            id = "LOG-103",
            adminName = "ثریا ننگرهاری (مدیر مالی)",
            actionType = "بروزرسانی تعرفه",
            details = "تنظیم تعرفه اشتراک ماهانه دکان‌ها: ۱۵۰۰ افغانی و سالانه ۱۴۰۰۰ افغانی",
            timestampFa = "۱۴۰۵/۰۲/۱۳ - ۱۱:۱۰"
        ),
        AuditLog(
            id = "LOG-104",
            adminName = "میرویس قندهاری (ناظر کالا)",
            actionType = "تأیید مدارک دکان",
            details = "تأیید جواز کسب و بررسی فیزیکی دکان «تکنولوژی آریانا بلخ»",
            timestampFa = "۱۴۰۵/۰۲/۱۲ - ۰۹:۳۰"
        )
    )

    val sampleChatMessages = listOf(
        ChatMessage(
            id = "msg_1",
            senderId = "buyer_1",
            senderName = "خریدار محترم",
            messageText = "سلام و احترام، آیا این جنس در انبار موجود است و به بلخ ارسال دارید؟",
            timestampFa = "۱۰:۱۵",
            isFromMe = false
        ),
        ChatMessage(
            id = "msg_2",
            senderId = "shop_1",
            senderName = "دیجیتال کابل مارکت",
            messageText = "سلام علیکم برادر گرامی! بله موجود است و تحویل به مزار شریف ظرف ۲۴ ساعت انجام می‌شود.",
            timestampFa = "۱۰:۱۷",
            isFromMe = true
        ),
        ChatMessage(
            id = "msg_3",
            senderId = "buyer_1",
            senderName = "خریدار محترم",
            messageText = "تشکر، آیا تست اصالت درب منزل هم دارد؟",
            timestampFa = "۱۰:۱۹",
            isFromMe = false
        ),
        ChatMessage(
            id = "msg_4",
            senderId = "shop_1",
            senderName = "دیجیتال کابل مارکت",
            messageText = "بله حتماً، همراه با فاکتور رسمی الکوزی و مهلت تست ۴۸ ساعته فیزیکی درب منزل است.",
            timestampFa = "۱۰:۲۱",
            isFromMe = true
        )
    )

    val sampleNotifications = listOf(
        AppNotification(
            id = "notif_1",
            title = "سفارش شما با موفقیت ثبت شد",
            message = "سفارش شماره AFN-8924 در حال آماده‌سازی توسط دکاندار است و ظرف ۲۴ ساعت تحویل خواهد شد.",
            type = NotificationType.ORDER,
            timestampFa = "۱۰ دقیقه پیش",
            isRead = false
        ),
        AppNotification(
            id = "notif_2",
            title = "فعال‌سازی سوییچ اشتراک رایگان دکانداران",
            message = "به دستور مدیریت پلتفرم، اشتراک رایگان برای تمامی دکانداران ۳۴ ولایت کشور فعال گردید.",
            type = NotificationType.SYSTEM,
            timestampFa = "۱ ساعت پیش",
            isRead = false
        ),
        AppNotification(
            id = "notif_3",
            title = "کد تخفیف ۵۰۰ افغانی ویژه کاربران جدید",
            message = "کد AFGHAN500 را هنگام پرداخت وارد نمایید و از ۵۰۰ افغانی تخفیف مستقیم بهره‌مند شوید.",
            type = NotificationType.PROMO,
            timestampFa = "دیروز",
            isRead = true
        ),
        AppNotification(
            id = "notif_4",
            title = "پیام جدید در گفتگوی آنلاین",
            message = "فروشگاه «دیجیتال کابل مارکت» به استعلام شما درباره اصالت کالا پاسخ داد.",
            type = NotificationType.VENDOR,
            timestampFa = "۲ روز پیش",
            isRead = true
        )
    )
}


