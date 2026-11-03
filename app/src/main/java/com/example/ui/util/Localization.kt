package com.example.ui.util

import com.example.model.AppLanguage

object AppStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "الکوزی مارکت"
        AppLanguage.PS -> "الکوزی مارکیټ"
        AppLanguage.EN -> "Alkozi Market"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "بازار آنلاین افغانستان (AFN)"
        AppLanguage.PS -> "د افغانستان آنلاین بازار (AFN)"
        AppLanguage.EN -> "Afghanistan Online Market (AFN)"
    }

    fun searchPlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "جستجوی کالا، برند یا غرفه..."
        AppLanguage.PS -> "د توکو، نښې یا هټۍ لټون..."
        AppLanguage.EN -> "Search products, brands, or shops..."
    }

    fun adminPortal(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "پنل مدیریت"
        AppLanguage.PS -> "اداري پنل"
        AppLanguage.EN -> "Admin Panel"
    }

    fun cart(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "سبد خرید"
        AppLanguage.PS -> "د پیرودلو ټوکرۍ"
        AppLanguage.EN -> "Cart"
    }

    fun filterAndSort(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "فیلتر و مرتب‌سازی"
        AppLanguage.PS -> "چاڼ او ترتیب"
        AppLanguage.EN -> "Filter & Sort"
    }

    fun notifications(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "اعلان‌ها"
        AppLanguage.PS -> "خبرتیاوې"
        AppLanguage.EN -> "Notifications"
    }

    fun allProvinces(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "همه ۳۴ ولایت"
        AppLanguage.PS -> "ټول ۳۴ ولایتونه"
        AppLanguage.EN -> "All 34 Provinces"
    }

    fun afnCurrency(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "افغانی"
        AppLanguage.PS -> "افغانۍ"
        AppLanguage.EN -> "AFN"
    }

    fun addToCart(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "افزودن به سبد خرید"
        AppLanguage.PS -> "ټوکرۍ ته اضافه کول"
        AppLanguage.EN -> "Add to Cart"
    }

    fun freeDeliveryAbove3000(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "ارسال رایگان برای خریدهای بالای ۳,۰۰۰ افغانی در سراسر ولایات"
        AppLanguage.PS -> "په ټول هیواد کې تر ۳۰۰۰ افغانیو لوړ پیرود لپاره وړیا لیږد"
        AppLanguage.EN -> "Free shipping across all provinces for orders over 3,000 AFN"
    }

    fun changeLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "تغییر زبان"
        AppLanguage.PS -> "ژبه بدلول"
        AppLanguage.EN -> "Language"
    }

    fun sellerBanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "این یک بازار آنلاین است؛ هر کسی می‌تواند دکان بسازد و فروشنده شود!"
        AppLanguage.PS -> "دا یو آنلاین بازار دی؛ هر څوک کولی شي خپل پلورنځی جوړ کړي!"
        AppLanguage.EN -> "This is an open marketplace; anyone can build a shop and start selling!"
    }

    fun createShop(lang: AppLanguage): String = when (lang) {
        AppLanguage.FA -> "ساخت دکان اختصاصی"
        AppLanguage.PS -> "خپل پلورنځی جوړ کړئ"
        AppLanguage.EN -> "Create My Shop"
    }
}
