package com.example.bublesortgame.Model

import java.util.*
import kotlin.random.Random
import kotlin.random.nextUInt

data class Slider(val game: Game, var X: Float = 0f) {
    private val r = Random(Date().time)
    fun act(Y: Float, rec : Receiver?) : Bubble? {
        if(rec == null) return null
        if((megaBoost || r.nextBoolean() ) && rec.number != lastLine) {
            lastLine = rec.number
            val element = Bubble(game/*, rec.X */,Y,Colour.values()[r.nextUInt(4u).toInt()],rec.number)
            game.bubbles.add(element)
            return element
        }
        return null
    }
    var megaBoost = false
    val centerX: Float
    get() = X + Game.SliderWidth / 2
    private var lastLine: Int = -1
}