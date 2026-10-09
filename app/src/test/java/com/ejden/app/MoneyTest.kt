package com.ejden.app

import com.ejden.app.core.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {
    private fun normalized(text: String) = text.replace('\u202f', ' ').replace('\u00a0', ' ')

    @Test fun formatGroupsThousands() {
        assertEquals("1 500 FCFA", normalized(Money.format(1_500)))
        assertEquals("0 FCFA", normalized(Money.format(0)))
    }

    @Test fun parseAcceptsSpacesAndPlainDigits() {
        assertEquals(1_500L, Money.parse("1 500"))
        assertEquals(1_500L, Money.parse("1500"))
    }

    @Test fun parseRejectsInvalidInput() {
        assertNull(Money.parse(""))
        assertNull(Money.parse("-5"))
        assertNull(Money.parse("12,5"))
        assertNull(Money.parse("abc"))
    }

    @Test fun quantityKeepsIntegersClean() {
        assertEquals("3", Money.quantity(3.0))
    }
}
