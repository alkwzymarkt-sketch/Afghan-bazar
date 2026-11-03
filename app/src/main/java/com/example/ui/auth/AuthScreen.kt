package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TelecomOperator
import com.example.ui.theme.DigikalaDarkRed
import com.example.ui.theme.DigikalaRed
import com.example.ui.theme.GreenSuccess
import com.example.viewmodel.AuthStep
import com.example.viewmodel.MarketUiState

@Composable
fun AuthScreen(
    uiState: MarketUiState,
    onPhoneChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onQuickFillOperator: (TelecomOperator) -> Unit,
    onProceedToOtp: () -> Unit,
    onOtpChanged: (String) -> Unit,
    onAutoFillTestOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onResendSms: () -> Unit,
    onRequestVoiceCall: () -> Unit,
    onBackToPhone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Branding Header
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(DigikalaRed, DigikalaDarkRed)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "الکوزی",
                    color = DigikalaRed,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "مارکت",
                    color = Color(0xFF222222),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Text(
                text = "بزرگ‌ترین بازار آنلاین افغانستان (AFN)",
                fontSize = 12.sp,
                color = Color(0xFF777777)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main Card Container
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(22.dp),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.authStep == AuthStep.PHONE_INPUT) {
                        RegistrationStep(
                            phoneInput = uiState.phoneInput,
                            nameInput = uiState.nameInput,
                            detectedOperator = uiState.detectedOperator,
                            isPhoneValid = uiState.isPhoneValid,
                            errorMessage = uiState.phoneErrorMessage,
                            onPhoneChanged = onPhoneChanged,
                            onNameChanged = onNameChanged,
                            onQuickFill = onQuickFillOperator,
                            onSubmit = onProceedToOtp
                        )
                    } else {
                        OtpVerificationStep(
                            phone = TelecomOperator.formatToStandard(uiState.phoneInput),
                            operator = uiState.detectedOperator ?: TelecomOperator.AWCC,
                            otpCode = uiState.otpCode,
                            remainingSeconds = uiState.otpRemainingSeconds,
                            isVoiceCall = uiState.isVoiceCallRequested,
                            onOtpChanged = onOtpChanged,
                            onAutoFillTestOtp = onAutoFillTestOtp,
                            onVerify = onVerifyOtp,
                            onResendSms = onResendSms,
                            onRequestVoiceCall = onRequestVoiceCall,
                            onChangeNumber = onBackToPhone
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Trust Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "امنیت",
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ثبت‌نام امن با پوشش سراسری ۵ اپراتور مخابراتی افغانستان",
                    fontSize = 11.sp,
                    color = Color(0xFF888888)
                )
            }
        }
    }
}

@Composable
private fun RegistrationStep(
    phoneInput: String,
    nameInput: String,
    detectedOperator: TelecomOperator?,
    isPhoneValid: Boolean,
    errorMessage: String?,
    onPhoneChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onQuickFill: (TelecomOperator) -> Unit,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "مرحله ۱: ثبت‌نام با شماره افغانستان",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF222222)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "برای دسترسی و ورود به بازار آنلاین، ابتدا شماره موبایل افغانستان خود را وارد نمایید.",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name input (Optional/convenient)
        Text(
            text = "نام و نام‌خانوادگی (اختیاری):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF444444)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChanged,
            placeholder = { Text("مثال: احمد الکوزی", fontSize = 13.sp, color = Color(0xFFAAAAAA)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "نام خریدار",
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(18.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DigikalaRed,
                unfocusedBorderColor = Color(0xFFDDDDDD)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("reg_name_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone Input
        Text(
            text = "شماره تلفن همراه (با پیش‌شماره 07):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF444444)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = phoneInput,
            onValueChange = onPhoneChanged,
            placeholder = { Text("07X XXX XXXX یا +93 7...", fontSize = 13.sp, color = Color(0xFFAAAAAA)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = "تلفن",
                    tint = if (detectedOperator != null) detectedOperator.primaryColor else Color(0xFF888888),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (isPhoneValid) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "شماره معتبر است",
                        tint = GreenSuccess
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = detectedOperator?.primaryColor ?: DigikalaRed,
                unfocusedBorderColor = if (detectedOperator != null) detectedOperator.primaryColor.copy(alpha = 0.5f) else Color(0xFFCCCCCC)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("reg_phone_input")
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = errorMessage,
                color = DigikalaDarkRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Smart Detected Operator Card
        AnimatedVisibility(
            visible = detectedOperator != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            detectedOperator?.let { op ->
                Surface(
                    color = op.accentColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, op.primaryColor, RoundedCornerShape(12.dp))
                        .testTag("detected_operator_card")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(op.primaryColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SimCard,
                                contentDescription = op.faName,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = op.faName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = op.tagColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = op.primaryColor
                                ) {
                                    Text(
                                        text = "تایید شد",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = op.description,
                                fontSize = 10.sp,
                                color = Color(0xFF444444)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5 Afghan Operators Quick Test Chips
        Text(
            text = "تست سریع ۵ اپراتور مخابراتی افغانستان:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OperatorChip(TelecomOperator.AWCC, "070/071", onQuickFill, Modifier.weight(1f))
            OperatorChip(TelecomOperator.ROSHAN, "079/072", onQuickFill, Modifier.weight(1f))
            OperatorChip(TelecomOperator.ETISALAT, "078/073", onQuickFill, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OperatorChip(TelecomOperator.MTN, "077/076", onQuickFill, Modifier.weight(1f))
            OperatorChip(TelecomOperator.SALAAM, "074/075", onQuickFill, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = onSubmit,
            enabled = isPhoneValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("reg_submit_phone_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DigikalaRed,
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            Text(
                text = "دریافت کد تایید یکبار مصرف (SMS)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun OperatorChip(
    op: TelecomOperator,
    prefixes: String,
    onClick: (TelecomOperator) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = op.accentColor,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(op) }
            .border(1.dp, op.primaryColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = op.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = op.tagColor
            )
            Text(
                text = prefixes,
                fontSize = 9.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun OtpVerificationStep(
    phone: String,
    operator: TelecomOperator,
    otpCode: String,
    remainingSeconds: Int,
    isVoiceCall: Boolean,
    onOtpChanged: (String) -> Unit,
    onAutoFillTestOtp: () -> Unit,
    onVerify: () -> Unit,
    onResendSms: () -> Unit,
    onRequestVoiceCall: () -> Unit,
    onChangeNumber: () -> Unit
) {
    val minutes = (remainingSeconds / 60).toString().padStart(2, '0')
    val seconds = (remainingSeconds % 60).toString().padStart(2, '0')

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onChangeNumber) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "بازگشت"
                )
            }
            Text(
                text = "مرحله ۲: تایید کد یکبار مصرف",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF222222)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(operator.accentColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = "کد تایید",
                tint = operator.primaryColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isVoiceCall) "تماس صوتی برای خواندن کد ۵ رقمی فعال شد" else "کد ۵ رقمی تایید پیامک شد",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "کد تایید به شماره $phone ($operator) ارسال گردید.",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        TextButton(onClick = onChangeNumber) {
            Text(
                text = "ویرایش شماره تلفن",
                color = DigikalaRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5-Digit Display Boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (0..4).forEach { index ->
                val char = otpCode.getOrNull(index)?.toString() ?: ""
                val isCurrent = index == otpCode.length
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (char.isNotEmpty()) Color(0xFFFFF0F2) else Color(0xFFF7F7F7))
                        .border(
                            1.5.dp,
                            if (isCurrent) DigikalaRed else if (char.isNotEmpty()) DigikalaDarkRed else Color(0xFFDDDDDD),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DigikalaDarkRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = otpCode,
            onValueChange = onOtpChanged,
            placeholder = { Text("کد ۵ رقمی را اینجا وارد کنید", textAlign = TextAlign.Center, fontSize = 13.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .testTag("reg_otp_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick auto-fill test button
        OutlinedButton(
            onClick = onAutoFillTestOtp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("reg_autofill_otp_btn")
        ) {
            Text(
                text = "⚡ درج خودکار کد تستی (12345)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DigikalaRed
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 120s Countdown Timer
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (remainingSeconds > 0) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "تایمر",
                    tint = Color(0xFF777777),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ارسال مجدد کد پس از $minutes:$seconds",
                    fontSize = 12.sp,
                    color = Color(0xFF777777)
                )
            } else {
                TextButton(onClick = onResendSms) {
                    Text(
                        text = "ارسال مجدد کد پیامکی",
                        color = DigikalaRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Voice Call Option
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF9F9F9),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onRequestVoiceCall() }
                .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(10.dp))
                .testTag("reg_voice_call_btn")
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "تماس صوتی",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "دریافت کد از طریق تماس تلفنی",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = "ویژه مناطقی از ولایات با پوشش پیامکی ضعیف",
                        fontSize = 10.sp,
                        color = Color(0xFF888888)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Verify & Enter Button
        Button(
            onClick = onVerify,
            enabled = otpCode.length == 5,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("reg_verify_and_enter_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DigikalaRed,
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            Text(
                text = "تایید کد و ورود به فروشگاه",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
