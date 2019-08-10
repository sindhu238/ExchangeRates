package com.example.exchangeRates.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversionRates(
    val base: String,
    val rates: Map<String, Double>
)