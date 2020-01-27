package com.example.bublesortgame.Model


class Fragment(val id: Int, var X: Float, var Y: Float, val tX: Float, val tY: Float, val duration: Int, val colour: Colour)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fragment

        if (id != other.id) return false
        if (colour != other.colour) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + colour.hashCode()
        return result
    }
}