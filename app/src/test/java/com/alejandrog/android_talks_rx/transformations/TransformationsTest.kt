package com.alejandrog.android_talks_rx.transformations

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test

class TransformationsTest {

    @Test
    fun testObservableSource() {
        // Iterable
        val disposable = getObservableSource()
            .subscribe { println("testObservableSource : $it") }

        // List itself
        Observable.fromArray(arrayListOf(1, 2, 3, 4))
            .doOnNext { println("test : $it") }
            .subscribe()

        disposable.dispose()
    }

    private fun getObservableSource(): Observable<Int> {
        return Observable.fromIterable(arrayListOf(1, 2, 3, 4))
    }

    @Test
    fun testSingleSource() {
        val disposable = getSingleSourceWithError()
            .toObservable()
            .subscribeBy(
                onNext = { println("testSingleSource : $it") },
                onError = { println("testSingleSource : $it") }
            )
        disposable.dispose()

        val disposableTwo = Single.just(arrayListOf(1, 2, 3, 4))
            .toObservable()
            .subscribe { println("testSingleSource : ${it.size}") }
        disposableTwo.dispose()
    }

    @Test
    fun testMaybeSource() {
        val disposable = Maybe.empty<String>()
            .subscribe({}, {}, { println("getEmptyMaybe: onCompleted") })
        disposable.dispose()
        val disposableTwo = Maybe.just("hello")
            .subscribe({ println("getEmptyMaybe:$it") }, {}, { println("getEmptyMaybe: onCompleted hello") })
        disposableTwo.dispose()
    }

    @Test
    fun testZipObservable() {
        val observableOne = Observable.just(1, 2, 3, 4, 5, 6, 7)
        val observableTwo = Observable.just("hello", "world", "android", "talks")
        val disposable = Observable.zip(observableOne, observableTwo, BiFunction<Int, String, String> { t1, t2 -> "$t1 - $t2" })
            .subscribe { println("testZipObservable : $it") }
        disposable.dispose()
    }

    @Test
    fun testMergeObservable() {
        val observableOne = Observable.just(1, 2, 3, 4, 5)
        val observableTwo = Observable.just(20, 30, 40, 50, 60)
        val disposable = Observable.merge(observableOne, observableTwo).subscribe { println("mergeResult merge: $it") }
        val disposableTwo = Observable.mergeArray(observableOne, observableTwo).subscribe { println("mergeResult merge: $it") }
        val disposableThree = Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5)).subscribe { println("mergeResult merge: $it") }

        disposable.dispose()
        disposableTwo.dispose()
        disposableThree.dispose()
    }

    @Test
    fun testFlatMap() {
        val observableOne = Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5))
        val observableTwo = Observable.fromIterable(arrayListOf("a", "b", "c", "d", "e"))

        val disposable = observableOne
            .doOnNext { println("observable1: $it") }
            .switchMap { observableTwo }
            .subscribe { println("observable2: $it") }

        disposable.dispose()
    }

    @Test
    fun testSwitchMap() {
        val disposable = Observable.fromArray(arrayListOf("a", "b", "c", "d"))
            .switchMap { Observable.fromArray(arrayListOf("ax", "bx", "cx", "dx")) }
            .subscribe {
                println("testSwitchMap : $it")
            }
        disposable.dispose()
    }

    private fun getSingleSourceWithError(): Single<Int> {
        return Single.create {
            it.onSuccess(0)
            it.onError(Throwable(""))
        }
    }
}