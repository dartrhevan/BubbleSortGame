package com.example.bublesortgame.Model

import java.util.*
import kotlin.random.Random

data class Slider(val game: Game, var X: Float = 0f) {
    private val r = Random(Date().time)
    fun act(Y: Float) : Bubble? {
        if(/*r.nextInt() % 10 == 0*/game.scores++ == 100) {
            val element = Bubble(X,Y,Colour.BLUE,0)
            game.bubbles.add(element)
            return element
        }
        return null
    }

}