package com.example.exchangeRates.customViews.currencyEditText

import io.reactivex.subjects.PublishSubject


class CurrencyEditTextViewModel {
    // Inputs
    val textChanges = PublishSubject.create<String>()

    // Outputs
    val amendedTextStream = PublishSubject.create<Double>()

    init {
        textChanges.map { it.toDoubleOrNull() ?: -1.0 }.filter { it != -1.0 }.subscribe(amendedTextStream)
    }
}