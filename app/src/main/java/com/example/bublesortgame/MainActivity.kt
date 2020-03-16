package com.example.bublesortgame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.bublesortgame.results.ResultsDialog
import kotlinx.android.synthetic.main.activity_start.*


class MainActivity : AppCompatActivity() {

    private lateinit var pauseButton: MenuItem
    private lateinit var gameField: GameField

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameField = GameField(this,{ s ->
            pauseButton.isVisible = false
            if(s) {
                val dialog = ResultsDialog(this, gameField.scores)
                dialog.show()
            }
        })

        setContentView(gameField)
        pauseDrawable = resources.getDrawable(android.R.drawable.ic_media_pause)
        resumeDrawable = resources.getDrawable(android.R.drawable.ic_media_play)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.title = "S 0 L 5"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#444444")))
        gameField.isPaused = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        pauseButton = menu!!.getItem(2)
        return true
    }

    private lateinit var resumeDrawable: Drawable
    private lateinit var pauseDrawable: Drawable


    fun pause(item: MenuItem) {
         gameField.isPaused = !gameField.isPaused
         item.icon = if(!gameField.isPaused) pauseDrawable else resumeDrawable
    }

    fun restart(item: MenuItem) {
        gameField.gameOver(false)
        gameField.restartGame()
        pauseButton.isVisible = true
        pauseButton.icon = pauseDrawable
    }

    fun results(item: MenuItem) {
        val dialog = ResultsDialog(this, -1)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        gameField.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        gameField.mediaPlayer.stop()
    }
}

