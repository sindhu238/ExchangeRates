package com.example.exchangeRates.api

import com.example.exchangeRates.model.ConversionRates
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Observable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkingService {
    @GET("latest?")
    fun getCurrencyRatesFor(@Query("base")currency: String): Observable<ConversionRates>
}

object RetrofitClient {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.nonstrict.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://revolut.duckdns.org")
            .build()
    }
}


