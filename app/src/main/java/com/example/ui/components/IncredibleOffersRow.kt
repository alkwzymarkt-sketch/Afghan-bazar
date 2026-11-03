package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Product
import com.example.ui.theme.DigikalaDarkRed
import com.example.ui.theme.DigikalaRed
import com.example.ui.theme.GoldAccent
import java.text.DecimalFormat

@Composable
fun IncredibleOffersRow(
    products: List<Product>,
    remainingSeconds: Long,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = (remainingSeconds / 3600).toString().padStart(2, '0')
    val minutes = ((remainingSeconds % 3600) / 60).toString().padStart(2, '0')
    val seconds = (remainingSeconds % 60).toString().padStart(2, '0')

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(DigikalaRed)
            .padding(vertical = 14.dp)
            .testTag("incredible_offers_section")
    ) {
        Column {
            // Section Header: Title & Countdown Timer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title with fire icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "پیشنهاد شگفت‌انگیز",
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "پیشنهاد شگفت‌انگیز",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = "تخفیف ویژه",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Countdown Timer Boxes (HH : MM : SS)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "زمان باقیمانده",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TimerBlock(seconds)
                    Text(" : ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    TimerBlock(minutes)
                    Text(" : ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    TimerBlock(hours)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Carousel of Offer Cards
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    IncredibleProductCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        onAddToCart = { onAddToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerBlock(text: String) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = DigikalaDarkRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun IncredibleProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,###")

    Surface(
        modifier = modifier
            .width(175.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("incredible_product_${product.id}"),
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Product Image with Discount Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF7F7F7))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.titleFa,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(125.dp)
                )

                // Discount Pill
                Surface(
                    shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
                    color = DigikalaRed,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "${product.discountPercent}٪ تخفیف",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Title
            Text(
                text = product.titleFa,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Vendor Name
            Text(
                text = "غرفه: ${product.vendorNameFa}",
                fontSize = 10.sp,
                color = Color(0xFF757575),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Original Price (Strikethrough)
            if (product.originalPriceAfn > product.priceAfn) {
                Text(
                    text = "${formatter.format(product.originalPriceAfn)} افغانی",
                    fontSize = 10.sp,
                    color = Color(0xFFAAAAAA),
                    textDecoration = TextDecoration.LineThrough
                )
            }

            // Discounted Final Price in AFN
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formatter.format(product.priceAfn),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = DigikalaDarkRed
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "افغانی",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigikalaDarkRed
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stock progress indicator
            LinearProgressIndicator(
                progress = { (product.stock / 20f).coerceIn(0.15f, 0.9f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = DigikalaRed,
                trackColor = Color(0xFFFFCDD2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Add to Cart Button
            ElevatedButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("add_to_cart_${product.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFFFF0F2),
                    contentColor = DigikalaRed
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "افزودن",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "افزودن به سبد",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
