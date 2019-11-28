package com.example.bublesortgame.Model

class SmallBonusBubble(game: Game, y: Float, colour: Colour, line: Int) : Bubble(game, y, colour, line) {
    override val label = "3"

    override fun extraAction() {
        game.scores += 2
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