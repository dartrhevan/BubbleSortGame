package com.example.bublesortgame

import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    private lateinit var gameField: GameField
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameField = GameField(this, {
            button.visibility = View.INVISIBLE
            restartBut.visibility = View.VISIBLE
            results.visibility = View.VISIBLE
            results.text = "Your result is: ${gameField.scores}"
        })
        restartBut.setOnClickListener {v ->
            gameField.restartGame()
            v.visibility = View.INVISIBLE
            results.visibility = View.INVISIBLE
            button.visibility = View.VISIBLE
        }
        gameField.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        cont.addView(gameField)
        pauseDrawable = resources.getDrawable(android.R.drawable.ic_media_pause)
        resumeDrawable= resources.getDrawable(android.R.drawable.ic_media_play)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.title = "Scores: 0 Lives: 5"
    }

    private lateinit var resumeDrawable: Drawable
    private lateinit var pauseDrawable: Drawable //= resources.getDrawable(android.R.drawable.ic_media_pause)

    fun pause(view: View) {
        gameField.isPaused = !gameField.isPaused
        view.background = if(!gameField.isPaused) pauseDrawable else resumeDrawable
    }
}
