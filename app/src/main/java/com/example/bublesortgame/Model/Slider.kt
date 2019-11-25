package com.example.bublesortgame.Model

import java.util.*
import kotlin.random.Random

data class Slider(val game: Game, var X: Float = 0f) {
    private val r = Random(Date().time)
    fun act(Y: Float, rec : Receiver?) : Bubble? {
        if(rec == null) return null
        if(r.nextInt() % 2 == 0) {
            val element = Bubble(game/*, rec.X */,Y,Colour.BLUE,rec.number)
            game.bubbles.add(element)
            return element
        }
        return null
    }
    val centerX: Float
    get() = X + Game.SliderWidth / 2

}