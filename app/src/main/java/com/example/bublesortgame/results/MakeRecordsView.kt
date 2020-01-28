package com.example.bublesortgame.results

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView

fun makeRecordsView(context: Context, records: ArrayList<Result>): LinearLayout {
    val linearLayout = LinearLayout(context)
    val listView = ListView(context)
    listView.adapter =
        ArrayAdapter<Result>(
            context,
            R.layout.simple_list_item_1,
            records
        )
    linearLayout.orientation = LinearLayout.VERTICAL
    linearLayout.addView(listView)
    return linearLayout
}