package com.example.bublesortgame.Model.bubbles

import com.example.bublesortgame.Model.Colour
import com.example.bublesortgame.Model.Game

class ExtraLifeBubble(game: Game, y: Float, colour: Colour, line: Int) : Bubble(game, y, colour, line) {
    override val label = "L"

    override fun extraAction() {
        game.lives++
    }

}