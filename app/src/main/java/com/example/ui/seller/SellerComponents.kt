package com.example.ui.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Category
import com.example.model.Product
import com.example.model.SampleMarketData
import com.example.model.SellerBooth
import com.example.model.UserProfile
import com.example.ui.theme.DigikalaDarkRed
import com.example.ui.theme.DigikalaRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenSuccess
import java.text.DecimalFormat

@Composable
fun SellerBannerStrip(
    isSeller: Boolean,
    boothName: String?,
    onOpenSellerAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpenSellerAction() }
            .testTag("seller_banner_strip")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF334155)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddBusiness,
                    contentDescription = "فروشنده شوید",
                    tint = GoldAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSeller) "غرفه شما: $boothName" else "هر کسی می‌تواند در الکوزی مارکت فروشنده شود!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isSeller) "مدیریت کالاها، مشاهده درآمد به افغانی و درج محصول جدید" else "بدون کارمزد غرفه بسازید و اجناس خود را به سراسر افغانستان بفروشید",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSeller) DigikalaRed else GoldAccent
            ) {
                Text(
                    text = if (isSeller) "داشبورد" else "افتتاح غرفه",
                    color = if (isSeller) Color.White else Color(0xFF1E293B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerRegistrationDialog(
    userProfile: UserProfile,
    onRegister: (boothName: String, province: String, category: String, description: String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    var boothName by remember { mutableStateOf("غرفه ${userProfile.nameFa}") }
    var selectedProvince by remember { mutableStateOf("کابل (تحویل اکسپرس ۲۴ ساعته)") }
    var selectedCategory by remember { mutableStateOf("موبایل و کالای دیجیتال") }
    var description by remember { mutableStateOf("عرضه و پخش مستقیم انواع کالاهای درجه یک و با کیفیت در افغانستان") }

    var expandedProvince by remember { mutableStateOf(false) }

    val categoriesList = listOf(
        "موبایل و کالای دیجیتال",
        "صنایع دستی و قالین افغانی",
        "زعفران و خشکبار اصیل",
        "پوشاک و لباس سنتی",
        "لوازم خانه و آشپزخانه",
        "سوپرمارکت و مواد غذایی"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("seller_registration_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddBusiness,
                        contentDescription = "ثبت غرفه",
                        tint = DigikalaRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "افتتاح رایگان غرفه فروشندگی",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF222222)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "بستن")
                }
            }

            Text(
                text = "در الکوزی مارکت هر فروشنده، دکان‌دار یا تولیدکننده افغان می‌تواند مستقیماً غرفه آنلاین داشته باشد.",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp)
            )

            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 12.dp))

            // Booth Name
            Text(text = "نام غرفه / دکان شما:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = boothName,
                onValueChange = { boothName = it },
                placeholder = { Text("مثال: زعفران هرات باستان، پوشاک احمدی و...") },
                modifier = Modifier.fillMaxWidth().testTag("seller_booth_name_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Province
            Text(text = "ولایت محل استقرار غرفه:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = expandedProvince,
                onExpandedChange = { expandedProvince = !expandedProvince }
            ) {
                OutlinedTextField(
                    value = selectedProvince,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvince) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed)
                )
                ExposedDropdownMenu(
                    expanded = expandedProvince,
                    onDismissRequest = { expandedProvince = false }
                ) {
                    SampleMarketData.afghanProvinces.forEach { prov ->
                        DropdownMenuItem(
                            text = { Text(prov, fontSize = 13.sp) },
                            onClick = {
                                selectedProvince = prov
                                expandedProvince = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category choice
            Text(text = "زمینه اصلی فعالیت غرفه:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categoriesList) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Color(0xFFFFF0F2) else Color(0xFFF5F5F5),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) DigikalaRed else Color.Transparent
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) DigikalaRed else Color(0xFF444444),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Phone info
            Surface(
                color = userProfile.operator.accentColor,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "شماره تایید شده",
                        tint = userProfile.operator.primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "شماره تماس غرفه: ${userProfile.phone} (${userProfile.operator.faName})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = userProfile.operator.tagColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            Text(text = "توضیحات و بیوگرافی غرفه:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onRegister(boothName, selectedProvince, selectedCategory, description)
                },
                enabled = boothName.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("confirm_open_booth_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
            ) {
                Text(text = "افتتاح رسمی غرفه و شروع فروشندگی", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardSheet(
    booth: SellerBooth,
    products: List<Product>,
    userCreatedIds: Set<String>,
    onOpenAddProduct: () -> Unit,
    onDeleteProduct: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,###")
    val boothProducts = products.filter { it.id in userCreatedIds }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("seller_dashboard_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF0F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = "غرفه",
                            tint = DigikalaRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = booth.boothNameFa,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF222222)
                        )
                        Text(
                            text = "شناسه غرفه: ${booth.boothId} • ${booth.provinceFa}",
                            fontSize = 10.sp,
                            color = Color(0xFF777777)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "بستن")
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 6.dp))

            // Stats Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "کالاهای فعال",
                    value = boothProducts.size.toString(),
                    icon = Icons.Default.Inventory2,
                    tint = DigikalaRed,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "درآمد کل غرفه",
                    value = "${formatter.format(booth.totalRevenueAfn)} افغانی",
                    icon = Icons.Default.MonetizationOn,
                    tint = GreenSuccess,
                    modifier = Modifier.weight(1.3f)
                )
                StatCard(
                    title = "امتیاز فروشنده",
                    value = "۵.۰ ★",
                    icon = Icons.Default.Star,
                    tint = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Add Product Button
            Button(
                onClick = onOpenAddProduct,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .padding(horizontal = 20.dp)
                    .testTag("dashboard_add_product_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "افزودن کالا")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "افزودن کالای جدید به این غرفه", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // List of seller's products
            Text(
                text = "کالاهای درج‌شده توسط شما در الکوزی مارکت (${boothProducts.size}):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (boothProducts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "هنوز کالایی در غرفه خود ثبت نکرده‌اید!",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "با زدن دکمه «افزودن کالای جدید»، اولین محصول خود را برای خریداران سراسر افغانستان به نمایش بگذارید.",
                        fontSize = 11.sp,
                        color = Color(0xFF999999),
                        lineHeight = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    items(boothProducts) { item ->
                        SellerProductItem(
                            product = item,
                            onDelete = { onDeleteProduct(item.id) }
                        )
                        HorizontalDivider(color = Color(0xFFF2F2F2), modifier = Modifier.padding(vertical = 6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFF9F9F9),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = tint, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 9.sp, color = Color(0xFF777777))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF222222))
        }
    }
}

@Composable
private fun SellerProductItem(
    product: Product,
    onDelete: () -> Unit
) {
    val formatter = DecimalFormat("#,###")

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.titleFa,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.titleFa,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${formatter.format(product.priceAfn)} افغانی",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DigikalaDarkRed
                )
                if (product.isSpecialOffer) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = DigikalaRed) {
                        Text(
                            text = "شگفت‌انگیز",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "حذف کالا",
                tint = Color(0xFFB71C1C),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    categories: List<Category>,
    onAddProduct: (
        title: String,
        categoryId: String,
        priceAfn: Long,
        originalPriceAfn: Long,
        discountPercent: Int,
        description: String,
        imageUrl: String,
        isSpecialOffer: Boolean,
        stock: Int
    ) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var originalPriceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("10") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("mobile") }
    var isSpecialOffer by remember { mutableStateOf(false) }

    // Preset sample product photos
    val photoPresets = listOf(
        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1600121848594-d8644e57abab?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?w=600&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1599785209707-a456fc1337bb?w=600&auto=format&fit=crop&q=80"
    )
    var selectedPhotoUrl by remember { mutableStateOf(photoPresets[0]) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("add_product_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "درج کالای جدید در الکوزی مارکت",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF222222)
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "بستن")
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))

            // Title
            Text(text = "نام یا عنوان کامل کالا:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("مثال: زعفران ممتاز قائنات هرات یا گوشی آیفون 15") },
                modifier = Modifier.fillMaxWidth().testTag("add_product_title_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Selector
            Text(text = "دسته‌بندی کالا:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories.filterNot { it.id == "all" }) { cat ->
                    val isSelected = cat.id == selectedCategoryId
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) DigikalaRed else Color(0xFFF5F5F5),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedCategoryId = cat.id }
                    ) {
                        Text(
                            text = cat.nameFa,
                            color = if (isSelected) Color.White else Color(0xFF444444),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "قیمت فروش (افغانی):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter { c -> c.isDigit() } },
                        placeholder = { Text("مثلاً 4500") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed),
                        modifier = Modifier.fillMaxWidth().testTag("add_product_price_input")
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "قیمت قبل تخفیف (اختیاری):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = originalPriceText,
                        onValueChange = { originalPriceText = it.filter { c -> c.isDigit() } },
                        placeholder = { Text("مثلاً 5000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "تعداد موجودی در انبار:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it.filter { c -> c.isDigit() } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Incredible Offer toggle
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSpecialOffer) Color(0xFFFFF0F2) else Color(0xFFF9F9F9),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSpecialOffer) DigikalaRed else Color(0xFFE5E5E5)
                    ),
                    modifier = Modifier
                        .weight(1.3f)
                        .padding(top = 18.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { isSpecialOffer = !isSpecialOffer }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSpecialOffer,
                            onCheckedChange = { isSpecialOffer = it },
                            colors = CheckboxDefaults.colors(checkedColor = DigikalaRed)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "پیشنهاد شگفت‌انگیز",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "پیشنهاد شگفت‌انگیز",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DigikalaDarkRed
                                )
                            }
                            Text(text = "نمایش در کادر ویژه سرخ دیجیکالا", fontSize = 8.sp, color = Color(0xFF777777))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Product Photo Presets
            Text(text = "انتخاب تصویر شاخص کالا:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(photoPresets) { photoUrl ->
                    val isSelected = photoUrl == selectedPhotoUrl
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                2.dp,
                                if (isSelected) DigikalaRed else Color(0xFFDDDDDD),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedPhotoUrl = photoUrl }
                    ) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "عکس نمونه",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .background(DigikalaRed, RoundedCornerShape(bottomStart = 6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "انتخاب شد", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(text = "توضیحات و جزییات کالا:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("مشخصات، اصالت و شرایط گارانتی کالا را بنویسید...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DigikalaRed)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    val price = priceText.toLongOrNull() ?: 1000L
                    val original = originalPriceText.toLongOrNull() ?: price
                    val discount = if (original > price) (((original - price).toFloat() / original) * 100).toInt() else 0
                    val stock = stockText.toIntOrNull() ?: 10
                    onAddProduct(
                        title,
                        selectedCategoryId,
                        price,
                        original,
                        discount,
                        description,
                        selectedPhotoUrl,
                        isSpecialOffer,
                        stock
                    )
                },
                enabled = title.isNotBlank() && priceText.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("publish_product_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
            ) {
                Text(text = "انتشار و درج کالا در الکوزی مارکت", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
