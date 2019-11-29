package com.example.bublesortgame.results

import java.util.*

data class Result(val scores: Int, val desc: String, val date: Date = Date()){
    override fun toString(): String  = "$desc: $scores ($date)"
}