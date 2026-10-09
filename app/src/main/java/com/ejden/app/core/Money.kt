package com.ejden.app.core

import java.util.Locale

/**
 * Les montants sont des entiers (FCFA, sans centimes) : jamais de Double pour l'argent.
 */
object Money {
    private val locale: Locale = Locale.FRANCE

    fun format(value: Long): String = "${group(value)} FCFA"

    /** Nombre groupé par milliers, sans l'unité (tickets, tableaux). */
    fun group(value: Long): String = String.format(locale, "%,d", value)

    /** Lit une saisie utilisateur (« 1 500 », « 1500 »). Null si invalide ou négative. */
    fun parse(text: String): Long? {
        val digits = text.filter { !it.isWhitespace() && it != '\u202f' && it != '\u00a0' }

        if (digits.isEmpty() || digits.length > 12 || !digits.all { it.isDigit() }) return null

        return digits.toLongOrNull()
    }

    /** Quantité : entier si possible, sinon jusqu'à 2 décimales. */
    fun quantity(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString()
        else String.format(locale, "%.2f", value).trimEnd('0').trimEnd(',')
}
