package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
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
import com.example.model.Order
import com.example.model.SampleMarketData
import com.example.ui.theme.DigikalaDarkRed
import com.example.ui.theme.DigikalaRed
import com.example.ui.theme.GreenSuccess
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDialog(
    selectedProvince: String,
    onProvinceSelected: (String) -> Unit,
    customerAddress: String,
    onAddressChanged: (String) -> Unit,
    paymentMethod: String,
    onPaymentMethodChanged: (String) -> Unit,
    payableAfn: Long,
    onConfirmOrder: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    var expandedProvinceDropdown by remember { mutableStateOf(false) }
    val formatter = DecimalFormat("#,###")

    val paymentOptions = listOf(
        "پرداخت نقدی هنگام تحویل کالا (درب منزل در کابل و مراکز ولایات)",
        "حواله صرافی سرای شهزاده کابل به نام الکوزی مارکت",
        "انتقال مستقیم بانکی (عزیزی بانک یا کابل بانک)"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier.testTag("checkout_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 24.dp)
        ) {
            // Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ثبت آدرس و نهایی‌سازی سفارش در افغانستان",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "انصراف")
                }
            }

            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))

            // Province Selector
            Text(
                text = "ولایت مقصد:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = expandedProvinceDropdown,
                onExpandedChange = { expandedProvinceDropdown = !expandedProvinceDropdown }
            ) {
                OutlinedTextField(
                    value = selectedProvince,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvinceDropdown) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "ولایت",
                            tint = DigikalaRed
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("province_dropdown"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DigikalaRed,
                        unfocusedBorderColor = Color(0xFFDDDDDD)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expandedProvinceDropdown,
                    onDismissRequest = { expandedProvinceDropdown = false }
                ) {
                    SampleMarketData.afghanProvinces.forEach { province ->
                        DropdownMenuItem(
                            text = { Text(province, fontSize = 13.sp) },
                            onClick = {
                                onProvinceSelected(province)
                                expandedProvinceDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detailed Address
            Text(
                text = "آدرس دقیق تحویل گیرنده در ولایت:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = customerAddress,
                onValueChange = onAddressChanged,
                placeholder = {
                    Text(
                        text = "مثال: کابل، ناحیه ۳، سرک ۴ کارته سه، کوچه دوم، پلاک ۲۵",
                        fontSize = 12.sp,
                        color = Color(0xFFAAAAAA)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("address_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DigikalaRed,
                    unfocusedBorderColor = Color(0xFFDDDDDD)
                ),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Options
            Text(
                text = "روش پرداخت معتبر در افغانستان:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))

            paymentOptions.forEach { method ->
                val isSelected = method == paymentMethod
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color(0xFFFFF0F2) else Color(0xFFF9F9F9),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            1.dp,
                            if (isSelected) DigikalaRed else Color(0xFFE5E5E5),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onPaymentMethodChanged(method) }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onPaymentMethodChanged(method) },
                            colors = RadioButtonDefaults.colors(selectedColor = DigikalaRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = method,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) DigikalaDarkRed else Color(0xFF333333)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total amount
            Surface(
                color = Color(0xFFF6F6F6),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مجموع پرداختی نهایی:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${formatter.format(payableAfn)} افغانی",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = DigikalaDarkRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onConfirmOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_order_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
            ) {
                Icon(
                    imageVector = Icons.Default.Payments,
                    contentDescription = "ثبت سفارش",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تایید و ثبت نهایی سفارش",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OrderSuccessDialog(
    order: Order,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,###")

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("order_success_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "موفقیت",
                        tint = GreenSuccess,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "سفارش شما با موفقیت ثبت شد!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1B5E20)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "کد رهگیری سفارش: ${order.id}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DigikalaRed
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color(0xFFFAFAFA),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "مبلغ: ${formatter.format(order.totalAfn)} افغانی",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "مقصد: ${order.provinceFa}",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "وضعیت: ${order.statusFa}",
                            fontSize = 11.sp,
                            color = DigikalaDarkRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DigikalaRed)
                ) {
                    Text(text = "بازگشت به بازار الکوزی", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
