package com.example.bublesortgame

import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var pauseButton: MenuItem
    private lateinit var gameField: GameField
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameField = GameField(this,{
            pauseButton.isVisible = false// = View.INVISIBLE
            //restartBut.visibility = View.VISIBLE
            results.visibility = View.VISIBLE
            results.text = "Your result is: ${gameField.scores}"
        })
        gameField.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        cont.addView(gameField)
        pauseDrawable = resources.getDrawable(android.R.drawable.ic_media_pause)
        resumeDrawable = resources.getDrawable(android.R.drawable.ic_media_play)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.title = "Scores: 0 Lives: 5"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        pauseButton = menu!!.getItem(1)
        return true
    }

    private lateinit var resumeDrawable: Drawable
    private lateinit var pauseDrawable: Drawable //= resources.getDrawable(android.R.drawable.ic_media_pause)
/*
    fun pause(view: View) {
        gameField.isPaused = !gameField.isPaused
        view.background = if(!gameField.isPaused) pauseDrawable else resumeDrawable
    }*/

    fun pause(item: MenuItem) {
        /*when (item.itemId) {
            R.id.button -> {*/
                gameField.isPaused = !gameField.isPaused
                item.icon = if(!gameField.isPaused) pauseDrawable else resumeDrawable
            /*}
            R.id.restartBut -> {
                gameField.restartGame()
                //item.visibility = View.INVISIBLE
                results.visibility = View.INVISIBLE
                //button.isVisible = true
            }
        }*/
    }

    fun restart(item: MenuItem) {
        gameField.gameOver()
        gameField.restartGame()
        pauseButton.isVisible = true
        pauseButton.icon = pauseDrawable
        //item.visibility = View.INVISIBLE
        results.visibility = View.INVISIBLE
    }
}

