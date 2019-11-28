package com.example.bublesortgame.Model

class ExtraLifeBubble(game: Game,y: Float,colour: Colour,line: Int) : Bubble(game, y, colour, line) {
    override val label = "L"

    override fun extraAction() {
        game.lives++
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}