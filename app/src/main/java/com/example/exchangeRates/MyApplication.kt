package com.example.exchangeRates

import android.app.Activity
import android.app.Application
import com.example.exchangeRates.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    companion object {
        val localesWithCurrencies: Map<String, Currency> by lazy {
            mutableMapOf<String, Currency>().apply {
                NumberFormat.getAvailableLocales().map { locale ->
                    put(locale.country, NumberFormat.getCurrencyInstance(locale).currency)
                }
            }
        }
    }

}
