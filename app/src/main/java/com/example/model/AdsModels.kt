package com.example.model

import com.example.R

data class PromotionalStory(
    val id: String,
    val vendorNameFa: String,
    val avatarUrl: String,
    val storyTitleFa: String,
    val storyDescriptionFa: String,
    val mediaUrl: String,
    val ctaTextFa: String = "مشاهده محصولات این غرفه",
    val targetCategoryId: String? = null,
    val hasUnseen: Boolean = true
)

data class BannerAd(
    val id: String,
    val titleFa: String,
    val subtitleFa: String,
    val badgeFa: String,
    val cityFa: String, // "همه", "کابل", "هرات", "بلخ", "قندهار"
    val imageUrl: String,
    val gradientColors: List<Long>,
    val discountCode: String? = null,
    val targetCategoryId: String = "all"
)

data class PromotionalPopupData(
    val title: String = "جشنواره بزرگ طلایی افغانستان",
    val subtitle: String = "کد تخفیف ۵۰۰ افغانی برای اولین خرید از بازار الکوزی",
    val voucherCode: String = "AFGHAN500",
    val discountAmountAfn: Long = 500L,
    val minOrderAmountAfn: Long = 2000L,
    val expiresText: String = "مهلت استفاده: تا پایان این هفته",
    val imageRes: Int = R.drawable.promo_popup_gift_1793608214114
)

object SampleAdsData {
    val stories = listOf(
        PromotionalStory(
            id = "story_awcc",
            vendorNameFa = "افغان بیسیم",
            avatarUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "جشنواره سیم‌کارت و دیتای 4G افغان بیسیم",
            storyDescriptionFa = "با خرید هر گوشی هوشمند از الکوزی مارکت، یک بسته ۵۰ گیگابایت اینترنت رایگان هدیه بگیرید!",
            mediaUrl = "https://images.unsplash.com/photo-1512428559087-560fa5ceab42?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "مشاهده محصولات دیجیتال",
            targetCategoryId = "mobile"
        ),
        PromotionalStory(
            id = "story_saffron",
            vendorNameFa = "زعفران هرات",
            avatarUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "طلای سرخ اصیل مزارع غوریان هرات",
            storyDescriptionFa = "تخفیف ویژه ۲۵ درصدی روی پک‌های هدیه مخمل صادراتی به مناسبت فصل برداشت تازه.",
            mediaUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "خرید مستقیم زعفران هرات",
            targetCategoryId = "handicrafts"
        ),
        PromotionalStory(
            id = "story_carpet",
            vendorNameFa = "قالین بلخ باستان",
            avatarUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "فرش‌های نفیس ابریشمی مزار شریف",
            storyDescriptionFa = "بافته شده توسط برجسته‌ترین استادان قالیباف شمال کشور با رنگ‌های صد درصد گیاهی.",
            mediaUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "مشاهده کلکسیون قالین",
            targetCategoryId = "handicrafts"
        ),
        PromotionalStory(
            id = "story_roshan",
            vendorNameFa = "روشن تلکام",
            avatarUrl = "https://images.unsplash.com/photo-1573164713988-8665fc963095?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "پوشش سراسری اینترنت پرسرعت روشن",
            storyDescriptionFa = "تماس نامحدود درون‌شبکه‌ای و پشتیبانی ۲۴ ساعته برای تمامی خریداران الکوزی مارکت.",
            mediaUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "دیدن گوشی‌های سازگار با 4G",
            targetCategoryId = "mobile"
        ),
        PromotionalStory(
            id = "story_nuts",
            vendorNameFa = "خشکبار قندهار",
            avatarUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "بادام شیرین شونبندی و انجیر کوهی",
            storyDescriptionFa = "سوغات اصیل و تازه جنوب افغانستان با بسته‌بندی بهداشتی ضد رطوبت برای ارسال به سراسر جهان.",
            mediaUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "سفارش میوه خشک تازه",
            targetCategoryId = "supermarket"
        ),
        PromotionalStory(
            id = "story_digital",
            vendorNameFa = "کابل دیجیتال",
            avatarUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=200&auto=format&fit=crop&q=80",
            storyTitleFa = "پرچمداران سامسونگ و آیفون با گارانتی",
            storyDescriptionFa = "تحویل اکسپرس کمتر از ۲۴ ساعت در کلیه نواحی شهر کابل درب منزل با فاکتور رسمی.",
            mediaUrl = "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800&auto=format&fit=crop&q=80",
            ctaTextFa = "خرید اقساط و نقد",
            targetCategoryId = "mobile"
        )
    )

    // Twin Banners (بنرهای دوقلو)
    val twinBanners = listOf(
        BannerAd(
            id = "twin_1",
            titleFa = "حراج ویژه لوازم صوتی و هدفون",
            subtitleFa = "تا ۳۵٪ تخفیف ویژه مدل‌های سونی و انکر",
            badgeFa = "پیشنهاد طلایی",
            cityFa = "کابل",
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF6A11CB, 0xFF2575FC),
            discountCode = "AUDIO30",
            targetCategoryId = "digital"
        ),
        BannerAd(
            id = "twin_2",
            titleFa = "فروش ویژه قالین دستباف مزار",
            subtitleFa = "ارسال رایگان به سراسر ولایات افغانستان",
            badgeFa = "صنایع دستی",
            cityFa = "بلخ",
            imageUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=500&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFFB71C1C, 0xFFE65100),
            discountCode = "CARPET25",
            targetCategoryId = "handicrafts"
        )
    )

    // Quad Banners (بنرهای چهارقلو تفکیک‌شده)
    val quadBanners = listOf(
        BannerAd(
            id = "quad_1",
            titleFa = "موبایل و تبلت",
            subtitleFa = "تا ۲۰٪ تخفیف ویژه",
            badgeFa = "ارسال سریع",
            cityFa = "کابل",
            imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF0F2027, 0xFF203A43),
            targetCategoryId = "mobile"
        ),
        BannerAd(
            id = "quad_2",
            titleFa = "طلای سرخ هرات",
            subtitleFa = "سوپر نگین صادراتی",
            badgeFa = "تضمین عیار",
            cityFa = "هرات",
            imageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF4A0E17, 0xFFB71C1C),
            targetCategoryId = "handicrafts"
        ),
        BannerAd(
            id = "quad_3",
            titleFa = "خشکبار قندهار",
            subtitleFa = "بادام و پسته خلم",
            badgeFa = "تازه و مقوی",
            cityFa = "قندهار",
            imageUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=400&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF134E5E, 0xFF71B280),
            targetCategoryId = "supermarket"
        ),
        BannerAd(
            id = "quad_4",
            titleFa = "پوشاک سنتی و مدرن",
            subtitleFa = "خامک‌دوزی اصیل",
            badgeFa = "دوخت دستی",
            cityFa = "کابل",
            imageUrl = "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=400&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF4B1248, 0xFFF0C27B),
            targetCategoryId = "fashion"
        )
    )

    // City Banners (بنرهای تفکیک‌شده بر اساس ولایت/شهر)
    val cityBanners = listOf(
        BannerAd(
            id = "city_kabul",
            titleFa = "جشنواره اختصاصی تحویل اکسپرس کابل",
            subtitleFa = "تحویل سفارشات کمتر از ۳ ساعت در نواحی شهر کابل با پرداخت نقدی درب منزل",
            badgeFa = "کابل پایتخت",
            cityFa = "کابل",
            imageUrl = "https://images.unsplash.com/photo-1512428559087-560fa5ceab42?w=600&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF00416A, 0xFFE4E5E6),
            discountCode = "KABULFAST"
        ),
        BannerAd(
            id = "city_herat",
            titleFa = "بازارچه اختصاصی غرفه‌های هرات باستان",
            subtitleFa = "عرضه بدون واسطه زعفران ممتاز، پارچه‌های ابریشمی و سوغات نامدار هرات",
            badgeFa = "هرات باستان",
            cityFa = "هرات",
            imageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF8E0E00, 0xFF1F1C18),
            discountCode = "HERAT20"
        ),
        BannerAd(
            id = "city_balkh",
            titleFa = "تخفیف‌های ویژه سرای قالین و چرم مزار شریف",
            subtitleFa = "ارسال با بسته‌بندی بیمه‌شده از بلخ به تمام ولایات افغانستان",
            badgeFa = "بلخ و مزار",
            cityFa = "مزار شریف",
            imageUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=600&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFF134E5E, 0xFF71B280),
            discountCode = "BALKH40"
        ),
        BannerAd(
            id = "city_kandahar",
            titleFa = "حراج بزرگ خشکبار و انار مرغوب قندهار",
            subtitleFa = "مغز بادام کاغذی، پسته اعلا و انجیر درجه یک با تخفیف کلی",
            badgeFa = "قندهار زیبا",
            cityFa = "قندهار",
            imageUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=600&auto=format&fit=crop&q=80",
            gradientColors = listOf(0xFFD32F2F, 0xFFFF8F00),
            discountCode = "KANDAHAR15"
        )
    )

    val availableCities = listOf("همه ولایات", "کابل", "هرات", "مزار شریف", "قندهار")
}

// =========================================================================
// IN-APP ADVERTISING SYSTEM MODELS (Shared across Mobile & Admin)
// =========================================================================

enum class AdTargetType(val titleFa: String) {
    COMPANY("شرکت و برند تجاری"),
    SHOP("غرفه و دکان محلی"),
    PRODUCT("کالای خاص"),
    SERVICE("خدمات و سرویس‌ها")
}

enum class AdPlacementArea(val titleFa: String, val badgeColor: Long) {
    HOME_PAGE("صفحه اصلی (Home Slider)", 0xFF0F5132),
    TOP_PRODUCTS("بالای لیست محصولات", 0xFF0288D1),
    CATEGORIES("درون دسته‌بندی‌ها", 0xFF6A1B9A),
    SHOPS_PAGE("صفحه دکان‌ها و غرفه‌ها", 0xFFF57F17),
    FEATURED_SECTION("بخش ویژه پروموشن", 0xFFC2185B)
}

data class InAppAdvertisement(
    val id: String,
    val title: String,
    val description: String,
    val bannerImageUrl: String,
    val targetType: AdTargetType = AdTargetType.COMPANY,
    val placement: AdPlacementArea = AdPlacementArea.HOME_PAGE,
    val shopId: String? = null,
    val productId: String? = null,
    val advertiserPhone: String? = "+93 700 123 456",
    val whatsappLink: String? = "https://wa.me/93700123456",
    val websiteUrl: String? = "https://afghan-wireless.com",
    val feeChargedAfn: Long = 15000L,
    val startDate: String = "۱۴۰۵/۰۲/۰۱",
    val endDate: String = "۱۴۰۵/۰۳/۰۱",
    var isActive: Boolean = true,
    var impressionsCount: Long = 3240L,
    var clicksCount: Long = 385L,
    val createdByName: String = "احمد شاه الکوزی (Super Admin)"
)

object SampleInAppAdsData {
    val initialAds = listOf(
        InAppAdvertisement(
            id = "ad-101",
            title = "بسته طلایی اینترنت 4G و سیم‌کارت افغان بیسیم",
            description = "۵۰ گیگابایت اینترنت هدیه با خرید هر دستگاه دیجیتال از غرفه‌های منتخب کابل",
            bannerImageUrl = "https://images.unsplash.com/photo-1512428559087-560fa5ceab42?w=800&auto=format&fit=crop&q=80",
            targetType = AdTargetType.COMPANY,
            placement = AdPlacementArea.HOME_PAGE,
            advertiserPhone = "+93 700 123 456",
            whatsappLink = "https://wa.me/93700123456?text=Salam%20Alkozi%20Ad",
            websiteUrl = "https://afghan-wireless.com",
            feeChargedAfn = 15000L,
            startDate = "۱۴۰۵/۰۲/۰۱",
            endDate = "۱۴۰۵/۰۳/۰۱",
            isActive = true,
            impressionsCount = 4120L,
            clicksCount = 490L
        ),
        InAppAdvertisement(
            id = "ad-102",
            title = "تخفیف ویژه زعفران صادراتی هرات باستان",
            description = "طلای سرخ مزارع غوریان با ۲۵٪ تخفیف عیدانه و ارسال رایگان سراسر افغانستان",
            bannerImageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=800&auto=format&fit=crop&q=80",
            targetType = AdTargetType.SHOP,
            placement = AdPlacementArea.TOP_PRODUCTS,
            shopId = "shop-herat-saffron",
            advertiserPhone = "+93 799 887 766",
            whatsappLink = "https://wa.me/93799887766",
            websiteUrl = "https://saffron.af",
            feeChargedAfn = 8500L,
            startDate = "۱۴۰۵/۰۲/۰۵",
            endDate = "۱۴۰۵/۰۲/۲۵",
            isActive = true,
            impressionsCount = 2850L,
            clicksCount = 310L
        ),
        InAppAdvertisement(
            id = "ad-103",
            title = "قالین‌های دست‌باف ابریشمی مزار شریف",
            description = "تخفیف استثنایی فروشگاه قالین بلخ با کیفیت صادراتی درجه یک",
            bannerImageUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=800&auto=format&fit=crop&q=80",
            targetType = AdTargetType.SHOP,
            placement = AdPlacementArea.SHOPS_PAGE,
            shopId = "shop-balkh-carpets",
            advertiserPhone = "+93 778 899 000",
            whatsappLink = "https://wa.me/93778899000",
            websiteUrl = "https://alkozi-market.af",
            feeChargedAfn = 12000L,
            startDate = "۱۴۰۵/۰۲/۰۲",
            endDate = "۱۴۰۵/۰۳/۰۲",
            isActive = true,
            impressionsCount = 1890L,
            clicksCount = 215L
        )
    )
}

