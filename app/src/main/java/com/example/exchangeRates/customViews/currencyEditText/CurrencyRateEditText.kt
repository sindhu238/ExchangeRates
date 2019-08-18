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

    val viewModel = CurrencyEditTextViewModel()

    /**
     * Emits events when user changes rate of base currency
     * Emitted Data - Rate of currency
     */
    val textChanges = PublishSubject.create<Double>()

    /**
     * Property that specifies if edit text can update values or not
     * By default it is set to true
     */
    private var canUpdate: Boolean = true

    private val bag = CompositeDisposable()

    init {
        isFocusable = false
        isFocusableInTouchMode = false

        textChanges()
            .skipInitialValue()
            .filter { !canUpdate && this.isShown }
            .map { it.toString() }
            .subscribe(viewModel.textChanges)

        viewModel.amendedTextStream.subscribe(textChanges)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bag.clear()
    }

    /**
     * Sets focus and selection to end of edit text
     * Sets canUpdate to false - which means that editText cannot be updated
     */
    fun setFocusAndSelection() {
        setSelection(text.count())
        canUpdate = false
        isFocusable = true
        isFocusableInTouchMode = true
    }
}