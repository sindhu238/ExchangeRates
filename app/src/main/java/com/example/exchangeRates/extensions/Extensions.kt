package com.example.exchangeRates.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import com.jwang123.flagkit.FlagKit
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun Map<String, Double>.reversed() = toList().reversed().toMap()

fun Double.roundToDecimals(decimalPlaces: Int): Double {
    val factor = 10.0.pow(decimalPlaces.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Editable.toDouble(): Double? = toString().toDoubleOrNull()

fun Context.getDrawableFor(currency: Currency): Drawable? =
    Locale.getAvailableLocales().firstOrNull {
        NumberFormat.getCurrencyInstance(it).currency == currency
    }?.let {
        try {
            FlagKit.drawableWithFlag(this, it.country.toLowerCase())
        } catch (exception: Exception) {
            return@let null
        }
    }