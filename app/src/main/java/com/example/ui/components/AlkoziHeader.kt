package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AfghanProvinces
import com.example.model.AppLanguage
import com.example.model.UserProfile
import com.example.ui.theme.AlkoziDarkGreen
import com.example.ui.theme.AlkoziDeepGreen
import com.example.ui.theme.AlkoziGold
import com.example.ui.theme.AlkoziGoldDark
import com.example.ui.theme.AlkoziLightGreen

@Composable
fun AlkoziHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    cartItemCount: Int,
    unreadNotificationsCount: Int,
    userProfile: UserProfile?,
    selectedProvince: String,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onProvinceChange: (String) -> Unit,
    onCartClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onFilterSortClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top branding & action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand logo & title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("brand_logo_title")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AlkoziDarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = AlkoziGold,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "الکوزی",
                                color = AlkoziDarkGreen,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 19.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "مارکت",
                                color = AlkoziGoldDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp
                            )
                        }
                        Text(
                            text = "بازار آنلاین افغانستان (AFN)",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                    }
                }

                // Action buttons: Cart, Language & User Profile (Admin is strictly removed into standalone app)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language quick switcher
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF1F8F5),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val nextLang = when (currentLanguage) {
                                    AppLanguage.FA -> AppLanguage.PS
                                    AppLanguage.PS -> AppLanguage.EN
                                    AppLanguage.EN -> AppLanguage.FA
                                }
                                onLanguageChange(nextLang)
                            }
                            .testTag("language_switcher_btn")
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.FA -> "دری"
                                AppLanguage.PS -> "پښتو"
                                AppLanguage.EN -> "EN"
                            },
                            color = AlkoziDarkGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Notifications button with badge
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.testTag("notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationsCount > 0) {
                                    Badge(
                                        containerColor = Color(0xFFD32F2F),
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = unreadNotificationsCount.toString(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "اعلان‌ها",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Profile button / Operator badge
                    if (userProfile != null) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = userProfile.operator.accentColor,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onProfileClick() }
                                .testTag("profile_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(userProfile.operator.primaryColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = userProfile.operator.name.uppercase(),
                                    color = userProfile.operator.tagColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "پروفایل",
                                    tint = userProfile.operator.primaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        IconButton(
                            onClick = onAuthClick,
                            modifier = Modifier.testTag("login_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PersonOutline,
                                contentDescription = "ورود / ثبت‌نام",
                                tint = AlkoziDarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Shopping Cart button with badge
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier.testTag("cart_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(
                                        containerColor = AlkoziDarkGreen,
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = cartItemCount.toString(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "سبد خرید",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("search_input"),
                placeholder = {
                    Text(
                        text = "جستجو در هزاران کالای الکوزی مارکت (۳۴ ولایت)...",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "جستجو",
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "پاک کردن",
                                tint = Color(0xFF888888),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AlkoziDarkGreen,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFF7F9F8)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 34 Provinces Filter Chips Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "ولایت",
                    tint = AlkoziDarkGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ولایت:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF555555)
                )
                Spacer(modifier = Modifier.width(6.dp))

                // Advanced filter & sort button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onFilterSortClick() }
                        .testTag("filter_sort_header_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "فیلتر پیشرفته",
                            tint = AlkoziDarkGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "فیلتر پیشرفته",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlkoziDarkGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                val provinceOptions = listOf("همه ۳۴ ولایت") + AfghanProvinces.all34.take(12)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(provinceOptions) { province ->
                        val isSelected = selectedProvince == province
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) AlkoziDarkGreen else Color(0xFFF0F3F1),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onProvinceChange(province) }
                        ) {
                            Text(
                                text = province,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF333333),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
