package com.example.bublesortgame.results

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.*


class ResultsDialog(context: Context, result: Int) : AlertDialog.Builder(context)
{
    private val db = DBResultsProvider(context)

    init {
        val records = db.getRecords()
        val linearLayout = makeRecordsView(context, records)
        val title = TextView(context)
        title.text = "Records"
        val tw = TextView(context)
        tw.text = "Enter name to save new reocrd: $result"
        val editText = EditText(context)
        if (result > 0 && (records.count() == 0 || records[0].scores < result)) {
            linearLayout.addView(tw)
            linearLayout.addView(editText)
        }
        setCustomTitle(title)
            .setView(linearLayout)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface?, which: Int ->
                if(result > 0 && (records.count() == 0 || records[0].scores < result))
                    db.addResult(Result(result, editText.text.toString()))
                db.close()
                dialog!!.cancel()
            }
    }
}