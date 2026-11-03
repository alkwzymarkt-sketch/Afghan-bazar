package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CartItem
import com.example.ui.theme.DigikalaDarkRed
import com.example.ui.theme.DigikalaRed
import com.example.ui.theme.GreenSuccess
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartItems: List<CartItem>,
    subtotalAfn: Long,
    discountAfn: Long,
    shippingFeeAfn: Long,
    finalPayableAfn: Long,
    onDismiss: () -> Unit,
    onUpdateQuantity: (productId: String, delta: Int) -> Unit,
    onRemoveItem: (productId: String) -> Unit,
    onClearCart: () -> Unit,
    onProceedCheckout: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,###")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("cart_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "سبد خرید",
                        tint = DigikalaRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "سبد خرید من (${cartItems.sumOf { it.quantity }} کالا)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cartItems.isNotEmpty()) {
                        TextButton(onClick = onClearCart) {
                            Text(
                                text = "حذف همه",
                                color = Color(0xFF888888),
                                fontSize = 12.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "بستن")
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))

            if (cartItems.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "سبد خرید خالی است",
                            tint = Color(0xFFCCCCCC),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "سبد خرید شما خالی است!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "می‌توانید برای مشاهده محصولات شگفت‌انگیز به صفحه اصلی برگردید.",
                        fontSize = 12.sp,
                        color = Color(0xFF888888),
                        lineHeight = 18.sp
                    )
                }
            } else {
                // Cart Items List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrement = { onUpdateQuantity(item.product.id, 1) },
                            onDecrement = { onUpdateQuantity(item.product.id, -1) },
                            onRemove = { onRemoveItem(item.product.id) }
                        )
                        HorizontalDivider(
                            color = Color(0xFFF2F2F2),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                // Summary Calculation Card
                Surface(
                    color = Color(0xFFFAFAFA),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "جمع اقلام", fontSize = 12.sp, color = Color(0xFF777777))
                            Text(
                                text = "${formatter.format(subtotalAfn)} افغانی",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF333333)
                            )
                        }

                        if (discountAfn > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "سود شما از خرید", fontSize = 12.sp, color = DigikalaRed)
                                Text(
                                    text = "${formatter.format(discountAfn)} افغانی-",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DigikalaRed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "هزینه ارسال ولایتی", fontSize = 12.sp, color = Color(0xFF777777))
                            Text(
                                text = if (shippingFeeAfn == 0L) "رایگان (خریدهای بالای ۳,۰۰۰ افغانی)" else "${formatter.format(shippingFeeAfn)} افغانی",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (shippingFeeAfn == 0L) GreenSuccess else Color(0xFF333333)
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFE5E5E5),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "مبلغ قابل پرداخت:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF222222)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatter.format(finalPayableAfn),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DigikalaDarkRed
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "افغانی",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DigikalaDarkRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Checkout Button
                Button(
                    onClick = onProceedCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .testTag("checkout_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
                ) {
                    Text(
                        text = "ادامه و تکمیل سفارش در افغانستان",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    val formatter = DecimalFormat("#,###")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF7F7F7))
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = item.product.titleFa,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(70.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.titleFa,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "غرفه: ${item.product.vendorNameFa}",
                fontSize = 10.sp,
                color = Color(0xFF888888)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${formatter.format(item.product.priceAfn)} افغانی",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigikalaDarkRed
                )
            }
        }

        // Stepper (+ / - / delete)
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF2F2F2),
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "افزایش", modifier = Modifier.size(16.dp))
                }

                Text(
                    text = item.quantity.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = {
                        if (item.quantity > 1) onDecrement() else onRemove()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    if (item.quantity > 1) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "کاهش", modifier = Modifier.size(16.dp))
                    } else {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "حذف", tint = DigikalaRed, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
