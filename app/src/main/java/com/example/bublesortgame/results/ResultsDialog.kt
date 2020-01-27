package com.example.bublesortgame.results

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.widget.*
import androidx.core.content.contentValuesOf


class ResultsDialog(context: Context, result: Int) : AlertDialog.Builder(context)
{
    private val db = DBResultsProvider(context)

    init {
        val records = db.getRecords()
        val (title, editText, linearLayout) = makeRecordsView(context, result, records)
        setCustomTitle(title)
            .setView(linearLayout)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface?, which: Int ->
                if(records.count() == 0  || records[0].scores < result)
                db.addResult(Result(result, editText.text.toString()))
                db.close()
                dialog!!.cancel()
            }
    }

    private fun makeRecordsView(context: Context, result: Int, records: ArrayList<Result>): Triple<TextView, EditText, LinearLayout> {
        val title = TextView(context)
        val tw = TextView(context)
        tw.text = "Enter name to save new reocrd: $result"
        val editText = EditText(context)
        // editText.setText("asds")
        title.text = "Records"
        val linearLayout = LinearLayout(context)
        //linearLayout.addView()
        val listView = ListView(context)
        listView.adapter = ArrayAdapter<Result>(context, R.layout.simple_list_item_1, records)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(listView)
        if (records.count() == 0 || records[0].scores < result) {
            linearLayout.addView(tw)
            linearLayout.addView(editText)
        }
        return Triple(title, editText, linearLayout)
    }
}