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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AfghanProvinces
import com.example.model.ProductSortOption
import com.example.ui.theme.AlkoziDarkGreen
import com.example.ui.theme.AlkoziDeepGreen
import com.example.ui.theme.AlkoziGold
import com.example.ui.theme.AlkoziLightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortDialog(
    currentSort: ProductSortOption,
    currentProvince: String,
    onlyInStock: Boolean,
    onlySpecialOffers: Boolean,
    onApply: (ProductSortOption, String, Boolean, Boolean) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    var selectedSort by remember { mutableStateOf(currentSort) }
    var selectedProvince by remember { mutableStateOf(currentProvince) }
    var inStockOnly by remember { mutableStateOf(onlyInStock) }
    var specialOffersOnly by remember { mutableStateOf(onlySpecialOffers) }
    var provinceSearch by remember { mutableStateOf("") }

    val filteredProvinces = remember(provinceSearch) {
        if (provinceSearch.isBlank()) listOf("همه ۳۴ ولایت") + AfghanProvinces.all34
        else listOf("همه ۳۴ ولایت") + AfghanProvinces.all34.filter { it.contains(provinceSearch.trim(), ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier.testTag("filter_sort_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AlkoziLightGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "فیلترها",
                            tint = AlkoziDarkGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "فیلتر و مرتب‌سازی کالاها",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E1E1E)
                    )
                }

                OutlinedButton(
                    onClick = {
                        selectedSort = ProductSortOption.RECOMMENDED
                        selectedProvince = "همه ۳۴ ولایت"
                        inStockOnly = false
                        specialOffersOnly = false
                        onReset()
                    },
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "بازنشانی",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("بازنشانی", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort Section
            Text(
                text = "مرتب‌سازی بر اساس:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AlkoziDarkGreen
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ProductSortOption.values()) { opt ->
                    FilterChip(
                        selected = selectedSort == opt,
                        onClick = { selectedSort = opt },
                        label = { Text(opt.titleFa, fontSize = 11.sp) },
                        leadingIcon = if (selectedSort == opt) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AlkoziDarkGreen,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Province Filter Section
            Text(
                text = "انتخاب ولایت مبدا / مقصد کالا (۳۴ ولایت افغانستان):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AlkoziDarkGreen
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredProvinces) { prov ->
                    FilterChip(
                        selected = selectedProvince == prov,
                        onClick = { selectedProvince = prov },
                        label = { Text(prov, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AlkoziDeepGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggles
            Surface(
                color = Color(0xFFF9F9F9),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("فقط کالاهای موجود در انبار", fontSize = 12.sp, color = Color(0xFF333333))
                        Switch(
                            checked = inStockOnly,
                            onCheckedChange = { inStockOnly = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = AlkoziDarkGreen, checkedTrackColor = AlkoziLightGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("فقط کالاهای دارای تخفیف ویژه", fontSize = 12.sp, color = Color(0xFF333333))
                        Switch(
                            checked = specialOffersOnly,
                            onCheckedChange = { specialOffersOnly = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = AlkoziDarkGreen, checkedTrackColor = AlkoziLightGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Apply Button
            Button(
                onClick = {
                    onApply(selectedSort, selectedProvince, inStockOnly, specialOffersOnly)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("apply_filter_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = AlkoziDarkGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "اعمال فیلترها و نمایش کالاها",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
