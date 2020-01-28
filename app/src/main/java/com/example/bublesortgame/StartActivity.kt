package com.example.bublesortgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    fun startGame(v: View) = startActivity(Intent(this, MainActivity::class.java))
    fun exitGame(v: View) = finish()
    fun openRecords(v: View) = startActivity(Intent(this, RecordsActivity::class.java))
}
