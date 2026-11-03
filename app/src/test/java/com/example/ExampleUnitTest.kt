package com.example

import com.example.model.TelecomOperator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testOperatorDetectionForAfghanWireless() {
        val op1 = TelecomOperator.detect("0701234567")
        val op2 = TelecomOperator.detect("+93 719 876 543")
        assertEquals(TelecomOperator.AWCC, op1)
        assertEquals(TelecomOperator.AWCC, op2)
    }

    @Test
    fun testOperatorDetectionForRoshan() {
        val op1 = TelecomOperator.detect("0799887766")
        val op2 = TelecomOperator.detect("0093721234567")
        assertEquals(TelecomOperator.ROSHAN, op1)
        assertEquals(TelecomOperator.ROSHAN, op2)
    }

    @Test
    fun testOperatorDetectionForEtisalat() {
        val op1 = TelecomOperator.detect("0788334455")
        val op2 = TelecomOperator.detect("0731234567")
        assertEquals(TelecomOperator.ETISALAT, op1)
        assertEquals(TelecomOperator.ETISALAT, op2)
    }

    @Test
    fun testOperatorDetectionForMtn() {
        val op1 = TelecomOperator.detect("0777112233")
        val op2 = TelecomOperator.detect("0761234567")
        assertEquals(TelecomOperator.MTN, op1)
        assertEquals(TelecomOperator.MTN, op2)
    }

    @Test
    fun testOperatorDetectionForSalaam() {
        val op1 = TelecomOperator.detect("0744998877")
        val op2 = TelecomOperator.detect("0751234567")
        assertEquals(TelecomOperator.SALAAM, op1)
        assertEquals(TelecomOperator.SALAAM, op2)
    }

    @Test
    fun testAfghanNumberValidationRegex() {
        assertTrue(TelecomOperator.isValidAfghanNumber("0700123456"))
        assertTrue(TelecomOperator.isValidAfghanNumber("+93799887766"))
        assertTrue(TelecomOperator.isValidAfghanNumber("0093788334455"))
        assertTrue(TelecomOperator.isValidAfghanNumber("777112233"))
        assertTrue(TelecomOperator.isValidAfghanNumber("0744998877"))

        // Invalid numbers
        assertFalse(TelecomOperator.isValidAfghanNumber("0600123456")) // prefix 6 does not exist in AF
        assertFalse(TelecomOperator.isValidAfghanNumber("070012345"))  // too short (8 digits)
        assertFalse(TelecomOperator.isValidAfghanNumber("070012345678")) // too long
        assertFalse(TelecomOperator.isValidAfghanNumber("abcdefghi"))
    }

    @Test
    fun testFormatToStandard() {
        val formatted = TelecomOperator.formatToStandard("0700123456")
        assertEquals("+93 700 123 456", formatted)
    }
}
