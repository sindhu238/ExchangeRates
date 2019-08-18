package com.example.exchangeRates.activities.exchangeRates

import androidx.lifecycle.ViewModel
import com.example.exchangeRates.api.ServerAPI
import com.example.exchangeRates.extensions.reversed
import com.example.exchangeRates.extensions.roundToDecimals
import com.example.exchangeRates.model.ConversionRates
import com.example.exchangeRates.model.CurrencyRate
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface ExchangeRatesViewModelType {
    val selectedCurrencyRate: BehaviorSubject<CurrencyRate>
    val contentToDisplay: BehaviorSubject<List<CurrencyRate>>
    val rateUpdates: BehaviorSubject<List<Double>>
}

class ExchangeRatesViewModel @Inject constructor(serverAPI: ServerAPI) : ViewModel(), ExchangeRatesViewModelType {

    /**
     *  Observable cum observer used to track changes in base currency or rate of the currency
     */
    override val selectedCurrencyRate = BehaviorSubject.createDefault(CurrencyRate(Currency.getInstance("EUR"), 100.0))

    /**
     *  Stream that emits data when base currency has been changed
     *  Data emitted: List of CurrencyRates
     */
    override val contentToDisplay: BehaviorSubject<List<CurrencyRate>> = BehaviorSubject.create()

    /**
     *  Stream that emits data when rate of base currency has been changed
     *  Data emitted: List of rates of all currencies
     */
    override val rateUpdates = BehaviorSubject.create<List<Double>>()

    /**
     *  Stream that makes the network call every one second or when different
     *  base currency has been selected by user
     */
    private val ratesStream: Observable<ConversionRates> =
        Observable.combineLatest(
            Observable.interval(1, TimeUnit.SECONDS),
            selectedCurrencyRate,
            BiFunction { _: Long, currencyRate: CurrencyRate -> currencyRate })
            .observeOn(Schedulers.io())
            .flatMap {
                serverAPI.getConversionRatesDetailsFor(it.currency.currencyCode)
            }.map {
                it.copy(rates = it.rates.mapValues { entry ->
                    (entry.value * (selectedCurrencyRate.value?.rate ?: -1.0)).roundToDecimals(2)
                }.plus((selectedCurrencyRate.value!!.currency.currencyCode to selectedCurrencyRate.value!!.rate)).reversed())
            }

    init {
        ratesStream
            .distinctUntilChanged { lhs, rhs ->
                lhs.base == rhs.base
            }
            .map { conversionRates ->
                conversionRates.rates.map {
                    CurrencyRate(currency = Currency.getInstance(it.key), rate = it.value)
                }
            }.subscribe(contentToDisplay)

        ratesStream.map { conversionRates ->
            conversionRates.rates.values.toList()
        }.distinctUntilChanged().subscribe(rateUpdates)
    }
}


