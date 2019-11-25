package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.bublesortgame.Model.Bubble
import com.example.bublesortgame.Model.Game
import com.example.bublesortgame.Model.Slider
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.sqrt


class GameField(context: Context?, private val game: Game = Game()) : View(context) {

    private var sliderAnimator: ObjectAnimator? = null
    private var sliderDuration = 3000L
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
    private var animDuration = 1200L
    //val BubbleRecieversCount = 4
    private val animators: HashSet<Animator> = HashSet()
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private fun addBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofFloat(b,Bubble::Y.name,b.Y, /*-bubbleDiametr.toFloat()*/0f)
        an.duration = animDuration
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                game.bubbles.remove(b)
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {
            }
        })
        bubbleAnims.add(an)
        animators.add(an)
        //an.start()
    }
    private var sliderBitmap = resources.getDrawable(R.drawable.slider,null).toBitmap()
    private var receiverBitmap = resources.getDrawable(R.drawable.receiver,null).toBitmap()
    init{
        Game.ReceiverWidth = receiverBitmap.width
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(animators != null)
        {
            for(a in animators)
                a.start()
            animators.clear()
        }
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = Color.GREEN
        canvas?.drawBitmap(sliderBitmap, game.slider.X ,height.toFloat() - sliderBitmap.height, null)
        for ( b in game.bubbles)
            canvas!!.drawCircle(b.X + bubbleDiametr / 2, b.Y, bubbleDiametr.toFloat() / 2, p )
        for(r in game.receivers)
            canvas!!.drawBitmap(receiverBitmap, r.value.X, 0f, null)
    }

    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w,h,oldw,oldh)
        initSliderAnim()
        bubbleDiametr = width / 8
        sliderBitmap = sliderBitmap.scale(bubbleDiametr, bubbleDiametr)
        Game.SliderWidth = sliderBitmap.width
        for(r in game.receivers)
            r.value.X = r.key * bubbleDiametr * 2f + (bubbleDiametr * 2f - receiverBitmap.width) / 2
    }

    companion object {
        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_THRESHOLD_VELOCITY = 200
        private fun getDistance(x1: Float, y1:Float, x2: Float, y2: Float): Float = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun getNearestLine(x: Float) : Int? = game.receivers.values.minBy { abs(x - it.centerX) }?.number

    private fun getTouchedBubble(x: Float, y: Float) : Bubble? = game.bubbles.firstOrNull { getDistance(x, y, it.getCentralX(bubbleDiametr.toFloat()),
        it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr * 3}
/*
    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            //this@GameField.isPaused = true
            val b = this@GameField.getTouchedBubble(e1.x, e1.y)
            //if(b != null)
                b?.line = getNearestLine(e2.x)?: b?.line!!
                /*if(e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    b.line = getNearestLine(e2.x)?: b.line
                    return false // справа налево
                } else if(e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    b.line
                    return false // слева направо
                }*/

            //this@GameField.isPaused = false
            /*
            if (e1.y - e2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false // снизу вверх
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false // сверху вниз
            }*/
            return false
        }
    }
*/
    init {
        //val gdt = GestureDetector(GestureListener())
        setOnTouchListener{ v,e ->
            //if(true)
            //{
                val b = this@GameField.getTouchedBubble(e.x, e.y)
                //b?.X = e.x
                b?.line = getNearestLine(e.x)?: b?.line!!
            //}
            //gdt.onTouchEvent(e)
            return@setOnTouchListener true
        }
    }

    private fun initSliderAnim() {
        if(sliderAnimator == null) {
        sliderAnimator = ObjectAnimator.ofFloat(game.slider,Slider::X.name,0f, width.toFloat() - sliderBitmap.width)
        sliderAnimator!!.duration = sliderDuration
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