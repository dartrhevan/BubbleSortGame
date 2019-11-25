package com.example.bublesortgame

import android.animation.Animator
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bublesortgame.Model.Bubble
import com.example.bublesortgame.Model.Game


class MainActivity : AppCompatActivity() {

    private lateinit var child: GameField
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        child = GameField(this)
        //setContentView(GameField(this))
        setContentView(R.layout.activity_main)
        //setContentView(GameField(this))
        //setContentView(R.layout.sample_game_field)
        child.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        cont.addView(child)
    }

    fun pause(view: View) {
        child.isPaused = !child.isPaused
    }
}
