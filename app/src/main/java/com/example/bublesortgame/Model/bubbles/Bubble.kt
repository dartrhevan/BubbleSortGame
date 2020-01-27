package com.example.bublesortgame.Model.bubbles

import com.example.bublesortgame.Model.Colour
import com.example.bublesortgame.Model.Game
import kotlin.random.Random

abstract class Bubble(val game: Game,/* var X: Float,*/ var Y: Float, var colour: Colour, var line: Int, val id: Int = Random.nextInt()) {
/*
    public var Y: Int
    get()
    init{

    }*/
    val X: Float
    get() = game.receivers[line]!!.X
    fun getCentralX(diam: Float): Float = X + diam / 2
    abstract val label: String
    fun getCentralY(diam: Float): Float = Y + diam / 2
    abstract fun extraAction()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bubble) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}