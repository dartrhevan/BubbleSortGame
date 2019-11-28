package com.example.bublesortgame.Model

class BigBonusBubble(game: Game,y: Float,colour: Colour,line: Int) : Bubble(game, y, colour, line) {
    override val label = "5"

    override fun extraAction() {
        game.scores += 4
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