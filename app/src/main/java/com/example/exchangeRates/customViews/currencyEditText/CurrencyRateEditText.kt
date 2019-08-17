package com.example.exchangeRates.customViews.currencyEditText

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class CurrencyRateEditText @JvmOverloads constructor(
    context: Context, attributesSet: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : EditText(context, attributesSet, defStyleAttr, defStyleRes) {

    val textChanges = PublishSubject.create<Double>()
    val viewModel = CurrencyEditTextViewModel()
    var canUpdate: Boolean = true

    private val bag = CompositeDisposable()

    init {
        textChanges()
            .skipInitialValue()
            .filter { !canUpdate }
            .map {
                it.toString()
            }
            .subscribe(viewModel.textChanges)

        viewModel.amendedTextStream.subscribe(textChanges)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bag.clear()
    }
}