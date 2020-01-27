package com.example.bublesortgame.Model.bubbles

import com.example.bublesortgame.Model.Colour
import com.example.bublesortgame.Model.Game

class SmallBonusBubble(game: Game, y: Float, colour: Colour, line: Int) : Bubble(game, y, colour, line) {
    override val label = "3"

    override fun extraAction() {
        game.scores += 2
    }

}