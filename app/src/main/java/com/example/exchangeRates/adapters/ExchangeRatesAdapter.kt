package com.example.exchangeRates.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeRates.extensions.toDouble
import com.example.exchangeRates.model.CurrencyRate
import com.jakewharton.rxbinding3.view.clicks
import com.jwang123.flagkit.FlagKit
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.rates_adapter_view.view.*
import java.text.NumberFormat
import java.util.*


enum class PayloadType {
    Rate
}

class ExchangeRatesAdapter(
    var items: List<CurrencyRate>
) : RecyclerView.Adapter<ExchangeRatesAdapter.MyViewHolder>() {

    val valueChanges = PublishSubject.create<CurrencyRate>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                com.example.exchangeRates.R.layout.rates_adapter_view,
                parent,
                false
            )
        )

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
            flagImageView.setImageDrawable(getDrawableFor(items[position].currency, context))

            if (position == 0) {
                rateET.setSelection(rateET.text.count())
                rateET.canUpdate = false
                rateET.isFocusable = true
                rateET.isFocusableInTouchMode = true
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


    private fun getDrawableFor(currency: Currency, context: Context): Drawable? =
        Locale.getAvailableLocales().firstOrNull {
            NumberFormat.getCurrencyInstance(it).currency == currency
        }?.let {
            try {
                FlagKit.drawableWithFlag(context, it.country.toLowerCase())
            } catch (exception: Exception) {
                return@let null
            }
        }
}