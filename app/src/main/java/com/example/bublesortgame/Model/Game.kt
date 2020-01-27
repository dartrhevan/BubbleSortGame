package com.example.bublesortgame.Model

import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.round
import kotlin.random.Random

@TargetApi(Build.VERSION_CODES.N)
@RequiresApi(Build.VERSION_CODES.N)
class Game(var scores: Int = 0, var lives: Int = 5, val bubbles: CopyOnWriteArrayList<Bubble> = CopyOnWriteArrayList(),//: MutableSet<Bubble> = ConcurrentHashMap.newKeySet(),
    val fragments: MutableSet<Fragment> = ConcurrentHashMap.newKeySet())//: CopyOnWriteArrayList<Bubble> = CopyOnWriteArrayList())//ConcurrentLinkedDeque
{


    val receivers =  Array<Receiver?>(4) {null}

    init {
        initReceivers()
    }

    private fun initReceivers() {
        val colours = Colour.values()
        val colourNums = HashSet<Int>()
        for(i in 0..3)
        {
            var colorNum = Random.nextInt(colours.size)
            while (colourNums.contains(colorNum))
                colorNum = Random.nextInt(Colour.values().size)
            receivers[i] = Receiver(i, colours[colorNum])
            colourNums.add(colorNum)
        }
    }

    val slider: Slider = Slider(this)
     fun acceptBubble(bubble: Bubble) : Result {
         var received = false
        if(!bubbles.contains(bubble)) return Result(false, false)
        if(receivers[bubble.line]?.colour == bubble.colour) {
            scores++
            bubble.extraAction()
            received = true
        }
        else lives--

        val frs = arrayOf(Fragment(Random.nextInt(), bubble.X, bubble.Y, bubble.X + 20, bubble.Y + 250, 250, bubble.colour))

        fragments.addAll(frs)

        bubbles.remove(bubble)
         if(!slider.megaBoost && (scores + 1) % speedUpFrequency == 0)
             speedUp()
        return Result(lives == 0, received, frs)
    }
    private var _colours = receivers.map { it!!.colour }
    val colours
    get() = _colours
    fun restart() {
        bubbleDuration = 2000L
        sliderDuration = 1500L
        bubbles.clear()
        slider.X = 0f
        scores = 0
        slider.megaBoost = false
        lives = 5

        initReceivers()
        _colours = receivers.map { it!!.colour }

        fragments.clear()
    }

    fun act(bottom: Float) : Bubble? {
        return slider.act(bottom, getReceiver())
    }

    private fun speedUp() {
        if(bubbleDuration > 1000L) {
            bubbleDuration = round(bubbleDuration * 0.9).toLong()
            sliderDuration = round(sliderDuration * 0.9).toLong()
        } else slider.megaBoost = true
    }

    private fun getReceiver() : Receiver?
    {
        val r = receivers.minBy { abs(slider.centerX - it!!.centerX) }!!
        return if(abs(slider.centerX - r.centerX) < DELTA) r else null
    }
    companion object {
        private var speedUpFrequency = 10
        private const val DELTA = 5
        const val bubbleStandardDuration = 2000L
        var bubbleDuration = bubbleStandardDuration
        var sliderDuration = 1500L
        var ReceiverWidth = 0
        var SliderWidth = 0
    }
}