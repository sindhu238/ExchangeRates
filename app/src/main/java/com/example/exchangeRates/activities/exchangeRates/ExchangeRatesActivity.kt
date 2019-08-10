package com.example.exchangeRates.activities.exchangeRates

import android.os.Bundle
import com.example.exchangeRates.R
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ExchangeRatesActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: ExchangeRatesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rates)
    }
}