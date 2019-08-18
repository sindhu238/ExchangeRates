package com.example.exchangeRates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeRates.R
import com.example.exchangeRates.cache.RxDataCacheType
import com.example.exchangeRates.extensions.toDouble
import com.example.exchangeRates.model.CurrencyRate
import com.jakewharton.rxbinding3.view.clicks
import com.jwang123.flagkit.FlagKit
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.rates_adapter_view.view.*
import java.util.*
import javax.inject.Inject


enum class PayloadType {
    Rate
}

interface ExchangeRatesAdapterType {

    /**
     * Emits events when user clicks on the item or when user changes currency rate
     * Data Emitted - Currency Rate
     */
    val valueChanges: PublishSubject<CurrencyRate>
}

class ExchangeRatesAdapter @Inject constructor(
    var items: List<CurrencyRate>,
    var dataCache: RxDataCacheType
) : ExchangeRatesAdapterType, RecyclerView.Adapter<ExchangeRatesAdapter.MyViewHolder>() {

    override val valueChanges = PublishSubject.create<CurrencyRate>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rates_adapter_view, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty())
            when (payloads[0]) {
                PayloadType.Rate -> if (position != 0)
                    holder.itemView.rateET.setText(items[position].rate.toString())
            }
        else
            super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.itemView) {
            currencyCodeTV.text = items[position].currency.currencyCode
            currencyNameTV.text = items[position].currency.displayName
            rateET.setText(items[position].rate.toString())
            dataCache.getCountryForCurrency(items[position].currency)?.let { country ->
                try {
                    flagImageView.setImageDrawable(FlagKit.drawableWithFlag(context, country.toLowerCase()))
                } catch (exception: Exception) {
                    flagImageView.setImageDrawable(FlagKit.drawableWithFlag(context, Locale.getDefault().country.toLowerCase()))
                }
            }

            if (position == 0) {
                rateET.setFocusAndSelection()
            }

            rateET.textChanges.map {
                CurrencyRate(currency = items[0].currency, rate = it)
            }.distinctUntilChanged().subscribe(valueChanges)

            clicks().map {
                CurrencyRate(currency = items[position].currency, rate = rateET.text.toDouble() ?: -1.0)
            }.doOnNext {
                notifyItemMoved(position, 1)
            }.subscribe(valueChanges)
        }
    }
}