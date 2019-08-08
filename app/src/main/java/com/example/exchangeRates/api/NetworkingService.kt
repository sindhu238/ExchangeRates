package com.example.exchangeRates.api

import com.example.exchangeRates.model.ConversionRates
import io.reactivex.Observable

interface ServerAPI {
    fun getConversionRatesDetails(): Observable<ConversionRates>
}

object ServerAPIImpl : ServerAPI {
    override fun getConversionRatesDetails(): Observable<ConversionRates> =
            RetrofitClient.retrofit.create(NetworkingService::class.java)
                .getCurrencyRatesFor("EUR")

}