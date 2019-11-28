package com.example.bublesortgame.Model

class StandartBubble(game: Game,y: Float,colour: Colour,line: Int) : Bubble(game, y, colour, line) {
    override val label = ""

    override fun extraAction() {}

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}