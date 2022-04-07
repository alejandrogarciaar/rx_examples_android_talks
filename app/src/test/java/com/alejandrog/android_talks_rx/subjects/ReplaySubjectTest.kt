package com.alejandrog.android_talks_rx.subjects

import com.alejandrog.android_talks_rx.observers.MyCustomObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.ReplaySubject
import org.junit.Test
import kotlin.random.Random

class ReplaySubjectTest {

    @Test
    fun testPublishSubject() {
        val mySubject = MyReplaySubject()

        val observerOne = MyCustomObserver("Alumno 1")
        val observerTwo = MyCustomObserver("Alumno 2")
        val observerThree = MyCustomObserver("Alumno 3")

        mySubject.addObserver(observerOne)

        mySubject.update("Topic 1")
        mySubject.update("Topic 2")
        mySubject.update("Topic 3")

        mySubject.addObserver(observerTwo)

        mySubject.update("Topic 4")
        mySubject.update("Topic 5")

        mySubject.addObserver(observerThree)

        mySubject.update("Topic 6")
        mySubject.update("Topic 7")

        mySubject.complete()

        mySubject.update("Topic 8")
    }

    @Test
    fun testMyBehaviorSubjectTwo() {
        val mySubject = MyReplaySubject()
        val disposable = mySubject.stream()
            .map { "${Random.nextInt()} - $it" }
            .subscribeBy(
                onNext = {
                    println("onNext: $it")
                },
                onComplete = {
                    println("onComplete")
                },
                onError = {
                    println("onError: $it")
                }
            )

        mySubject.update("Obi Wan")
        mySubject.update("Yoda")
        mySubject.update("Chewbacca")
        mySubject.update("Luke")
        mySubject.update("Leia")

        disposable.dispose()
    }
}

private class MyReplaySubject {

    private val subject: ReplaySubject<String> = ReplaySubject.create()

    fun addObserver(observer: Observer<String>) {
        subject.subscribe(observer)
    }

    fun stream(): Observable<String> {
        return subject.hide()
    }

    fun update(item: String) {
        subject.onNext(item)
    }

    fun complete() {
        subject.onComplete()
    }
}