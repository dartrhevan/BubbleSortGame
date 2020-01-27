package com.example.bublesortgame

import com.example.bublesortgame.Model.Bubble
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun performanceCollectionComparison() {
        val set: MutableSet<Int> = ConcurrentHashMap.newKeySet()//: CopyOnWriteArrayList<Bubble> = CopyOnWriteArrayList())//ConcurrentLinkedDeque
        val list = CopyOnWriteArrayList<Int>()
        var before = System.nanoTime()

        for(i in 0..1000)
            list.add(Random.nextInt())/*
        for(i in 0..1000)
            list.remove(Random.nextInt())*/
        iterate(list)

        var after = System.nanoTime()
        System.out.println("List: ${after - before}\n")

        before = System.nanoTime()
        for(i in 0..1000)
            set.add(Random.nextInt())/*
        for(i in 0..1000)
            set.remove(5)*/
        iterate(set)
        after = System.nanoTime()
        System.out.println("Set:  ${after - before}\n")
    }

    fun iterate(col: Collection<Int>) {
        for(i in 0..100)
            col.forEach {}
    }
}
