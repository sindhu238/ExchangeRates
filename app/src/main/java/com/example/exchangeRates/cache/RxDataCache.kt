package com.example.exchangeRates.cache

import com.example.exchangeRates.MyApplication
import java.util.*

interface RxDataCacheType {

    /**
     * Returns country as a string for a given currency
     * @return Country
     */
    fun getCountryForCurrency(currency: Currency): String?
}

object RxDataCache: RxDataCacheType {
    override fun getCountryForCurrency(currency: Currency) =
        MyApplication.localesWithCurrencies.filter {
            it.value == currency
        }.keys.firstOrNull()
}

