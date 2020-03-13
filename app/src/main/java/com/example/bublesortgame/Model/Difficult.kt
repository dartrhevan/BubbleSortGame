package com.example.bublesortgame.Model

data class Difficult private constructor(var bubbleDuration : Long, var sliderDuration : Long, var delta : Int ){
    companion object {
        fun getEasyDifficult() = Difficult(2500L, 2000L, 4 )

        fun getStandardDifficult() = Difficult(2000L, 1500L, 12)

        fun getHardDifficult() = Difficult(1500L,  1000L, 20)
    }
}