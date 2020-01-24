package com.example.bublesortgame.Model

data class Receiver(var number: Int, var colour: Colour, var X: Float = 0f) {
    fun recieve(bubble: Bubble) {
    }
    val centerX : Float
        get() = X + Game.ReceiverWidth / 2
}