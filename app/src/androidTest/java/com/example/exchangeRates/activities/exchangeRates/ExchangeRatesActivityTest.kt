package com.example.exchangeRates.activities.exchangeRates

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.exchangeRates.MyApplication
import com.example.exchangeRates.adapters.ExchangeRatesAdapter
import com.example.exchangeRates.cache.RxDataCache
import com.example.exchangeRates.model.CurrencyRate
import com.nhaarman.mockito_kotlin.whenever
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Provider

@SmallTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ExchangeRatesActivityTest {

    @Mock
    lateinit var fakeViewModel: ExchangeRatesViewModelType

    @Rule
    @JvmField
    var activityRule =
        object : ActivityTestRule<ExchangeRatesActivity>(ExchangeRatesActivity::class.java, false, false) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                val myApp =
                    InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApplication
                myApp.dispatchingActivityInjector = createFakeMainActivityInjector {
                    viewModel = fakeViewModel
                    exchangeAdapter = ExchangeRatesAdapter(listOf(), RxDataCache)
                }
            }
        }

    private val currencyRateList = listOf(
        CurrencyRate(Currency.getInstance("EUR"), 100.0),
        CurrencyRate(Currency.getInstance("GBP"), 2100.0),
        CurrencyRate(Currency.getInstance("INR"), 1000.0)
    )

    private val contentStream = BehaviorSubject.create<List<CurrencyRate>>()
    private val selectedCurrencyStream = BehaviorSubject.create<CurrencyRate>()
    private val ratesStream = BehaviorSubject.create<List<Double>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(fakeViewModel.contentToDisplay).thenReturn(contentStream)
        whenever(fakeViewModel.selectedCurrencyRate).thenReturn(selectedCurrencyStream)
        whenever(fakeViewModel.rateUpdates).thenReturn(ratesStream)
        activityRule.launchActivity(null)

    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun test_display_of_items() {
        // act
        contentStream.onNext(currencyRateList)

        // assert
        onView(withText("EUR")).check(matches(isDisplayed()))
        onView(withText("INR")).check(matches(isDisplayed()))
        onView(withText("GBP")).check(matches(isDisplayed()))
    }

    @Test
    fun test_updates_of_rates() {
        // act
        contentStream.onNext(currencyRateList)
        ratesStream.onNext(listOf(100.5, 125.5, 2342.0))

        // assert
        onView(withText("100.5")).check(matches(isDisplayed()))
        onView(withText("125.5")).check(matches(isDisplayed()))
        onView(withText("2342.0")).check(matches(isDisplayed()))
    }

    private fun createFakeMainActivityInjector(block: ExchangeRatesActivity.() -> Unit)
            : DispatchingAndroidInjector<Activity> {
        val injector = AndroidInjector<Activity> { instance ->
            if (instance is ExchangeRatesActivity) {
                instance.block()
            }
        }
        val factory = AndroidInjector.Factory<Activity> { injector }
        val map = mapOf(
            Pair<Class<*>,
                    Provider<AndroidInjector.Factory<*>>>(ExchangeRatesActivity::class.java, Provider { factory })
        )
        return DispatchingAndroidInjector_Factory.newInstance(map, emptyMap())
    }
}
