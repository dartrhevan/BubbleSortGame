package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.bublesortgame.Model.Bubble
import com.example.bublesortgame.Model.Game
import com.example.bublesortgame.Model.Slider
import java.util.*


class GameField(context: Context?, private val game: Game = Game(0f)) : View(context) {

    private val sliderAnimator: Animator
    private var sliderDuration = 500L
    private var _isPaused = false
    public var isPaused
    get() = _isPaused
    set(value){
        _isPaused = value
        if(value) {
            sliderAnimator.pause()
            for(i in bubbleAnims)
                i.pause()
        }
        else
        {
            sliderAnimator.resume()
            for(i in bubbleAnims)
                i.resume()
        }
    }

    private val timer = Timer()

    private var bubbleDiametr = 50
    private var animDuration = 500L
    //val BubbleRecieversCount = 4
    private var animator: Animator? = null
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private fun addBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofFloat(b,Bubble::Y.name,b.Y,0f)
        an.duration = animDuration
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {
            }
        })
        bubbleAnims.add(an)
        animator = an
        //an.start()
    }
    init {
        sliderAnimator = ObjectAnimator.ofFloat(game.slider, Slider::X.name, 0f, 500f)
        sliderAnimator.duration = sliderDuration
        sliderAnimator.repeatMode = ValueAnimator.REVERSE
        sliderAnimator.repeatCount = ValueAnimator.INFINITE
        timer.schedule(object : TimerTask() {
            override fun run() {
                val bub = game.act()
                if(bub != null)
                    addBubbleAnim(bub)
                this@GameField.invalidate()
            }
        }, 0, 15)
        sliderAnimator.start()

    }
    private val p = Paint()
    private var slider = resources.getDrawable(R.drawable.slider,null).toBitmap()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(animator != null)
        {
            animator!!.start()
            animator = null
        }
        bubbleDiametr = width / 8
        p.style = Paint.Style.FILL
        p.color = Color.GREEN
        slider = slider.scale(bubbleDiametr, bubbleDiametr)
        canvas?.drawBitmap(slider, game.slider.X ,height.toFloat() - slider.height, null)
        for ( b in game.bubbles)
            canvas!!.drawCircle(b.X, b.Y, bubbleDiametr.toFloat() / 2, p )
    }
    private var r = Rect(0, 0, 200, 200)
    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w,h,oldw,oldh)
    }
}