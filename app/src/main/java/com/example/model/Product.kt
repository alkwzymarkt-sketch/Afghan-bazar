package com.example.model

data class Category(
    val id: String,
    val nameFa: String,
    val iconName: String,
    val productCount: Int
)

data class Product(
    val id: String,
    val titleFa: String,
    val vendorNameFa: String,
    val vendorLocationFa: String,
    val categoryId: String,
    val priceAfn: Long,
    val originalPriceAfn: Long,
    val discountPercent: Int,
    val rating: Float,
    val reviewCount: Int,
    val imageUrl: String,
    val isSpecialOffer: Boolean = false,
    val isSponsored: Boolean = false,
    val stock: Int = 15,
    val descriptionFa: String = "",
    val specs: Map<String, String> = emptyMap()
)

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class SellerBooth(
    val boothId: String,
    val boothNameFa: String,
    val provinceFa: String,
    val categoryFa: String,
    val descriptionFa: String,
    val contactPhone: String,
    val totalRevenueAfn: Long = 0L,
    val totalSalesCount: Int = 0,
    val registeredDateFa: String = "۱۴۰۵/۰۲/۱۵"
)

data class UserProfile(
    val phone: String,
    val operator: TelecomOperator,
    val nameFa: String = "کاربر محترم الکوزی",
    val balanceAfn: Long = 15000L,
    val province: String = "کابل",
    val cityFa: String = "کابل",
    val role: UserRole = UserRole.CUSTOMER,
    val isSuspended: Boolean = false,
    val sellerBooth: SellerBooth? = null
) {
    val isSeller: Boolean
        get() = sellerBooth != null || role == UserRole.SELLER
    val isAdmin: Boolean
        get() = role in listOf(UserRole.GENERAL_ADMIN, UserRole.SUPER_ADMIN, UserRole.PRODUCT_MODERATOR, UserRole.FINANCE_MANAGER)
}

data class Order(
    val id: String,
    val dateFa: String,
    val items: List<CartItem>,
    val totalAfn: Long,
    val provinceFa: String,
    val addressDetailFa: String,
    val statusFa: String,
    val paymentMethodFa: String,
    var orderStatus: OrderStatus = OrderStatus.PENDING
)

object SampleMarketData {
    val categories = listOf(Category("all", "همه کالاها", "Apps", 50)) + DefaultCategories.all16
    val afghanProvinces = AfghanProvinces.all34

    val products = listOf(
        // Incredible Offers
        Product(
            id = "prod_1",
            titleFa = "گوشی موبایل سامسونگ Galaxy S25 Ultra حافظه ۵۱۲ گیگابایت",
            vendorNameFa = "دیجیتال کابل مارکت (تضمین اصالت)",
            vendorLocationFa = "کابل، فروشگاه دهبوری",
            categoryId = "mobile",
            priceAfn = 84500L,
            originalPriceAfn = 96000L,
            discountPercent = 12,
            rating = 4.9f,
            reviewCount = 142,
            imageUrl = "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = true,
            isSponsored = true,
            stock = 8,
            descriptionFa = "جدیدترین پرچمدار سامسونگ با دوربین ۲۰۰ مگاپیکسلی، قلم هوشمند S-Pen، پردازنده اسنپ‌دراگون و پشتیبانی از شبکه‌های 4G/5G تمامی اپراتورهای افغانستان.",
            specs = mapOf(
                "حافظه داخلی" to "512 گیگابایت",
                "رم" to "16 گیگابایت",
                "رنگ" to "تیتانیوم مشکی",
                "گارانتی" to "۱۸ ماهه رسمی الکوزی"
            )
        ),
        Product(
            id = "prod_2",
            titleFa = "زعفران نگین سوپر ممتاز هرات (بسته‌بندی صادراتی کادویی ۵۰ گرام)",
            vendorNameFa = "غرفه طلای سرخ هرات باستان",
            vendorLocationFa = "هرات، درب ملک",
            categoryId = "handicrafts",
            priceAfn = 6800L,
            originalPriceAfn = 8900L,
            discountPercent = 24,
            rating = 5.0f,
            reviewCount = 280,
            imageUrl = "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = true,
            stock = 25,
            descriptionFa = "زعفران درجه یک اصل ولایت هرات با عطر فوق‌العاده و قدرت رنگ‌دهی بی‌نظیر آزمایشگاهی. تست شده و دارای سرتیفیکیت کیفی صادراتی بین‌المللی.",
            specs = mapOf(
                "نوع زعفران" to "سوپر نگین اعلا",
                "وزن خالص" to "۵۰ گرام",
                "مبدا تولید" to "ولسوالی غوریان هرات",
                "بسته‌بندی" to "جعبه مخمل خاتم‌کاری"
            )
        ),
        Product(
            id = "prod_3",
            titleFa = "لپ‌تاپ گیمینگ ایسوس TUF Gaming F15 پردازنده Core i7 نسل ۱۳",
            vendorNameFa = "تکنولوژی آریانا",
            vendorLocationFa = "کابل، پارک زرنگار",
            categoryId = "mobile",
            priceAfn = 71200L,
            originalPriceAfn = 82000L,
            discountPercent = 13,
            rating = 4.8f,
            reviewCount = 95,
            imageUrl = "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = true,
            stock = 5,
            descriptionFa = "قدرت پردازش فوق‌العاده برای گیمرها، مهندسین و برنامه‌نویسان با کارت گرافیک RTX 4060 و سیستم خنک‌کننده دوگانه ضد گرد و غبار.",
            specs = mapOf(
                "پردازنده" to "Intel Core i7 13700H",
                "رم" to "32GB DDR5",
                "کارت گرافیک" to "NVIDIA RTX 4060 8GB",
                "صفحه نمایش" to "144Hz IPS FHD"
            )
        ),
        Product(
            id = "prod_4",
            titleFa = "قالین دستباف ابریشمی نقشه قزاق مزار شریف (ابعاد ۲ در ۳ متر)",
            vendorNameFa = "فرش و قالین سرای بلخ باستان",
            vendorLocationFa = "مزار شریف، جاده بلخ",
            categoryId = "handicrafts",
            priceAfn = 43000L,
            originalPriceAfn = 55000L,
            discountPercent = 22,
            rating = 4.9f,
            reviewCount = 64,
            imageUrl = "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = true,
            stock = 3,
            descriptionFa = "شاهکار هنر دستباف قالیبافان شمال افغانستان با الیاف ۱۰۰٪ پشم طبیعی و ابریشم خالص با رنگ‌های گیاهی ماندگار صد ساله.",
            specs = mapOf(
                "ابعاد" to "۲ × ۳ متر (۶ متر مربع)",
                "جنس تار و پود" to "پشم مرینوس و ابریشم",
                "رنگ‌رزی" to "کاملاً گیاهی و طبیعی",
                "تراکم" to "رج‌شمار ۵۰ چله"
            )
        ),
        Product(
            id = "prod_5",
            titleFa = "اپل آیفون 16 پرو مکس ۲۵۶ گیگابایت رنگ تیتانیوم صحرایی",
            vendorNameFa = "اپل سنتر کابل (Apple Kabul)",
            vendorLocationFa = "کابل، شهر نو، مرکز تجاری مجید",
            categoryId = "mobile",
            priceAfn = 98000L,
            originalPriceAfn = 105000L,
            discountPercent = 7,
            rating = 5.0f,
            reviewCount = 310,
            imageUrl = "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            isSponsored = true,
            stock = 12,
            descriptionFa = "گوشی هوشمند اپل مدل iPhone 16 Pro Max سیم‌کارت فیزیکی + سیم‌کارت الکترونیک سازگار کامل با اپراتورهای روشن، اتصالات و افغان بیسیم.",
            specs = mapOf(
                "حافظه" to "256GB",
                "بدنه" to "تیتانیوم گرید ۵ فضایی",
                "پردازنده" to "Apple A18 Pro Bionic",
                "وضعیت" to "پلمپ اصلی با کد رجیستری"
            )
        ),
        Product(
            id = "prod_6",
            titleFa = "پک لوکس مغزیات و میوه خشک قندهار (بادام شونبندی، پسته و انجیر اعلا - ۱ کیلو)",
            vendorNameFa = "خشکبار سرای احمد شاه بابا",
            vendorLocationFa = "قندهار، چوک مدد",
            categoryId = "supermarket",
            priceAfn = 2950L,
            originalPriceAfn = 3800L,
            discountPercent = 22,
            rating = 4.9f,
            reviewCount = 188,
            imageUrl = "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            stock = 40,
            descriptionFa = "مغز بادام کاغذی شیرین قندهار، انجیر کوهی درجه یک، توت خشک هرات و پسته دست‌چین خلم با طعم تازه و بسته‌بندی زیپ‌کیپ وکیوم.",
            specs = mapOf(
                "محتویات" to "بادام، پسته، انجیر، چهارمغز",
                "وزن" to "۱ کیلوگرم خالص",
                "ماندگاری" to "۱۲ ماه در جای خشک و خنک"
            )
        ),
        Product(
            id = "prod_7",
            titleFa = "هدفون بی‌سیم نویز کنسلینگ سونی Sony WH-1000XM5",
            vendorNameFa = "دیجیتال کابل مارکت",
            vendorLocationFa = "کابل، فروشگاه دهبوری",
            categoryId = "digital",
            priceAfn = 24900L,
            originalPriceAfn = 28500L,
            discountPercent = 13,
            rating = 4.8f,
            reviewCount = 76,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            stock = 14,
            descriptionFa = "بهترین کیفیت صدای استودیویی با حذف نویز محیطی هوشمند، باتری ۳۰ ساعته و شارژ سریع ۳ دقیقه‌ای برای ۳ ساعت پخش موسیقی.",
            specs = mapOf(
                "عمر باتری" to "30 ساعت مداوم",
                "فناوری" to "Active Noise Cancellation",
                "اتصال" to "Bluetooth 5.2 و کابل جک 3.5mm"
            )
        ),
        Product(
            id = "prod_8",
            titleFa = "پیراهن تنبان سنتی دست‌دوز خامک‌دوزی قندهاری اصیل",
            vendorNameFa = "پوشاک سنتی احمدی",
            vendorLocationFa = "قندهار، راسته بازار",
            categoryId = "fashion",
            priceAfn = 5400L,
            originalPriceAfn = 6500L,
            discountPercent = 17,
            rating = 4.9f,
            reviewCount = 82,
            imageUrl = "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            stock = 18,
            descriptionFa = "لباس مردانه فاخر افغانی از پارچه کتان مرغوب خنک به همراه سوزن‌دوزی و خامک‌دوزی ظریف یقه و سرآستین با دست.",
            specs = mapOf(
                "سایز" to "فری سایز با بند تنبان تنظیم‌شونده",
                "جنس پارچه" to "کتان نخ ۱۰۰٪ ارگانیک",
                "نوع دوخت" to "خامک‌دوزی دستی اصیل قندهار"
            )
        ),
        Product(
            id = "prod_9",
            titleFa = "ساعت هوشمند اپل واچ سری ۱۰ آلومینیومی ۴۶ میلی‌متری",
            vendorNameFa = "اپل سنتر کابل",
            vendorLocationFa = "کابل، شهر نو",
            categoryId = "digital",
            priceAfn = 32000L,
            originalPriceAfn = 35500L,
            discountPercent = 10,
            rating = 4.7f,
            reviewCount = 59,
            imageUrl = "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            stock = 9,
            descriptionFa = "باریک‌ترین اپل واچ با بزرگ‌ترین نمایشگر همیشه روشن، پایش ضربان قلب، اکسیژن خون و سنسور خواب پیشرفته.",
            specs = mapOf(
                "اندازه قاب" to "46mm",
                "مقاومت" to "ضدآب تا عمق 50 متری",
                "قابلیت مکالمه" to "اسپیکر و میکروفون داخلی"
            )
        ),
        Product(
            id = "prod_10",
            titleFa = "سرویس چای‌خوری سفالی دست‌ساز فیروزه‌ای اصیل استالف کابل",
            vendorNameFa = "صنایع دستی استالف باستان",
            vendorLocationFa = "کابل، ولسوالی استالف",
            categoryId = "home",
            priceAfn = 3600L,
            originalPriceAfn = 4500L,
            discountPercent = 20,
            rating = 4.8f,
            reviewCount = 43,
            imageUrl = "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=600&auto=format&fit=crop&q=80",
            isSpecialOffer = false,
            stock = 7,
            descriptionFa = "سرویس سفالی ۶ نفره شامل قوری، پیاله‌ها، نعلبکی و قندان با لعاب سبز فیروزه‌ای سنتی مشهور استالف.",
            specs = mapOf(
                "تعداد پارچه" to "۱۲ پارچه کامل",
                "لعاب" to "لعاب سنتی فیروزه‌ای نسوز",
                "هنر" to "چرخ‌کاری و کوره‌ای استالف"
            )
        )
    )

    val sampleOrders = listOf(
        Order(
            id = "ALK-908231",
            dateFa = "۱۴۰۵/۰۲/۱۵",
            items = listOf(CartItem(product = products[0], quantity = 1)),
            totalAfn = 84500L,
            provinceFa = "کابل",
            addressDetailFa = "شهر نو، کوچه قصابی‌ها، پلاک ۱۲",
            statusFa = "در حال بسته‌بندی در انبار مرکزی",
            paymentMethodFa = "پرداخت نقدی هنگام تحویل (کابل اکسپرس)",
            orderStatus = OrderStatus.PROCESSING
        ),
        Order(
            id = "ALK-884120",
            dateFa = "۱۴۰۵/۰۲/۱۴",
            items = listOf(CartItem(product = products[1], quantity = 2)),
            totalAfn = 13600L,
            provinceFa = "هرات",
            addressDetailFa = "جاده بهزاد، کوچه حاجی یعقوب",
            statusFa = "تحویل پست هوایی گردید",
            paymentMethodFa = "حساب M-Paisa روشن",
            orderStatus = OrderStatus.SHIPPED
        ),
        Order(
            id = "ALK-773419",
            dateFa = "۱۴۰۵/۰۲/۱۳",
            items = listOf(CartItem(product = products[2], quantity = 1)),
            totalAfn = 71200L,
            provinceFa = "بلخ",
            addressDetailFa = "مزار شریف، کارته آریانا، کوچه ۳",
            statusFa = "تحویل داده شد به مشتری",
            paymentMethodFa = "حواله صرافی و بانکی",
            orderStatus = OrderStatus.DELIVERED
        ),
        Order(
            id = "ALK-652190",
            dateFa = "۱۴۰۵/۰۲/۱۵",
            items = listOf(CartItem(product = products[3], quantity = 1)),
            totalAfn = 48000L,
            provinceFa = "قندهار",
            addressDetailFa = "عینو مینه، فاز دوم، خیابان صلح",
            statusFa = "در انتظار تأیید پرداخت",
            paymentMethodFa = "کیف پول MyMoney اتصالات",
            orderStatus = OrderStatus.PENDING
        )
    )

    val sampleUsers = listOf(
        UserProfile(
            phone = "+93 700 000 001",
            operator = TelecomOperator.AWCC,
            nameFa = "احمد شاه الکوزی (مالک پلتفرم)",
            balanceAfn = 500000L,
            province = "کابل",
            cityFa = "کابل، شیرپور",
            role = UserRole.SUPER_ADMIN,
            isSuspended = false
        ),
        UserProfile(
            phone = "+93 700 123 456",
            operator = TelecomOperator.AWCC,
            nameFa = "محمد داوود کابل‌وال",
            balanceAfn = 28500L,
            province = "کابل",
            cityFa = "کابل، دهبوری",
            role = UserRole.SELLER,
            isSuspended = false
        ),
        UserProfile(
            phone = "+93 799 887 766",
            operator = TelecomOperator.ROSHAN,
            nameFa = "زرغونه هروی",
            balanceAfn = 45000L,
            province = "هرات",
            cityFa = "هرات، درب ملک",
            role = UserRole.SELLER,
            isSuspended = false
        ),
        UserProfile(
            phone = "+93 788 334 455",
            operator = TelecomOperator.ETISALAT,
            nameFa = "فرهاد بلخی",
            balanceAfn = 12000L,
            province = "بلخ",
            cityFa = "مزار شریف",
            role = UserRole.CUSTOMER,
            isSuspended = false
        ),
        UserProfile(
            phone = "+93 777 112 233",
            operator = TelecomOperator.MTN,
            nameFa = "میرویس قندهاری",
            balanceAfn = 35000L,
            province = "قندهار",
            cityFa = "عینو مینه",
            role = UserRole.PRODUCT_MODERATOR,
            isSuspended = false
        ),
        UserProfile(
            phone = "+93 744 998 877",
            operator = TelecomOperator.SALAAM,
            nameFa = "ثریا ننگرهاری",
            balanceAfn = 62000L,
            province = "ننگرهار",
            cityFa = "جلال‌آباد",
            role = UserRole.FINANCE_MANAGER,
            isSuspended = false
        )
    )
}

