package com.example.bublesortgame.Model

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.round
import kotlin.random.Random

class Game(var scores: Int = 0,var lives: Int = 5,val bubbles: CopyOnWriteArrayList<Bubble> = CopyOnWriteArrayList()/*MutableList<Bubble> = Collections.synchronizedList(ArrayList<Bubble>())*/,
           val receivers : Map<Int, Receiver> =  mapOf(0 to Receiver(0, Colour.BLUE), 1 to Receiver(1, Colour.RED), 2 to
                   Receiver(2, Colour.GREEN), 3 to Receiver(3, Colour.YELLOW))) {
    val slider: Slider = Slider(this)
     fun acceptBubble(bubble: Bubble) : Boolean {
        if(!bubbles.contains(bubble)) return false
        if(receivers[bubble.line]?.colour == bubble.colour) {
            scores++
            bubble.extraAction()
        }
        else lives--
        bubbles.remove(bubble)
         if(!slider.megaBoost && (scores + 1) % speedUpFrequency == 0)
             speedUp()
        return lives == 0
    }

    fun restart() {
        bubbleDuration = 2000L
        sliderDuration = 1500L
        bubbles.clear()
        slider.X = 0f
        scores = 0
        slider.megaBoost = false
        lives = 5
    }

    fun act(bottom: Float) : Bubble? {
        return slider.act(bottom, getReceiver())
    }

    private fun speedUp() {
        if(bubbleDuration > 1000L) {
            bubbleDuration = round(bubbleDuration * 0.9).toLong()
            sliderDuration = round(sliderDuration * 0.9).toLong()
        } else slider.megaBoost = true
    }

    private fun getReceiver() : Receiver?
    {
        val r = receivers.minBy { abs(slider.centerX - it.value.centerX) }!!.value
        return if(abs(slider.centerX - r.centerX) < DELTA) r else null
    }
    companion object {
        private var speedUpFrequency = 10
        private const val DELTA = 5
        const val bubbleStandardDuration = 2000L
        var bubbleDuration = bubbleStandardDuration
        var sliderDuration = 1500L
        var ReceiverWidth = 0
        var SliderWidth = 0
    }
}