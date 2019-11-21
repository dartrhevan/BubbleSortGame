package com.example.bublesortgame.Model

class Game(sY: Int, private var scores: Int = 0, private val bubbles: ArrayList<Bubble> = ArrayList(),
           private val slider: Slider = Slider(sY), public val receivers : Map<Int, Receiver> =
               mapOf(0 to Receiver(0, Colour.BLUE), 1 to Receiver(1, Colour.RED), 2 to
                   Receiver(2, Colour.GREEN), 3 to Receiver(3, Colour.YELLOW))) {
    fun acceptBubble(bubble: Bubble){
        if(receivers[bubble.line]?.colour == bubble.colour)
            scores++
        else scores--
        bubbles.remove(bubble)
    }
}