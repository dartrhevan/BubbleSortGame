package com.example.bublesortgame

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.children
import com.example.bublesortgame.results.DBResultsProvider
import com.example.bublesortgame.results.makeRecordsView

class RecordsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DBResultsProvider(this)
        val title = TextView(this)
        title.text = "Records"
        val layout = makeRecordsView(this, db.getRecords())
        layout.background = ColorDrawable(this.resources.getColor(R.color.colorAccent))

        setContentView(layout)
        supportActionBar!!.title = "Records"
    }
}
