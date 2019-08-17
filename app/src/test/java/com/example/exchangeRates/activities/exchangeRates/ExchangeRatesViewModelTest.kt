package com.example.exchangeRates.activities.exchangeRates

import com.example.exchangeRates.api.ServerAPI
import com.example.exchangeRates.model.ConversionRates
import com.example.exchangeRates.model.CurrencyRate
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class ExchangeRatesViewModelTest {
    lateinit var viewModel: ExchangeRatesViewModel

    @Mock
    lateinit var serverAPI: ServerAPI

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockitoAnnotations.initMocks(this)
        viewModel = ExchangeRatesViewModel(serverAPI)
    }

    @Test
    fun `test initial set currency`() {
        // arrange
        val testObserver = TestObserver<CurrencyRate>()

        // act
        viewModel.selectedCurrencyRate.subscribe(testObserver)

        // assert
        testObserver.assertValue(CurrencyRate(Currency.getInstance("EUR"), 100.0))
    }

    @Test
    fun `test contents to display stream`() {
        // arrange
        val conversionRates = ConversionRates(base = "EUR", rates = mapOf("AUD" to 12.34, "GBP" to 5.6))
        whenever(serverAPI.getConversionRatesDetailsFor("EUR")).thenReturn(Observable.just(conversionRates))
        val testObserver = TestObserver<List<CurrencyRate>>()

        // act
        viewModel.selectedCurrencyRate.onNext(CurrencyRate(Currency.getInstance("EUR"), 100.0))
        viewModel.contentToDisplay.subscribe(testObserver)
        viewModel.contentToDisplay.blockingFirst()

        // assert
        testObserver.assertValue(listOf(
            CurrencyRate(Currency.getInstance("EUR"), 100.0),
            CurrencyRate(Currency.getInstance("GBP"), 560.0),
            CurrencyRate(Currency.getInstance("AUD"), 1234.0)
        ))
    }

    @Test
    fun `test rate updates stream`() {
        // arrange
        val conversionRates = ConversionRates(base = "EUR", rates = mapOf("AUD" to 12.34, "GBP" to 5.6))
        whenever(serverAPI.getConversionRatesDetailsFor("EUR")).thenReturn(Observable.just(conversionRates))
        val testObserver = TestObserver<List<Double>>()

        // act
        viewModel.selectedCurrencyRate.onNext(CurrencyRate(Currency.getInstance("EUR"), 100.0))
        viewModel.rateUpdates.subscribe(testObserver)
        viewModel.rateUpdates.blockingFirst()

        // assert
        testObserver.assertValue(listOf(100.0,560.0,1234.0))
    }
}