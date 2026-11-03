package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Afghan Telecom Operators coverage:
 * 1. Afghan Wireless (AWCC / افغان بیسیم): 070, 071
 * 2. Roshan (روشن): 079, 072
 * 3. Etisalat (اتصالات): 078, 073
 * 4. MTN (ام‌تی‌ان): 077, 076
 * 5. Salaam (سلام): 074, 075
 */
enum class TelecomOperator(
    val id: String,
    val faName: String,
    val enName: String,
    val prefixes: List<String>,
    val primaryColor: Color,
    val accentColor: Color,
    val tagColor: Color,
    val description: String
) {
    AWCC(
        id = "awcc",
        faName = "افغان بیسیم (AWCC)",
        enName = "Afghan Wireless",
        prefixes = listOf("070", "071", "70", "71"),
        primaryColor = Color(0xFFF25C05),
        accentColor = Color(0xFFFFF3E0),
        tagColor = Color(0xFFE65100),
        description = "اولین شبکه مخابراتی بیسیم در افغانستان"
    ),
    ROSHAN(
        id = "roshan",
        faName = "روشن (Roshan)",
        enName = "Roshan Telecom",
        prefixes = listOf("079", "072", "79", "72"),
        primaryColor = Color(0xFFC6102F),
        accentColor = Color(0xFFFFEBEE),
        tagColor = Color(0xFFB71C1C),
        description = "شبکه پیشرو با پوشش سراسری و اینترنت پرسرعت"
    ),
    ETISALAT(
        id = "etisalat",
        faName = "اتصالات (Etisalat)",
        enName = "Etisalat Afghanistan",
        prefixes = listOf("078", "073", "78", "73"),
        primaryColor = Color(0xFF679E1D),
        accentColor = Color(0xFFF1F8E9),
        tagColor = Color(0xFF33691E),
        description = "اینترنت پرسرعت 4G و خدمات نوین دیجیتال"
    ),
    MTN(
        id = "mtn",
        faName = "ام‌تی‌ان (MTN)",
        enName = "MTN Afghanistan",
        prefixes = listOf("077", "076", "77", "76"),
        primaryColor = Color(0xFFFFC000),
        accentColor = Color(0xFFFFFDE7),
        tagColor = Color(0xFFF57F17),
        description = "پوشش گسترده در تمامی ولایات افغانستان"
    ),
    SALAAM(
        id = "salaam",
        faName = "سلام (Salaam)",
        enName = "Salaam Telecom",
        prefixes = listOf("074", "075", "74", "75"),
        primaryColor = Color(0xFF009688),
        accentColor = Color(0xFFE0F2F1),
        tagColor = Color(0xFF004D40),
        description = "شبکه مخابراتی ملی با ارزان‌ترین تعرفه‌های تماس و دیتا"
    );

    companion object {
        /**
         * Detect operator from raw phone input string.
         * Strips +93, 0093, or leading 0 to get the 9-digit national number starting with 7.
         */
        fun detect(rawInput: String): TelecomOperator? {
            val cleaned = cleanNumber(rawInput)
            if (cleaned.length < 2) return null
            val prefix2 = cleaned.take(2)
            return entries.firstOrNull { op ->
                op.prefixes.any { p ->
                    val cleanP = p.removePrefix("0")
                    cleanP == prefix2
                }
            }
        }

        /**
         * Cleans user phone string:
         * Removes whitespace, hyphens, and leading +93 / 0093 / 0.
         * Returns 9 digits starting with '7' if valid.
         */
        fun cleanNumber(raw: String): String {
            var digits = raw.filter { it.isDigit() }
            if (digits.startsWith("0093")) {
                digits = digits.removePrefix("0093")
            } else if (digits.startsWith("93") && digits.length > 9) {
                digits = digits.removePrefix("93")
            }
            if (digits.startsWith("0") && digits.length > 9) {
                digits = digits.removePrefix("0")
            }
            return digits
        }

        /**
         * Checks if phone matches standard Afghanistan regex: ^(?:\+93|0093|0)?(7[0-9]{8})$
         */
        fun isValidAfghanNumber(raw: String): Boolean {
            val afghanRegex = Regex("""^(?:\+93|0093|0)?(7[0-9]{8})$""")
            val cleaned = raw.trim().replace(" ", "").replace("-", "")
            return afghanRegex.matches(cleaned)
        }

        /**
         * Formats phone into standard Afghan display format: +93 7XX XXX XXX
         */
        fun formatToStandard(raw: String): String {
            val cleaned = cleanNumber(raw)
            return if (cleaned.length == 9) {
                "+93 ${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6)}"
            } else {
                "+93 $cleaned"
            }
        }
    }
}
