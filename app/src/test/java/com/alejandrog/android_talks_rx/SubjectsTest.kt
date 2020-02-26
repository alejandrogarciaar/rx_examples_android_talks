package com.alejandrog.android_talks_rx

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.junit.Test

class SubjectsTest {

    @Test
    fun testPublishSubject() {
        val source = PublishSubject.create<Int>()
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onComplete()
    }

    private val dispatcher = PublishSubject.create<String>()
    private lateinit var action: String

    @Test
    fun examplePublishSubject() {
        dispatcher.map {
            println("dispatch : $it")
            // call reducers

            // call epics

            // call middlewares

        }.subscribe()
        //appStore.dispatch("")
        dispatcher.onNext("redux.action.logged")
    }

    @Test
    fun testReplaySubject() {
        val source = ReplaySubject.create<Int>()
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.onNext(4)
        source.onComplete()
        source.subscribe(getSecondObserver())
    }

    @Test
    fun testBehaviorSubject() {
        val source = BehaviorSubject.create<Int>()
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onComplete()
    }

    @Test
    fun testAsyncSubject() {
        val source = AsyncSubject.create<Int>()
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onComplete()
    }

    @Test
    fun testObservableSource() {
        getObservableSource().subscribe { println("testObservableSource : $it") }
        Observable.fromArray(arrayListOf(1, 2, 3, 4)).doOnNext { println("test : $it") }.subscribe()
    }

    private fun getObservableSource(): Observable<Int> {
        return Observable.create {
            it.onNext(0)
            it.onNext(1)
            it.onNext(2)
            it.onNext(3)
            it.onNext(4)
        }
    }

    @Test
    fun testSingleSource() {
        getSingleSource().toObservable().subscribe { println("testSingleSource : $it") }
        Single.just(arrayListOf(1, 2, 3, 4)).toObservable().subscribe { println("testSingleSource : ${it.size}") }
    }

    @Test
    fun testMaybeSource() {
        Maybe.empty<String>().subscribe({}, {}, { println("getEmptyMaybe: onCompleted") })
        Maybe.just("hello").subscribe({ println("getEmptyMaybe:$it") }, {}, { println("getEmptyMaybe: onCompleted hello") })
    }

    @Test
    fun testZipObservable() {
        val observableOne = Observable.just(1, 2, 3, 4, 5, 6, 7)
        val observableTwo = Observable.just("hello", "world", "android", "talks")
        Observable.zip(observableOne, observableTwo, BiFunction<Int, String, String> { t1, t2 -> "$t1 - $t2" })
            .subscribe { println("testZipObservable : $it") }
    }

    @Test
    fun testMergeObservable() {
        val observableOne = Observable.just(1, 2, 3, 4, 5)
        val observableTwo = Observable.just(20, 30, 40, 50, 60)
        Observable.merge(observableOne, observableTwo).subscribe { println("mergeResult merge: $it") }
        Observable.mergeArray(observableOne, observableTwo).subscribe { println("mergeResult merge: $it") }
        Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5)).subscribe { println("mergeResult merge: $it") }
    }

    @Test
    fun testFlatMap() {
        val observableOne = Observable.fromIterable(arrayListOf(1, 2, 3, 4, 5))
        val observableTwo = Observable.fromIterable(arrayListOf("a", "b", "c", "d", "e"))

        observableOne
            .doOnNext { println("observable1: $it") }
            .switchMap { observableTwo }
            .subscribe { println("observable2: $it") }
    }

    @Test
    fun testSwitchMap() {
        Observable.fromArray(arrayListOf("a", "b", "c", "d"))
            .switchMap { Observable.fromArray(arrayListOf("ax", "bx", "cx", "dx")) }
            .subscribe {
                println("testSwitchMap : $it")
            }
    }

    private fun getFirstObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                println("Alumno 1 onSubscribe : " + d.isDisposed)
            }

            override fun onComplete() {
                println("Alumno 1 onComplete")
            }

            override fun onNext(value: Int) {
                println("Alumno 1 onNext value : $value")
            }

            override fun onError(e: Throwable) {}
        }
    }

    private fun getSecondObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                println("Alumno 2 onSubscribe : " + d.isDisposed)
            }

            override fun onComplete() {
                println("Alumno 2 onComplete")
            }

            override fun onNext(value: Int) {
                println("Alumno 2 onNext value : $value")
            }

            override fun onError(e: Throwable) {}
        }
    }

    private fun getSingleSource(): Single<Int> {
        return Single.create {
            it.onSuccess(0)
            it.onError(Throwable(""))
        }
    }
}