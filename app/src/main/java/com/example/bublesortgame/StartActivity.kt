package com.example.bublesortgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    fun startGame(v: View) {
        try {
            startActivity(Intent(this, MainActivity::class.java))
        }
        catch(e : Exception)
        {
            Snackbar.make(v, e.message!!, Snackbar.LENGTH_LONG)
        }
    }
    fun openSettings(v : View) = startActivity(Intent(this, SettingsActivity::class.java))
    fun exitGame(v: View) = finish()
    fun openRecords(v: View) = startActivity(Intent(this, RecordsActivity::class.java))
}
