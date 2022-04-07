package com.alejandrog.android_talks_rx.observers

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MyCustomObserver(private val identifier: String) : Observer<String> {

    override fun onSubscribe(d: Disposable) {
        println("$identifier -> Subscrito")
    }

    override fun onNext(t: String) {
        println("$identifier -> onNext : $t")
    }

    override fun onError(e: Throwable) {
        println("$identifier -> onError : $e")
    }

    override fun onComplete() {
        println("$identifier -> onComplete")
    }
}
