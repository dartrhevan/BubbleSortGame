package com.example.bublesortgame

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bublesortgame.results.ResultsDialog


class MainActivity : AppCompatActivity() {

    private lateinit var pauseButton: MenuItem
    private lateinit var gameField: GameField
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        gameField = GameField(this,{ s ->
            pauseButton.isVisible = false/*
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Result")
                .setMessage("Your result is: ${gameField.scores}\nRecords:\n")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog: DialogInterface?,which: Int ->
                }
                .setNegativeButton("No") { dialog: DialogInterface,id1: Int -> dialog.cancel() }
            val alert = builder.create()*/
            if(s) {
                val dialog = ResultsDialog(this, gameField.scores)
                dialog.show()
            }
            //alert.show()
        })
        gameField.background = ColorDrawable(Color.parseColor("#AAAAAA"))
        setContentView(gameField)
        pauseDrawable = resources.getDrawable(android.R.drawable.ic_media_pause)
        resumeDrawable = resources.getDrawable(android.R.drawable.ic_media_play)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.title = "S 0 L 5"
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#444444")))
        //supportActionBar!!.title = Html.fromHtml("<font color=\"red\">Scores: 0 Lives: 5</font>")//"Scores: 0 Lives: 5"
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
}

