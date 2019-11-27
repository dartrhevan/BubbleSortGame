package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.bublesortgame.Model.Bubble
import com.example.bublesortgame.Model.Colour
import com.example.bublesortgame.Model.Game
import com.example.bublesortgame.Model.Slider
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.sqrt


class GameField(context: Context?, private val onGameOver: ( ) -> Unit, private val game: Game = Game()) : View(context) {

    private val paints = mutableMapOf<Colour, Paint>()
    init {
        val y = Paint()
        y.style = Paint.Style.FILL
        y.color = Color.YELLOW
        paints[Colour.YELLOW] = y

        val g = Paint()
        g.style = Paint.Style.FILL
        g.color = Color.GREEN
        paints[Colour.GREEN] = g

        val b = Paint()
        b.style = Paint.Style.FILL
        b.color = Color.BLUE
        paints[Colour.BLUE] = b

        val r = Paint()
        r.style = Paint.Style.FILL
        r.color = Color.RED
        paints[Colour.RED] = r
    }
    private var sliderAnimator: ObjectAnimator? = null
    private var _isPaused = false
    var isPaused
    get() = _isPaused
    set(value){
        _isPaused = value
        if(value) {
            sliderAnimator!!.pause()
            for(i in bubbleAnims)
                i.pause()
        }
        else
        {
            sliderAnimator!!.resume()
            for(i in bubbleAnims)
                i.resume()
        }
    }

    private val timer = Timer()

    private var bubbleDiametr = 0
    //val BubbleRecieversCount = 4
    private val animators: HashSet<Animator> = HashSet()
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private fun addBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofFloat(b,Bubble::Y.name,b.Y, /*-bubbleDiametr.toFloat()*/0f)
        an.duration = Game.bubbleDuration
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                if(game.acceptBubble(b))
                    gameOver()
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
        bubbleAnims.add(an)
        animators.add(an)
        //an.start()
    }
    fun gameOver() {
        onGameOver()
        sliderAnimator!!.pause()
        //restartGame()
        isPaused = true
    }

    fun restartGame() {
        isPaused = false
        animators.clear()
        sliderAnimator!!.start()
        bubbleAnims.clear()
        game.restart()
    }

    private var sliderBitmap = resources.getDrawable(R.drawable.slider,null).toBitmap()
    //private var receiverBitmap = resources.getDrawable(R.drawable.receiver_green,null).toBitmap()
    private val receiversBitmap = mapOf(Colour.GREEN to resources.getDrawable(R.drawable.receiver_green,null).toBitmap(),
        Colour.BLUE to resources.getDrawable(R.drawable.receiver_blue,null).toBitmap(),
        Colour.RED to resources.getDrawable(R.drawable.receiver_red,null).toBitmap(),
        Colour.YELLOW to resources.getDrawable(R.drawable.receiver_yel,null).toBitmap())
    init{
        Game.ReceiverWidth = receiversBitmap[Colour.YELLOW]!!.width
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
            for(a in animators)
                a.start()
            animators.clear()
        /*
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = Color.GREEN*/
        canvas?.drawBitmap(sliderBitmap, game.slider.X ,height.toFloat() - sliderBitmap.height, null)
        for ( b in game.bubbles)
            canvas!!.drawCircle(b.X + bubbleDiametr / 2, b.Y, bubbleDiametr.toFloat() / 2, paints[b.colour]!! )
        for(r in game.receivers)
            canvas!!.drawBitmap(receiversBitmap[r.value.colour]!!, r.value.X, 0f, null)
    }

    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w,h,oldw,oldh)
        initSliderAnim()
        bubbleDiametr = width / 8
        sliderBitmap = sliderBitmap.scale(bubbleDiametr, bubbleDiametr)
        Game.SliderWidth = sliderBitmap.width

        synchronized(game.bubbles) {
            for(r in game.receivers)
                r.value.X = r.key * bubbleDiametr * 2f + (bubbleDiametr * 2f - Game.ReceiverWidth) / 2
        }
    }

    companion object {/*
        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_THRESHOLD_VELOCITY = 200*/
        private fun getDistance(x1: Float, y1:Float, x2: Float, y2: Float): Float = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun getNearestLine(x: Float) : Int? = game.receivers.values.minBy { abs(x - it.centerX) }?.number

    private fun getTouchedBubble(x: Float, y: Float) : Bubble? = game.bubbles.firstOrNull { getDistance(x, y, it.getCentralX(bubbleDiametr.toFloat()),
        it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr * 2.5}

    init {
        setOnTouchListener{ v,e ->
                if(isPaused) return@setOnTouchListener true
                val b = this@GameField.getTouchedBubble(e.x, e.y)
                b?.line = getNearestLine(e.x)?: b?.line!!
            return@setOnTouchListener true
        }
    }

    private fun initSliderAnim() {
        if(sliderAnimator == null) {
        sliderAnimator = ObjectAnimator.ofFloat(game.slider,Slider::X.name,0f, width.toFloat() - sliderBitmap.width)
        sliderAnimator!!.duration = Game.sliderDuration
        sliderAnimator!!.repeatMode = ValueAnimator.REVERSE
        sliderAnimator!!.repeatCount = ValueAnimator.INFINITE
        timer.schedule(object : TimerTask() {
            override fun run() {
                if(!_isPaused) {
                    val bub = game.act(height - sliderBitmap.height.toFloat())
                    if(bub != null)
                        addBubbleAnim(bub)
                }
                this@GameField.invalidate()
            }
        }, 0, 15)
        sliderAnimator!!.start()
        }
    }
}