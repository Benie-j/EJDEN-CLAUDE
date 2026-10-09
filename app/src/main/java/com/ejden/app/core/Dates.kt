package com.ejden.app.core

import java.time.LocalDate
import java.time.ZoneId

object Dates {
    /** Début et fin (inclus) du jour local, en millisecondes. */
    fun dayRange(day: LocalDate = LocalDate.now(), zone: ZoneId = ZoneId.systemDefault()): LongRange {
        val start = day.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = day.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        return start..end
    }
}
