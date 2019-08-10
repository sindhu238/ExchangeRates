package com.example.exchangeRates.di

import com.example.exchangeRates.activities.exchangeRates.ExchangeRatesActivity
import com.example.exchangeRates.di.viewModel.ViewModelFactoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    internal abstract fun bindMainActivity(): ExchangeRatesActivity
}


