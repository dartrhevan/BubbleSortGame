package com.example.bublesortgame

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.example.bublesortgame.Model.*
import com.example.bublesortgame.Model.bubbles.Bubble
import com.example.bublesortgame.Model.bubbles.ExtraLifeBubble
import com.example.bublesortgame.Model.bubbles.StandartBubble
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

class GameField(context: Context?, private val onGameOver: (s:Boolean) -> Unit, private val game: Game = Game()) : View(context) {

    private val ambientMediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.soundtrack)
    init {
        ambientMediaPlayer.isLooping = true
        //ambientMediaPlayer.start()
    }
    private val animTimer: Timer = Timer()
    private val paints = mutableMapOf<Colour, Paint>()
    private val radiancePaints = mutableMapOf<Colour, Paint>()
    init {
        initPaints()
    }
    private fun initPaints() {
        paints.clear()
        paints.putAll(game.colours.map {
            val p = Paint()
            p.style = Paint.Style.FILL
            p.color = it.color
            it to  p
        })
        val p = Paint()
        p.style = Paint.Style.FILL
        p.color = Colour.BROWN.color
        paints[Colour.BROWN] = p
        radiancePaints.clear()
        radiancePaints.putAll(paints.map {
            val p = Paint()
            p.style = it.value.style
            p.color = it.value.color
            p.setShadowLayer(15f, 0f, 0f, it.value.color)
            it.key to p
        }.toMap())
    }

    private val backs = arrayOf(context!!.resources.getColor(R.color.colorAccent), Color.parseColor("#AAAAAA"), Color.parseColor("#FFFFFF"))

    init {
        this.setLayerType(LAYER_TYPE_SOFTWARE, null)
        setBackground()
    }

    private fun setBackground() {
        background = ColorDrawable(backs[Random.nextInt(backs.size)])

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
            if (ambientMediaPlayer.isPlaying)
                ambientMediaPlayer.pause()
        }
        else
        {
            if (!ambientMediaPlayer.isPlaying)
                ambientMediaPlayer.start()
            sliderAnimator!!.resume()
            for(i in bubbleAnims)
                i.resume()
        }
    }

    private val timer = Timer()

    private var bubbleDiametr = 0
    private val animators: MutableSet<Animator> = ConcurrentHashMap.newKeySet()
    private val bubbleAnims: ArrayList<Animator> = ArrayList()
    private val animatedReceivers = HashSet<Int>()
    private val radiancePaint : Paint = Paint()
    init {
        radiancePaint.color = Color.LTGRAY
    }

    private fun addBubbleAnim(b: Bubble) {
        val an = ObjectAnimator.ofFloat(b, Bubble::Y.name,b.Y, 0f)
        an.duration = Game.bubbleDuration
        an.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onAnimationEnd(p0: Animator?) {
                val res = game.acceptBubble(b, bubbleDiametr)
                if(res.endGame && !_isPaused)
                    gameOver()
                (context as AppCompatActivity).supportActionBar!!.title = "S ${game.scores} L ${game.lives}"
                this@GameField.animateReceivers(b, res)
                this@GameField.animateFragments(res.fragments)
            }
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
        bubbleAnims.add(an)
        animators.add(an)
        radiancePaint.setShadowLayer(25f, 0f, 0f, b.colour.color)
        sliderTimer.purge()
        sliderTimer.schedule(object : TimerTask() {override fun run() = radiancePaint.clearShadowLayer()}, 300)
    }

    fun animateReceivers(b: Bubble, res: Result) {
        if(res.received) {
            animatedReceivers.add(b.line)
            timer.schedule(object : TimerTask() {
                override fun run() {
                    animatedReceivers.remove(b.line)
                }
            },250)
        }
    }

    fun animateFragments(fragments: ArrayList<Fragment>) {
        for(fr in fragments) {
            val ax = ObjectAnimator.ofFloat(fr, Fragment::Y.name, fr.tY)
            val ay = ObjectAnimator.ofFloat(fr, Fragment::X.name, fr.tX)
            ax.duration = fr.duration
            ay.duration = fr.duration
            animators.add(ax)
            animators.add(ay)

            //Log.println(Log.DEBUG, "", "ADD: ${fragments.size}")
            ay.addListener(object: AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    game.fragments.remove(fr)
                }
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            } )
        }
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
        initReceivers()
        initPaints()
        setBackground()
    }

    fun close() {
        Game.bubbleDuration = 2000L
        Game.sliderDuration = 1500L
        game.slider.straightDirection = true
    }

    private var receiverBitmap = resources.getDrawable(R.drawable.receiver,null).toBitmap()
    private var sliderBitmap = resources.getDrawable(R.drawable.slider,null).toBitmap()
    private val textPaint = Paint()
    init {
        textPaint.textSize = 50f
        textPaint.color = Color.GRAY
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for(a in animators)
            if(!a.isStarted)
                a.start()

        for(fr in game.fragments)
            canvas!!.drawCircle(fr.X, fr.Y,fr.diam, paints[fr.colour]!!)
        canvas?.drawRect(game.slider.X ,getSliderY(), game.slider.X + sliderBitmap.width,height.toFloat() - sliderBitmap.height + 25f, radiancePaint)//
        canvas?.drawBitmap(sliderBitmap, game.slider.X ,height.toFloat() - sliderBitmap.height, null)
        for(b in game.bubbles)
            drawBubble(b, canvas!!)
        for(r in game.receivers) {
            canvas!!.drawRect(r!!.X + 2, 0f,r.X + receiverBitmap.width, receiverBitmap.height.toFloat() ,
                if(animatedReceivers.contains(r.number)) radiancePaints[r.colour]!! else paints[r.colour]!!)
            canvas.drawBitmap(receiverBitmap,r.X,0f,null)
        }

    }

    private fun drawBubble(b: Bubble, canvas: Canvas) {
        canvas.drawCircle(b.X + bubbleDiametr / 2, b.Y,bubbleDiametr / 2f, if(b is StandartBubble) paints[b.colour]!! else radiancePaints[b.colour]!!)
        if(b is ExtraLifeBubble) {
            val path = Path()
            path.moveTo(b.getCentralX(bubbleDiametr.toFloat()) - bubbleDiametr / 2, b.Y)
            path.lineTo(b.getCentralX(bubbleDiametr.toFloat()) + bubbleDiametr / 2, b.Y)
            path.lineTo(b.getCentralX(bubbleDiametr.toFloat()), b.getCentralY(bubbleDiametr.toFloat()) + 2 * bubbleDiametr)
            canvas.drawPath(path, radiancePaints[b.colour]!!)
        }
        canvas.drawText(b.label,b.X + bubbleDiametr / 2,b.Y + bubbleDiametr * 0.125f,textPaint)
    }

    private fun getSliderY(): Float = height.toFloat() - sliderBitmap.height
    override fun onSizeChanged(w: Int,h: Int,oldw: Int,oldh: Int) {
        super.onSizeChanged(w,h,oldw,oldh)
        initSliderAnim()
        bubbleDiametr = width / 8
        sliderBitmap = sliderBitmap.scale(bubbleDiametr, bubbleDiametr)
        Game.SliderWidth = sliderBitmap.width
        Game.ReceiverWidth = Game.SliderWidth
        receiverBitmap = receiverBitmap.scale(Game.SliderWidth, Game.SliderWidth)
        initReceivers()
    }

    private fun initReceivers() {
        for(r in game.receivers)
            r!!.X = r.number * bubbleDiametr * 2f + (bubbleDiametr * 2f - Game.ReceiverWidth) / 2f
    }

    companion object {
        //private var frAn= 0
        private fun getDistance(x1: Float, y1:Float, x2: Float, y2: Float): Float = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun getNearestLine(x: Float) : Int? = game.receivers.minBy { abs(x - it!!.centerX) }?.number

    private fun getTouchingBubble(x: Float,y: Float,radius: Float = 2.45f) : Bubble? = game.bubbles.firstOrNull { getDistance(x, y, it.getCentralX(bubbleDiametr.toFloat()),
        it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr *  radius}


    private fun getTouchedBubble(x: Float,y: Float) : Bubble? =
        game.bubbles.firstOrNull { abs(y - it.getCentralY(bubbleDiametr.toFloat())) <= bubbleDiametr * 2.35
                && abs(x - it.getCentralX(bubbleDiametr.toFloat())) <= bubbleDiametr * 2}

    init {
        setOnTouchListener{ v,e ->
            if(isPaused) return@setOnTouchListener true
            if(e.action == MotionEvent.ACTION_DOWN)
            {
                val b = this@GameField.getTouchedBubble(e.x, e.y)
                b?.line = getNearestLine(e.x)?: b?.line!!
            }
            else {
                val b = this@GameField.getTouchingBubble(e.x, e.y )
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
                (this@GameField.context as MainActivity).runOnUiThread {
                    this@GameField.invalidate()
                }
            }
        }, 0, 17)
        sliderAnimator!!.doOnRepeat { game.slider.straightDirection = !game.slider.straightDirection }
        animTimer.schedule(object : TimerTask() {
            override fun run() {
                if (!_isPaused && bubbleDiametr > 0) {
                    animators.removeIf { it.isStarted }
                    val f = game.makeTrace(getSliderY(), bubbleDiametr)
                    animateFragments(f)
                }
            }
        }, 0, Game.traceDuration)
        sliderAnimator!!.start()
        sliderAnimator!!.pause()
        }
    }

}