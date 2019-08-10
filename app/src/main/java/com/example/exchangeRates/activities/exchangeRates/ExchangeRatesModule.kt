package com.example.exchangeRates.activities.exchangeRates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exchangeRates.di.viewModel.MyViewModelFactory
import com.example.exchangeRates.di.viewModel.ViewModelFactoryModule
import com.example.exchangeRates.di.viewModel.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class, ExchangeRatesModule.ProvideViewModel::class])
abstract class ExchangeRatesModule {
    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun bind(): ExchangeRatesActivity

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(ExchangeRatesViewModel::class)
        fun provideViewModel(): ViewModel =
            ExchangeRatesViewModel()
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideExchangeRatesViewModel(
            factory: MyViewModelFactory,
            target: ExchangeRatesActivity
        ): ExchangeRatesViewModel = ViewModelProvider(target, factory).get(ExchangeRatesViewModel::class.java)

    }

}