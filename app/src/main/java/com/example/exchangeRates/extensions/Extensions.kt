package com.example.exchangeRates.extensions

import android.text.Editable
import kotlin.math.pow
import kotlin.math.roundToInt

fun Map<String, Double>.reversed() = toList().reversed().toMap()

fun Double.roundToDecimals(decimalPlaces: Int): Double {
    val factor = 10.0.pow(decimalPlaces.toDouble())
    return (this * factor).roundToInt() / factor
}

fun Editable.toDouble(): Double? = toString().toDoubleOrNull()