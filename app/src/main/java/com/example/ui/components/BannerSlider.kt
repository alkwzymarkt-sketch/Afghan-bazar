package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DigikalaRed
import kotlinx.coroutines.delay

data class BannerSlide(
    val drawableRes: Int?,
    val title: String,
    val subtitle: String,
    val tag: String,
    val gradientColors: List<Color>
)

@Composable
fun BannerSlider(
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {}
) {
    val slides = remember {
        listOf(
            BannerSlide(
                drawableRes = R.drawable.banner_promo_main_1793606052216,
                title = "جشنواره بزرگ حراج تابستانه افغانستان",
                subtitle = "تا ۷۰٪ تخفیف روی تمامی اجناس کالای دیجیتال و پوشاک",
                tag = "پیشنهاد شگفت‌انگیز",
                gradientColors = listOf(Color(0xFFD32F2F), Color(0xFFFF8F00))
            ),
            BannerSlide(
                drawableRes = R.drawable.banner_promo_tech_1793606067785,
                title = "فروش ویژه گوشی‌های سامسونگ و آیفون",
                subtitle = "با گارانتی اصالت کالا و تحویل ۲۴ ساعته در شهر کابل",
                tag = "دیجیتال مارکت",
                gradientColors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
            ),
            BannerSlide(
                drawableRes = null,
                title = "طلای سرخ افغانستان: زعفران درجه یک هرات",
                subtitle = "ارسال مستقیم از مزارع هرات به تمام ولایات با تست آزمایشگاهی",
                tag = "صنایع دستی و صادراتی",
                gradientColors = listOf(Color(0xFF880E4F), Color(0xFFC2185B))
            )
        )
    }

    var currentSlideIndex by remember { mutableIntStateOf(0) }

    // Auto rotate banners every 4 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000L)
            currentSlideIndex = (currentSlideIndex + 1) % slides.size
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(18.dp))
                .clickable { onBannerClick(slides[currentSlideIndex].title) }
        ) {
            AnimatedContent(
                targetState = currentSlideIndex,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "banner_transition"
            ) { targetIndex ->
                val slide = slides[targetIndex]
                if (slide.drawableRes != null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = slide.drawableRes),
                            contentDescription = slide.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Subtle dark gradient overlay on bottom for text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                                        startY = 100f
                                    )
                                )
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(slide.gradientColors))
                            .padding(20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = slide.tag,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = slide.title,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = slide.subtitle,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Dot indicators at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                slides.indices.forEach { index ->
                    val isSelected = index == currentSlideIndex
                    val dotWidth = if (isSelected) 18.dp else 6.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .width(dotWidth)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                if (isSelected) DigikalaRed else Color.White.copy(alpha = 0.6f)
                            )
                            .clickable { currentSlideIndex = index }
                    )
                }
            }
        }
    }
}
