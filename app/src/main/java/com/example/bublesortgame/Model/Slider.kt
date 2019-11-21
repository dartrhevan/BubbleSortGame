package com.example.bublesortgame.Model

class Slider(val Y: Int, var X: Int = 0) {
    fun generateBubble() : Bubble = Bubble(X, Y, Colour.BLUE)

}