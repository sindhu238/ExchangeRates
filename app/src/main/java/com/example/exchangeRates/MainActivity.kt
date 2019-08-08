package com.example.exchangeRates

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.exchangeRates.api.ServerAPIImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ServerAPIImpl.getConversionRatesDetails()
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.v("sindhu", it.base)
            }.addTo(CompositeDisposable())
    }
}
