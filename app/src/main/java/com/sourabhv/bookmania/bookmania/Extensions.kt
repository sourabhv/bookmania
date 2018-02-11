package com.sourabhv.bookmania.bookmania

import android.content.Context
import android.support.annotation.LayoutRes
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Number.toPx(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

private val beforeTextChangedStub = { _: CharSequence?, _: Int, _: Int, _: Int -> Unit }
private val onTextChangedStub = { _: CharSequence?, _: Int, _: Int, _: Int -> Unit }
private val afterTextChanged = { _: Editable? -> Unit }

fun EditText.addTextWatcher(
        beforeTextChange: (var1: CharSequence?, var2: Int, var3: Int, var4: Int) -> Unit = beforeTextChangedStub,
        onTextChange: (var1: CharSequence?, var2: Int, var3: Int, var4: Int) -> Unit = onTextChangedStub,
        afterTextChange: (var1: Editable?) -> Unit = afterTextChanged
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChange(p0, p1, p2, p3)
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChange(p0, p1, p2, p3)
        }

        override fun afterTextChanged(p0: Editable?) {
            afterTextChange(p0)
        }

    })
}

fun EditText.reactiveSearch(func: (String) -> Unit, debounceTime: Long = 200): Disposable {
    val searchProcessor = BehaviorProcessor.create<String>()
    val disposable = searchProcessor
            .debounce(debounceTime, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                    onNext = { func(it) },
                    onError = { Timber.e(it) }
            )
    this.addTextWatcher(afterTextChange = { searchProcessor.onNext(it?.toString() ?: "") })
    return disposable
}