package com.example.bublesortgame.Model

data class Bubble(val game: Game,/* var X: Float,*/ var Y: Float, var colour: Colour, var line: Int) {
/*
    public var Y: Int
    get()
    init{

    }*/
    val X: Float
    get() = game.receivers[line]!!.X
    fun getCentralX(diam: Float): Float = X + diam / 2

    fun getCentralY(diam: Float): Float = Y + diam / 2
}