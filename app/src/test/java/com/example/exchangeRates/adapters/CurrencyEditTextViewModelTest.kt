package com.example.exchangeRates.adapters

import com.example.exchangeRates.customViews.currencyEditText.CurrencyEditTextViewModel
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class CurrencyEditTextViewModelTest {

    private lateinit var viewModel: CurrencyEditTextViewModel
    private lateinit var testObserver: TestObserver<Double>
    private lateinit var textStream: PublishSubject<String>

    @Before
    fun setup() {
        viewModel = CurrencyEditTextViewModel()
        testObserver = TestObserver.create()
        textStream = PublishSubject.create()

        viewModel.amendedTextStream.subscribe(testObserver)
        textStream.subscribe(viewModel.textChanges)
    }

    @Test
    fun `test outputs of amended text stream - When focused`() {
        // arrange
        // act
        textStream.onNext("1.2")

        // assert
        testObserver.assertValue(1.2)
    }

    @Test
    fun `test outputs of amended text stream - Incorrect value`() {
        // arrange
        // act
        textStream.onNext("sf")

        // assert
        testObserver.assertNoValues()
    }
}