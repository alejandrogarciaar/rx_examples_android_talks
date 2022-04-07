package com.alejandrog.android_talks_rx.observables

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test

class CombineLatestTest {

    @Test
    fun testTripleSources() {
        val historySubject = MyBehaviorSubject("History Topic")
        val geographySubject = MyBehaviorSubject("Geography Topic")
        val scienceSubject = MyBehaviorSubject("Science Topic")

        val disposable = Observables.combineLatest(
            historySubject.stream(),
            geographySubject.stream(),
            scienceSubject.stream()
        )
            .subscribeBy(
                onNext = {
                    val historyValue = it.first
                    val geographyValue = it.second
                    val scienceValue = it.third
                    println("onNext -> Subject: $historyValue | Subject: $geographyValue | Subject: $scienceValue")
                },
                onError = {
                    println("Error! $it")
                }
            )

        historySubject.update("Feudal Age")
        geographySubject.update("Colombia")
        scienceSubject.update("Computation")
        disposable.dispose()
    }

    @Test
    fun testMultipleSources() {
        val historySubject = MyBehaviorSubject("History Topic")
        val geographySubject = MyBehaviorSubject("Geography Topic")
        val scienceSubject = MyBehaviorSubject("Science Topic")
        val sportsSubject = MyBehaviorSubject("Sports Topic")

        val disposable = Observables.combineLatest(
            historySubject.stream(),
            geographySubject.stream(),
            scienceSubject.stream(),
            sportsSubject.stream()
        ) { history, geography, science, sports ->
            StreamsState(
                history = history,
                geography = geography,
                science = science,
                sports = sports
            )
        }
            .subscribeBy(
                onNext = {
                    println("onNext -> Subject: ${it.history} | Subject: ${it.geography} | Subject: ${it.science} | Subject: ${it.sports}")
                },
                onError = {
                    println("Error! $it")
                }
            )

        historySubject.update("Feudal Age")
        geographySubject.update("Colombia")
        scienceSubject.update("Computation")
        sportsSubject.update("World Cup")
        disposable.dispose()
    }
}

private class MyBehaviorSubject(defaultValue: String) {

    private val behaviorSubject: BehaviorSubject<String> = BehaviorSubject.createDefault(defaultValue)

    fun stream(): Observable<String> {
        return behaviorSubject.hide()
    }

    fun update(item: String) {
        behaviorSubject.onNext(item)
    }
}

private data class StreamsState(
    val history: String,
    val geography: String,
    val science: String,
    val sports: String
)
