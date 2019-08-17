package com.example.exchangeRates.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ConversionRates(
    val base: String,
    val rates: Map<String, Double>
)

data class CurrencyRate(val currency: Currency, val rate: Double)