package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class GameField(context: Context?, private val onGameOver: ( s:Boolean) -> Unit, private val game: Game = Game()) : View(context) {

    private val paints = mutableMapOf<Colour, Paint>()
    init {/*
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
        Colour.values().map {
            val p = Paint()
            p.style = Paint.Style.FILL
            p.color = it.color
            it to Pair(it, p)
        }*/
        paints.putAll(Colour.values().map {
            val p = Paint()
            p.style = Paint.Style.FILL
            p.color = it.color
            it to  p
        })
    }
    private val shadowedPaints = paints.map {
        val p = Paint()
        p.style = it.value.style
        p.color = it.value.color
        p.setShadowLayer(15f, 0f, 0f, it.value.color)
        it.key to p
    }.toMap()

    init {
        //shadowedPaints.forEach { it.value.setShadowLayer(15f, 0f, 0f, it.value.color)}
        this.setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var sliderAnimator: ObjectAnimator? = null
    private var _isPaused = false
    var isPaused
    get() = _isPaused
    set(value){
        _isPaused = value
        if(value) {
            sliderAnimator?.pause()
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
    private val animators: HashSet<Animator> = HashSet()
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private val animatedReceivers = HashSet<Int>()
    private val radiancePaint : Paint = Paint()
    init {
        radiancePaint.color = Color.LTGRAY
    }
    //@Synchronized
    //@SuppressLint("NewApi")
    private fun addBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofFloat(b,Bubble::Y.name,b.Y, /*-bubbleDiametr.toFloat()*/0f)
        an.duration = Game.bubbleDuration
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                val res = game.acceptBubble(b)
                if(res.endGame)
                    gameOver()
                (context as AppCompatActivity).supportActionBar!!.title = "S ${game.scores} L ${game.lives}"
                if(res.received) {
                    animatedReceivers.add(b.line)
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            animatedReceivers.remove(b.line)
                        }

                    },250)
                }
                //context.actionBar!!.title = "Scores: 0 Lives: 5"
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
        bubbleAnims.add(an)
        animators.add(an)

        radiancePaint.setShadowLayer(25f, 0f, 0f, b.colour.color)
        sliderTimer.purge()
        sliderTimer.schedule(object : TimerTask() {
            override fun run() {
                radiancePaint.clearShadowLayer()
                /*animatedReceivers.remove(b.line)*/
            }
        }, 300)
    }

    private val sliderTimer = Timer()
    fun gameOver(showDialog: Boolean = true) {
        onGameOver(showDialog)
        sliderAnimator!!.pause()
        isPaused = true
        for(ba in bubbleAnims)
            ba.cancel()
    }
    val scores: Int
    get() = game.scores

    fun restartGame() {
        isPaused = false
        animators.clear()
        sliderAnimator!!.start()
        bubbleAnims.clear()
        game.restart()
        (context as AppCompatActivity).supportActionBar!!.title = "S ${game.scores} L ${game.lives}"
    }
    private var receiverBitmap = resources.getDrawable(R.drawable.receiver,null).toBitmap()
    private var sliderBitmap = resources.getDrawable(R.drawable.slider,null).toBitmap()
    //private val radianceBitmap = ColorDrawable(Color.BLACK).toBitmap(sliderBitmap.width, 1)
    private val textPaint = Paint()
    init {
        textPaint.textSize = 50f
        textPaint.color = Color.GRAY
        textPaint.textAlign = Paint.Align.CENTER
    }
/*
    private val receiversBitmap = mapOf(Colour.GREEN to resources.getDrawable(R.drawable.receiver_green,null).toBitmap(),
        Colour.BLUE to resources.getDrawable(R.drawable.receiver_blue,null).toBitmap(),
        Colour.RED to resources.getDrawable(R.drawable.receiver_red,null).toBitmap(),
        Colour.YELLOW to resources.getDrawable(R.drawable.receiver_yel,null).toBitmap())
    init{
        Game.ReceiverWidth = receiversBitmap[Colour.YELLOW]!!.width
    }*/

    //@Synchronized
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(a in animators)
            a.start()
        animators.clear()
        canvas?.drawRect(game.slider.X ,height.toFloat() - sliderBitmap.height, game.slider.X + sliderBitmap.width,height.toFloat() - sliderBitmap.height + 25f, radiancePaint)//
        canvas?.drawBitmap(sliderBitmap, game.slider.X ,height.toFloat() - sliderBitmap.height, null)
        //synchronized(game.bubbles) {
        for(b in game.bubbles) {
            canvas!!.drawCircle(b.X + bubbleDiametr / 2, b.Y,bubbleDiametr / 2f,paints[b.colour]!!)
            canvas.drawText(b.label,b.X + bubbleDiametr / 2,b.Y + bubbleDiametr * 0.125f,textPaint)
            }
        //}
        for(r in game.receivers) {
            /*val p = Paint()
            p.setShadowLayer(30f, 5f, 5f, Color.RED)
            p.color = Color.WHITE
            p.style = Paint.Style.FILL*/
            canvas!!.drawRect(r.value.X + 2, 0f,r.value.X + receiverBitmap.width, receiverBitmap.height.toFloat() ,
                if(animatedReceivers.contains(r.key)) shadowedPaints[r.value.colour]!! else paints[r.value.colour]!!)
            canvas.drawBitmap(receiverBitmap,r.value.X,0f,null)
        }
    }

    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w,h,oldw,oldh)
        initSliderAnim()
        bubbleDiametr = width / 8
        sliderBitmap = sliderBitmap.scale(bubbleDiametr, bubbleDiametr)
        Game.SliderWidth = sliderBitmap.width
        Game.ReceiverWidth = Game.SliderWidth
        receiverBitmap = receiverBitmap.scale(Game.SliderWidth, Game.SliderWidth)
            for(r in game.receivers)
                r.value.X = r.key * bubbleDiametr * 2f + (bubbleDiametr * 2f - Game.ReceiverWidth) / 2f
    }

    companion object {
        private fun getDistance(x1: Float, y1:Float, x2: Float, y2: Float): Float = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun getNearestLine(x: Float) : Int? = game.receivers.values.minBy { abs(x - it.centerX) }?.number

    private fun getTouchingBubble(x: Float,y: Float,radius: Float = 2.45f) : Bubble? = game.bubbles.firstOrNull { getDistance(x, y, it.getCentralX(bubbleDiametr.toFloat()),
        it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr *  radius}


    private fun getTouchedBubble(x: Float,y: Float) : Bubble? =
        game.bubbles.firstOrNull { abs(y - it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr * /*(if(Game.bubbleDuration < 1000) 2.1 else 2.4*/2.35
                && abs(x - it.getCentralX(bubbleDiametr.toFloat())) <= bubbleDiametr * /*if(Game.bubbleDuration < 1000) 1.6 else 1.8*/2}

    init {
        setOnTouchListener{ v,e ->
            //(context as MainActivity).supportActionBar!!.title = e.action.toString().
            if(isPaused) return@setOnTouchListener true
            if(e.action == MotionEvent.ACTION_DOWN)
            {
                val b = this@GameField.getTouchedBubble(e.x, e.y)
                b?.line = getNearestLine(e.x)?: b?.line!!
            }
            else/*if(e.action == MotionEvent.ACTION_MOVE) */{
                val b = this@GameField.getTouchingBubble(e.x, e.y )//if(e.action == MotionEvent.ACTION_MOVE) 2.35f else if(e.action == MotionEvent.ACTION_DOWN) 1.3f else 0f)
                b?.line = getNearestLine(e.x)?: b?.line!!
            }
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
        sliderAnimator!!.pause()
        }
    }
}