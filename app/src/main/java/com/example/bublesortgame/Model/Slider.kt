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
            val element = generateBubble(Y,rec)
            game.bubbles.add(element)
            return element
        }
        return null
    }

    private fun generateBubble(Y: Float,rec: Receiver) : Bubble
    {
        return when(r.nextInt(100)) {
            in 0..15 -> SmallBonusBubble(game/*, rec.X */,Y,Colour.values()[r.nextUInt(4u).toInt()],rec.number)
            in 15..20 -> BigBonusBubble(game/*, rec.X */,Y,Colour.values()[r.nextUInt(4u).toInt()],rec.number)
            in 20..25 -> ExtraLifeBubble(game/*, rec.X */,Y,Colour.values()[r.nextUInt(4u).toInt()],rec.number)
            else -> StandartBubble(game/*, rec.X */,Y,Colour.values()[r.nextUInt(4u).toInt()],rec.number)
        }
    }

    var megaBoost = false
    val centerX: Float
    get() = X + Game.SliderWidth / 2
    private var lastLine: Int = -1
}