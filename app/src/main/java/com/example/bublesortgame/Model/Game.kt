package com.example.bublesortgame.Model

import com.example.bublesortgame.Model.bubbles.Bubble
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.round
import kotlin.random.Random

class Game(var scores: Int = 0, var lives: Int = 5, val bubbles: MutableSet<Bubble> =  ConcurrentHashMap.newKeySet(),
           val fragments: MutableSet<Fragment> = ConcurrentHashMap.newKeySet())
{

    val receivers =  Array<Receiver?>(4) {null}

    init {
        currentDifficult = chosenDifficult.copy()
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

     fun acceptBubble(bubble: Bubble, bubbleDiam: Int) : Result {
         var received = false
        if(!bubbles.contains(bubble)) return Result(false, false)
        if(receivers[bubble.line]?.colour == bubble.colour) {
            scores++
            bubble.extraAction()
            received = true
        }
        else lives--
        val frs = generateFragments(bubble, bubbleDiam)
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
        currentDifficult = chosenDifficult.copy()
        bubbles.clear()
        slider.X = 0f
        scores = 0
        slider.megaBoost = false
        lives = 5
        initReceivers()
        _colours = receivers.map { it!!.colour }
        fragments.clear()
        slider.straightDirection = true
    }

    fun act(bottom: Float) : Bubble? {
        return slider.act(bottom, getReceiver())
    }

    private fun speedUp() {
        if(currentDifficult.bubbleDuration > 800L) {
            currentDifficult.bubbleDuration = round(currentDifficult.bubbleDuration * 0.9).toLong()
            currentDifficult.sliderDuration = round(currentDifficult.sliderDuration * 0.9).toLong()
            currentDifficult.delta++
        }
        else slider.megaBoost = true
    }

    private fun getReceiver() : Receiver?
    {
        val r = receivers.minBy { abs(slider.centerX - it!!.centerX) }!!
        return if(abs(slider.centerX - r.centerX) < currentDifficult.delta) r else null
    }

    fun makeTrace(sliderY: Float, bubbleDiam: Int) : ArrayList<Fragment>
            = if(slider.straightDirection) makeLeftTrace(sliderY, bubbleDiam) else makeRightTrace(sliderY, bubbleDiam)

    private fun makeRightTrace(sliderY: Float, bubbleDiam: Int) : ArrayList<Fragment> {
        val res = ArrayList<Fragment>()
        val size = 2 + Random.nextInt(2)
        val spread = bubbleDiam / 4
        for(i in 0 until size)
            res.add(Fragment(slider.X + bubbleDiam, sliderY + 3f * spread + Random.nextInt(-spread / 2, spread / 2),
                slider.X + bubbleDiam + spread / 2 + Random.nextInt(-spread / 4, spread / 4), sliderY + 3f * spread + Random.nextInt(-spread, spread),
                round(1.3 * _traceDuration).toLong(), Colour.BROWN, spread.toFloat() * Random.nextFloat() / 2))
        fragments.addAll(res)
        return res
    }

    private fun makeLeftTrace(sliderY: Float, bubbleDiam: Int): ArrayList<Fragment>  {
        val res = ArrayList<Fragment>()
        val size = 2 + Random.nextInt(2)
        val spread = bubbleDiam / 4
        for(i in 0 until size)
            res.add(Fragment(slider.X, sliderY + 3f * spread + Random.nextInt(-spread / 2, spread / 2),
                slider.X - spread / 2 + Random.nextInt(-spread / 4, spread / 4), sliderY + 3f * spread + Random.nextInt(-spread, spread),
                round(1.3 * _traceDuration).toLong(), Colour.BROWN, spread.toFloat() * Random.nextFloat() / 2))
        fragments.addAll(res)
        return res
    }

    companion object {
        private fun generateFragments(bubble: Bubble, bubbleDiam: Int) : ArrayList<Fragment> {
            val res = ArrayList<Fragment>()
            val size = 5 + Random.nextInt(12)
            val spread = bubbleDiam / 4
            for(i in 0 until size)
                res.add(Fragment(bubble.getCentralX(bubbleDiam.toFloat()) + Random.nextInt(-spread, spread),
                    bubble.getCentralY(bubbleDiam.toFloat())+ Random.nextInt(-spread, spread),
                    bubble.X + 2 * spread + 3 * Random.nextInt(-spread, spread), bubble.Y + bubbleDiam + 1.5f * Random.nextInt(2 * spread),
                    round(350 * Random.nextFloat()).toLong(), bubble.colour, spread.toFloat() * Random.nextFloat() / 2))
            return res
        }

        private var speedUpFrequency = 10

        val bubbleDuration : Long
            get() = currentDifficult.bubbleDuration

        val sliderDuration : Long
            get() = currentDifficult.sliderDuration

        var chosenDifficult = Difficulty.getStandardDifficulty()
        var currentDifficult = chosenDifficult.copy()
        var receiverWidth = 0
        var sliderWidth = 0
        private var _traceDuration = 60L
        val traceDuration
        get() = _traceDuration


    }
}