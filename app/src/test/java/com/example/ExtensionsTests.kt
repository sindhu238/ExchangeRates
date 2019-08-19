package com.example

import com.example.exchangeRates.extensions.roundToDecimals
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ExtensionsTests {

    @Test
    fun `test rounding to decimals`() {
        assertEquals(10.34, 10.344343.roundToDecimals(2))
    }
}