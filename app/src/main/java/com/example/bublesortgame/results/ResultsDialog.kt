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
    //companion object {
        private val db = DBResultsProvider(context)
    //}
    init {
        //isOnlyFoldersFilter = false
        //title = createTitle(context)
        //changeTitle()
        //if(db == null)
           // db = DBResultsProvider(context)
        val records = db.getRecords()
        val title = TextView(context)
        val tw = TextView(context)
        tw.text = "Enter name to save new reocrd: $result"
        val editText = EditText(context)
        // editText.setText("asds")
        title.text = "Records"
        val linearLayout = LinearLayout(context)
        //linearLayout.addView()
        val listView = ListView(context)
        listView.adapter = ArrayAdapter<Result>(context, android.R.layout.simple_list_item_1,records )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(listView)
        if(records.count() == 0 || records[0].scores < result)
        {
        linearLayout.addView(tw)
        linearLayout.addView(editText)
        }
        setCustomTitle(title)
            .setView(linearLayout)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface?, which: Int ->/*
                if (selectedIndex > -1 && listener != null) {
                    listener.OnSelectedFile(listView.getItemAtPosition(selectedIndex).toString())
                }
                if (listener != null && isOnlyFoldersFilter) {
                    listener.OnSelectedFile(currentPath)
                }*/

                if(records.count() == 0  || records[0].scores < result)
                db.addResult(Result(result, editText.text.toString()))
                db.close()
                dialog!!.cancel()
            }
            //.setNegativeButton(R.string.cancel, null)
    }
}