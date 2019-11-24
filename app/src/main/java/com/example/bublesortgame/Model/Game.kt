package com.example.bublesortgame.Model

import kotlin.math.abs

class Game(var scores: Int = 0,val bubbles: ArrayList<Bubble> = arrayListOf(/*Bubble(200f, 300f, Colour.YELLOW, 0)*/),val receivers : Map<Int, Receiver> =
               mapOf(0 to Receiver(0, Colour.BLUE), 1 to Receiver(1, Colour.RED), 2 to
                   Receiver(2, Colour.GREEN), 3 to Receiver(3, Colour.YELLOW))) {
    val slider: Slider = Slider(this)
    private fun acceptBubble(bubble: Bubble){
        if(receivers[bubble.line]?.colour == bubble.colour)
            scores++
        else scores--
        bubbles.remove(bubble)
    }
    fun act(bottom: Float) : Bubble? {
        return slider.act(bottom, getReceiver())
    }

    private fun getReceiver() : Receiver?
    {
        val r: Receiver = receivers.minBy { abs(slider.centerX - it.value.centerX) }!!.value
        return if(abs(slider.centerX - r.centerX) < DELTA) r else null
    }
    companion object{
        private val DELTA = 5
        var ReceiverWidth = 0
        var SliderWidth = 0
    }
}