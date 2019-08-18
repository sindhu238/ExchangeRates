package com.example.exchangeRates.activities.exchangeRates

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exchangeRates.R
import com.example.exchangeRates.adapters.ExchangeRatesAdapter
import com.example.exchangeRates.adapters.PayloadType
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_exchange_rates.*
import javax.inject.Inject

class ExchangeRatesActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: ExchangeRatesViewModelType

    @Inject
    lateinit var exchangeAdapter: ExchangeRatesAdapter

    private val bag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rates)

        with(ratesRecyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ExchangeRatesActivity)
            adapter = exchangeAdapter.also {
                it.valueChanges.subscribe(viewModel.selectedCurrencyRate)
            }
        }

        viewModel.contentToDisplay
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                (ratesRecyclerView.adapter as? ExchangeRatesAdapter)?.let { adapter ->
                    adapter.items = it
                    adapter.notifyDataSetChanged()
                }
            }.addTo(bag)

        viewModel.rateUpdates
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ratesRecyclerView.postOnAnimation {
                    (ratesRecyclerView.adapter as? ExchangeRatesAdapter)?.let { adapter ->
                        adapter.items = adapter.items.mapIndexed { index, currencyRate ->
                            currencyRate.copy(rate = if (index < it.size) it[index] else -1.0)
                        }
                        it.forEachIndexed { i, _ ->
                            if (i != 0)
                                adapter.notifyItemChanged(i, PayloadType.Rate)
                        }
                    }
                }
            }.addTo(bag)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}