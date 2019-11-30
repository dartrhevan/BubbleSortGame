package com.example.bublesortgame.results

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import com.example.bublesortgame.results.Result
import java.util.*
import kotlin.collections.ArrayList


class DBResultsProvider(ct: Context)
{
    private val db = ct.openOrCreateDatabase("recs.db", MODE_PRIVATE, null)
    init {
        db.execSQL("CREATE TABLE IF NOT EXISTS results (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, describe TEXT, scores INTEGER NOT NULL, time INTEGER)")
    }

    fun addResult(res: Result) =
        db.execSQL("INSERT INTO results (describe, scores, time) VALUES ('${res.desc}', ${res.scores}, ${res.date.time})")


    fun getRecords() : ArrayList<Result> {
        val res = ArrayList<Result>()
        val query = db.rawQuery("SELECT * FROM results ORDER BY scores DESC",null)
        if(query.moveToFirst())
        //for(i in 0..9) {
            do {
                if(res.count() <= 10)
                res.add(Result(query.getInt(2),query.getString(1),Date(query.getLong(3))))
                else db.execSQL("DELETE FROM results WHERE id = ${query.getInt(0)}")
            } while(query.moveToNext())
        //}
        query.close()
        return res
    }
    fun close() = db.close()

    /*
    companion object {
        private var instance: DBProvider? = null
        fun getInstance(): DBProvider {
            if(instance == null)
                instance = DBProvider()
            return instance!!
        }
    }*/
}