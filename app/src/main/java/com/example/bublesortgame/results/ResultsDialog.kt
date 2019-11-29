package com.example.bublesortgame.results

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.widget.*


class ResultsDialog(context: Context, result: Int) : AlertDialog.Builder(context)
{
    init {
        //isOnlyFoldersFilter = false
        //title = createTitle(context)
        //changeTitle()
        val title = TextView(context)
        val tw = TextView(context)
        tw.text = "Enter name to save new reocrd: $result"
        val editText = EditText(context)
        //editText.setText("asds")
        title.text = "Records"
        val linearLayout = LinearLayout(context)
        //linearLayout.addView()
        val listView = ListView(context)
        listView.adapter = ArrayAdapter<Result>(context, android.R.layout.simple_list_item_1, arrayListOf())
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(listView)
        linearLayout.addView(tw)
        linearLayout.addView(editText)
        setCustomTitle(title)
            .setView(linearLayout)
            .setPositiveButton(R.string.ok) { dialog: DialogInterface?, which: Int ->/*
                if (selectedIndex > -1 && listener != null) {
                    listener.OnSelectedFile(listView.getItemAtPosition(selectedIndex).toString())
                }
                if (listener != null && isOnlyFoldersFilter) {
                    listener.OnSelectedFile(currentPath)
                }*/
            }
            .setNegativeButton(R.string.cancel, null)
    }
}