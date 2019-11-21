package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.example.bublesortgame.Model.Bubble
import com.example.bublesortgame.Model.Game

class GameField(context: Context?,private val game: Game) : View(context) {/*
    init{
        inflate(context, R.layout.activity_main, this)
    }*/
    companion object {
        public val BubbleRecieversCount = 4
    }

    private var bubbleRadius : Int = 50
    private var animDuration : Int = 500
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private fun getBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofInt(b,Bubble::Y.name,this.height - bubbleRadius,0)
        an.duration = animDuration as Long
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {
            }
        })
        bubbleAnims.add(an)
        //return anim
    }
    private val p = Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bubbleRadius = width / 4

        //r.set(0, 0, width, 200)
        p.style = Paint.Style.FILL
        p.color = Color.GREEN
        //canvas?.drawPaint(p)
        canvas?.drawRect(r, p)
        //this.draw(canvas)
    }

    private var r = Rect(0, 0, 200, 200)
}