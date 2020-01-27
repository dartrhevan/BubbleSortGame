package com.example.bublesortgame.Model.bubbles

import com.example.bublesortgame.Model.Colour
import com.example.bublesortgame.Model.Game

class StandartBubble(game: Game, y: Float, colour: Colour, line: Int) : Bubble(game, y, colour, line) {
    override val label = ""

    override fun extraAction() {}

}