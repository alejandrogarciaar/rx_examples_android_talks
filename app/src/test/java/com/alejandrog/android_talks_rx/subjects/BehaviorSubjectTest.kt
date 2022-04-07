package com.alejandrog.android_talks_rx.subjects

import com.alejandrog.android_talks_rx.observers.MyCustomObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class BehaviorSubjectTest {

    @Test
    fun testMyBehaviorSubject() {

        val mySubject = MyBehaviorSubject()
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
    }

    @Test
    fun testMyBehaviorSubjectTwo() {
        val myBSubject = MyBehaviorSubject()
        val disposable = myBSubject.stream()
            .filter { !it.contains("Legolas") }
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

        myBSubject.update("Aragorn")
        myBSubject.update("Legolas")
        myBSubject.update("Gimli")
        myBSubject.update("Sauron")
        myBSubject.update("Frodo")

        disposable.dispose()

        myBSubject.update("Sam")
        myBSubject.update("Boromir")
    }
}

private class MyBehaviorSubject {

    private val behaviorSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("-")

    fun addObserver(observer: Observer<String>) {
        behaviorSubject.subscribe(observer)
    }

    fun stream(): Observable<String> {
        return behaviorSubject.hide()
    }

    fun update(item: String) {
        behaviorSubject.onNext(item)
    }
}

