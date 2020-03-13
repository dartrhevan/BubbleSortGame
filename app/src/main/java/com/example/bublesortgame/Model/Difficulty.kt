package com.example.bublesortgame.Model

data class Difficulty private constructor(var bubbleDuration : Long, var sliderDuration : Long, var delta : Int ){
    companion object {
        fun getEasyDifficulty() = Difficulty(2500L, 2000L, 4 )

        fun getStandardDifficulty() = Difficulty(2000L, 1500L, 12)

        fun getHardDifficulty() = Difficulty(1500L,  1000L, 20)
    }
}