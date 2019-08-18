package com.example.exchangeRates.activities.exchangeRates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exchangeRates.api.ServerAPI
import com.example.exchangeRates.api.ServerAPIImpl
import com.example.exchangeRates.di.viewModel.MyViewModelFactory
import com.example.exchangeRates.di.viewModel.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ExchangeRatesModule {

    @Module
    class ProvideViewModel {

        @Provides
        fun provideServerApi(): ServerAPI = ServerAPIImpl

        @Provides
        @IntoMap
        @ViewModelKey(ExchangeRatesViewModel::class)
        fun provideViewModel(serverAPI: ServerAPI): ViewModel =
            ExchangeRatesViewModel(serverAPI)
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideExchangeRatesViewModel(
            factory: MyViewModelFactory,
            target: ExchangeRatesActivity
        ): ExchangeRatesViewModelType = ViewModelProvider(target, factory).get(ExchangeRatesViewModel::class.java)
    }

}