package com.ejden.app

import com.ejden.app.core.CartLine
import com.ejden.app.core.SaleMath
import org.junit.Assert.assertEquals
import org.junit.Test

class SaleMathTest {
    private val lines = listOf(
        CartLine("a", "Riz", 2.0, 1_000, 700),
        CartLine("b", "Huile", 1.5, 2_000, 1_500)
    )

    @Test fun totalsWithChange() {
        val t = SaleMath.totals(lines, discount = 500, paid = 6_000)

        assertEquals(5_000, t.subtotal)
        assertEquals(500, t.discount)
        assertEquals(4_500, t.total)
        assertEquals(1_500, t.change)
        assertEquals(0, t.remaining)
    }

    @Test fun partialPaymentLeavesRemaining() {
        val t = SaleMath.totals(lines, discount = 0, paid = 3_000)

        assertEquals(2_000, t.remaining)
        assertEquals(0, t.change)
    }

    @Test fun discountNeverExceedsSubtotal() {
        val t = SaleMath.totals(lines, discount = 99_999, paid = 0)

        assertEquals(5_000, t.discount)
        assertEquals(0, t.total)
    }
}
